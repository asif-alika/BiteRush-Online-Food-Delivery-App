package com.ty.BiteRush.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.ty.BiteRush.entity.User;
import com.ty.BiteRush.service.OrderService;
import com.ty.BiteRush.service.PaymentService;
import com.ty.BiteRush.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CheckoutController {

    @Autowired private PaymentService paymentService;
    @Autowired private OrderService orderService;
    @Autowired private UserService userService;

    // ✅ Inject Razorpay keys securely from application.properties
    @Value("${razorpay.key_id}")
    private String razorpayKey;

    @Value("${razorpay.key_secret}")
    private String razorpaySecret;

    // ✅ Step 1: Create BiteRush + Razorpay order
    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails ud,
                           @RequestParam String addressLine1,
                           @RequestParam(required = false) String addressLine2,
                           @RequestParam String city,
                           @RequestParam String state,
                           @RequestParam String zip,
                           Model model) throws Exception {

        // Fetch authenticated user
        User user = userService.getByUsername(ud.getUsername());
        if (user == null) {
            System.err.println("❌ Authenticated user not found in DB!");
            return "redirect:/login?error";
        }

        System.out.println("🧾 Checkout started for user: " + user.getUsername());

        // ✅ Create local order
        com.ty.BiteRush.entity.Order order =
                orderService.checkout(user, addressLine1, addressLine2, city, state, zip);
        double amount = order.getTotal();

        // ✅ Initialize Razorpay client (Test Mode)
        RazorpayClient client = new RazorpayClient(razorpayKey, razorpaySecret);

        // ✅ Create order request for Razorpay
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (amount * 100)); // amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_" + order.getId());

        Order razorOrder = client.orders.create(orderRequest);

        // ✅ Pass data to payment.html
        model.addAttribute("razorOrderId", razorOrder.get("id"));
        model.addAttribute("orderId", order.getId());
        model.addAttribute("amount", amount);
        model.addAttribute("addressLine1", addressLine1);
        model.addAttribute("addressLine2", addressLine2);
        model.addAttribute("city", city);
        model.addAttribute("state", state);
        model.addAttribute("zip", zip);
        model.addAttribute("razorpayKey", razorpayKey);

        System.out.println("✅ Redirecting to payment.html for order " + order.getId());
        return "payment";
    }

    // ✅ Step 2: Handle payment success callback
    @PostMapping("/payment-success")
    public String paymentSuccess(@RequestParam String razorpay_payment_id,
                                 @RequestParam String razorpay_order_id,
                                 @RequestParam String razorpay_signature,
                                 @RequestParam Long orderId,
                                 @AuthenticationPrincipal UserDetails ud,
                                 Model model) {

        // Verify payment
        boolean verified = paymentService.verifyPayment(
                razorpay_order_id,
                razorpay_payment_id,
                razorpay_signature,
                razorpaySecret
        );

        if (verified) {
            orderService.markAsPaid(orderId, razorpay_payment_id);
            User user = userService.getByUsername(ud.getUsername());

            // ✅ Add model attributes for success page
            model.addAttribute("paymentId", razorpay_payment_id);
            model.addAttribute("orderId", orderId);
            model.addAttribute("amount", orderService.getTotalById(orderId));
            model.addAttribute("addressLine1", user.getAddressLine1());
            model.addAttribute("addressLine2", user.getAddressLine2());
            model.addAttribute("city", user.getCity());
            model.addAttribute("state", user.getState());
            model.addAttribute("zip", user.getZip());

            System.out.println("✅ Payment verified and order marked as PAID: " + orderId);
            return "order-success";
        } else {
            System.err.println("❌ Payment verification failed!");
            model.addAttribute("errorTitle", "Payment Failed");
            model.addAttribute("errorMessage", "We couldn’t verify your payment. Please try again or contact support.");
            return "payment-failed";
        }
    }
}
