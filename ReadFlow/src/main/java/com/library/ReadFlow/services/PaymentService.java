package com.library.ReadFlow.services;

import com.library.ReadFlow.payload.dtos.PaymentDTO;
import com.library.ReadFlow.payload.request.PaymentInitiateRequest;
import com.library.ReadFlow.payload.request.PaymentVerifyRequest;
import com.library.ReadFlow.payload.response.PaymentInitiateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest req);

    PaymentDTO verifyPayment(PaymentVerifyRequest req);

    Page<PaymentDTO> getAllPayments(Pageable pageable);

    
}
