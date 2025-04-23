package org.project1.shopweb.service.payment;

import jakarta.servlet.http.HttpServletRequest;
import org.project1.shopweb.dto.payment.PaymentDTO;

import java.io.IOException;

public interface IPaymentService {

    String createPaymentUrl(PaymentDTO paymentRequest, HttpServletRequest request);
//    String queryTransaction(PaymentQueryDTO paymentQueryDTO, HttpServletRequest request) throws IOException;
//    String refundTransaction(PaymentRefundDTO refundDTO) throws IOException;
}
