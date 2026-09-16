package com.library.ReadFlow.services.impl;

import com.library.ReadFlow.domain.PaymentGateway;
import com.library.ReadFlow.domain.PaymentStatus;
import com.library.ReadFlow.entites.Payment;
import com.library.ReadFlow.entites.Subscription;
import com.library.ReadFlow.entites.User;
import com.library.ReadFlow.event.publisher.PaymentEventPublisher;
import com.library.ReadFlow.mapper.PaymentMapper;
import com.library.ReadFlow.payload.dtos.PaymentDTO;
import com.library.ReadFlow.payload.request.PaymentInitiateRequest;
import com.library.ReadFlow.payload.request.PaymentVerifyRequest;
import com.library.ReadFlow.payload.response.PaymentInitiateResponse;
import com.library.ReadFlow.payload.response.PaymentLinkResponse;
import com.library.ReadFlow.repositories.PaymentRepository;
import com.library.ReadFlow.repositories.SubscriptionRepository;
import com.library.ReadFlow.repositories.UserRepository;
import com.library.ReadFlow.services.PaymentService;
import com.library.ReadFlow.services.gateway.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RazorpayService razorpayService;
    private final PaymentMapper paymentMapper;
    private final PaymentEventPublisher paymentEventPublisher;


    @Override
    public PaymentInitiateResponse initiatePayment(PaymentInitiateRequest req) {
        User user = userRepository.findById(req.getUserId()).get();

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentType(req.getPaymentType());
        payment.setPaymentGateway(req.getPaymentGateway());
        payment.setAmount(req.getAmount());

        payment.setDescription(req.getDescription());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId("TXN_"+ UUID.randomUUID());
        payment.setInitiatedAt(LocalDateTime.now());

        if(req.getSubscriptionId() != null){
            Subscription sub = subscriptionRepository
                    .findById(req.getSubscriptionId())
                    .orElseThrow(() -> new RuntimeException("Subscription not found"));

            payment.setSubscription(sub);
        }

        payment=paymentRepository.save(payment);
        PaymentInitiateResponse response = new PaymentInitiateResponse();

        if(req.getPaymentGateway() == PaymentGateway.RAZORPAY){
            PaymentLinkResponse paymentLinkResponse = razorpayService.createPaymentLink(
                    user, payment
            );
            response=PaymentInitiateResponse.builder()
                    .paymentId(payment.getId())
                    .gateway(payment.getPaymentGateway())
                    .checkoutUrl(paymentLinkResponse.getPayment_link_url())
                    .transactionId(paymentLinkResponse.getPayment_link_id())
                    .amount(payment.getAmount())
                    .description(payment.getDescription())
                    .success(true)
                    .message("payment Initiated Successfully")
                    .build();

            payment.setGatewayOrderId(paymentLinkResponse.getPayment_link_id());
        }
        payment.setPaymentStatus(PaymentStatus.PROCESSING);
        paymentRepository.save(payment);

        // Payment Initiate Event

        return response;
    }

    @Override
    public PaymentDTO verifyPayment(PaymentVerifyRequest req) {

        JSONObject paymentDetails = razorpayService.fetchPaymentDetails(
                req.getRazorpayPaymentId()
        );

        JSONObject notes = paymentDetails.getJSONObject("notes");

        Long paymentId = Long.parseLong(notes.optString("payment_id"));

        Payment payment = paymentRepository.findById(paymentId).get();

        boolean isValid = razorpayService.isValidPayment(req.getRazorpayPaymentId());

        if(PaymentGateway.RAZORPAY == payment.getPaymentGateway()){
            if(isValid){
                payment.setGatewayOrderId(req.getRazorpayPaymentId());
            }
        }
        if(isValid){
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setCompletedAt(LocalDateTime.now());
            payment=paymentRepository.save(payment);

            // todo: publish payment success event
            paymentEventPublisher.publishPaymentSuccessEvent(payment);

        }
        return paymentMapper.toDTO(payment);
    }

    @Override
    public Page<PaymentDTO> getAllPayments(Pageable pageable) {

        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(paymentMapper::toDTO);
    }
}
