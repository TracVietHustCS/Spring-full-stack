package org.project1.shopweb.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.project1.shopweb.component.VNPayConfig;
import org.project1.shopweb.component.VNPayUtils;
import org.project1.shopweb.dto.payment.PaymentDTO;
import org.project1.shopweb.service.payment.IPaymentService;
import org.project1.shopweb.service.payment.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/payments")
public class PaymentController {
    private final PaymentService vnPayService;

    @PostMapping("/create_payment_url")
    public ResponseEntity<?> createPayment(@RequestBody PaymentDTO paymentRequest, HttpServletRequest request) {

            String paymentUrl = vnPayService.createPaymentUrl(paymentRequest, request);

            return ResponseEntity.ok(paymentUrl);

    }
    // call back xong fe gui code + va update status order toi api be


    @GetMapping("/vnpay-callback")


    public ResponseEntity<String> handleVNPayCallback(@RequestParam Map<String, String> allParams) {
        // Extract secure hash to validate
        String receivedHash = allParams.remove("vnp_SecureHash");
        String receivedHashType = allParams.remove("vnp_SecureHashType"); // Optional

        // Validate signature
        VNPayUtils vnpayUtils = new VNPayUtils(new VNPayConfig()); // Normally autowired
        String computedHash = vnpayUtils.hashAllFields(allParams);

        if (receivedHash != null && receivedHash.equals(computedHash)) {
            // ✅ Signature OK - Handle success/failure based on vnp_ResponseCode
            String responseCode = allParams.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                return ResponseEntity.ok("Payment successful");
            } else {
                return ResponseEntity.ok("Payment failed with code: " + responseCode);
            }
        } else {
            // ❌ Invalid signature
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature!");
        }
    }
}
