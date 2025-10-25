package com.ty.BiteRush.service;

import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    // ✅ Verifies Razorpay signature to confirm payment authenticity
    public boolean verifyPayment(String orderId, String paymentId, String signature, String secret) {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", orderId);
            attributes.put("razorpay_payment_id", paymentId);
            attributes.put("razorpay_signature", signature);

            // Razorpay-provided signature validation utility
            return Utils.verifyPaymentSignature(attributes, secret);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
