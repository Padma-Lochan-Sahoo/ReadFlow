package com.library.ReadFlow.payload.request;

import com.library.ReadFlow.domain.PaymentGateway;
import com.library.ReadFlow.domain.PaymentStatus;
import com.library.ReadFlow.domain.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInitiateRequest {

    @NotNull(message = "User ID is mandatory")
    private Long userId;

    private Long bookLoanId;

    @NotNull(message = "Payment Type is mandatory")
    private PaymentType paymentType;

    @NotNull(message = "Payment gateway is mandatory")
    private PaymentGateway paymentGateway;

    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    private Long amount;

    @Size(max = 500, message = "Description must not exceed 500 character")
    private String description;

    private Long fineId;
    private Long subscriptionId;
    private PaymentStatus paymentStatus;

    @Size(max = 500, message = "Success URL must not exceed 500 character")
    private String successUrl;

    @Size(max = 500, message = "cancel URL must not exceed 500 character")
    private String cancelUrl;
}
