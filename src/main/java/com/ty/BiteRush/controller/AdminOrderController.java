package com.ty.BiteRush.controller;

import com.ty.BiteRush.entity.Order;
import com.ty.BiteRush.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderRepository orderRepository;

    // ✅ View all customer orders
    @GetMapping
    public String viewOrders(Model model) {
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "admin-orders"; // Renders admin-orders.html
    }

    // ✅ Mark order as delivered (no email sending)
    @PostMapping("/{id}/delivered")
    public String markAsDelivered(@PathVariable Long id) {
        orderRepository.findById(id).ifPresent(order -> {
            order.setStatus("DELIVERED");
            orderRepository.save(order);
            System.out.println("✅ Order #" + id + " marked as DELIVERED.");
        });
        return "redirect:/admin/orders?delivered";
    }
}
