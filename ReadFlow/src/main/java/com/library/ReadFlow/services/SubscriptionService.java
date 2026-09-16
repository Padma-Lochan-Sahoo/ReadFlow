package com.library.ReadFlow.services;

import com.library.ReadFlow.payload.dtos.SubscriptionDTO;
import com.library.ReadFlow.payload.response.PaymentInitiateResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionService {
    PaymentInitiateResponse subscribe(SubscriptionDTO subscriptionDTO);

    SubscriptionDTO getUsersActiveSubscription(Long userId);

    SubscriptionDTO cancelSubscription(Long subscriptionId,String reason);

    SubscriptionDTO activateSubscription(Long subscriptionId,Long paymentId);

    List<SubscriptionDTO> getAllSubscriptions(Pageable pageable);

    void deactivateExpiredSubscriptions();
}
