package com.library.ReadFlow.services.impl;

import com.library.ReadFlow.entites.Subscription;
import com.library.ReadFlow.entites.SubscriptionPlan;
import com.library.ReadFlow.entites.User;
import com.library.ReadFlow.exceptions.SubscriptionException;
import com.library.ReadFlow.mapper.SubscriptionMapper;
import com.library.ReadFlow.payload.dtos.SubscriptionDTO;
import com.library.ReadFlow.repositories.SubscriptionPlanRepository;
import com.library.ReadFlow.repositories.SubscriptionRepository;
import com.library.ReadFlow.services.SubscriptionService;
import com.library.ReadFlow.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserService userService;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public SubscriptionDTO subscribe(SubscriptionDTO subscriptionDTO) {
        User user = userService.getCurrentUser();

        SubscriptionPlan plan = subscriptionPlanRepository
                .findById(subscriptionDTO.getPlanId())
                .orElseThrow(
                        () -> new RuntimeException("Plan not found!")
                );
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO,plan, user);
        subscription.initializeFromPlan();
        subscription.setIsActive(false);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        //  todo: Create Payment


        return subscriptionMapper.toDTO(savedSubscription);
    }

    @Override
    public SubscriptionDTO getUsersActiveSubscription(Long userId) {
        User user = userService.getCurrentUser();

        Subscription subscription = subscriptionRepository
                .findActiveSubscriptionById(user.getId(), LocalDate.now())
                .orElseThrow(() -> new SubscriptionException("No Active Subscription Found"));

        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException(
                        "Subscription Not Found With ID: "+subscriptionId
                ));
        if(!subscription.getIsActive()){
            throw new SubscriptionException("Subscription is already inactive");
        }

        // Mark As Cancelled
        subscription.setIsActive(false);
        subscription.setCancelledAt(LocalDateTime.now());
        subscription.setCancellationReason(reason != null ? reason : "Cancelled By User");

        subscription = subscriptionRepository.save(subscription);
        log.info("Subscription Cancelled Successfully: {}",subscriptionId);

        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException(
                        "Subscription Not Found With ID: "+subscriptionId
                ));
        // todo: Verify Payment

        subscription.setIsActive(true);
        subscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public List<SubscriptionDTO> getAllSubscriptions(Pageable pageable) {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptionMapper.toDTOList(subscriptions);
    }

    @Override
    public void deactivateExpiredSubscriptions() {
        List<Subscription> expiredSubscriptions = subscriptionRepository
                .findExpiredActiveSubscriptions(LocalDate.now());

        for(Subscription subscription : expiredSubscriptions){
            subscription.setIsActive(false);
            subscriptionRepository.save(subscription);
        }
    }
}
