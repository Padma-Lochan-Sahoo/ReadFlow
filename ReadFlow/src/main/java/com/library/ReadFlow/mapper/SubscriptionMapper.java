package com.library.ReadFlow.mapper;

import com.library.ReadFlow.entites.Subscription;
import com.library.ReadFlow.entites.SubscriptionPlan;
import com.library.ReadFlow.entites.User;
import com.library.ReadFlow.exceptions.SubscriptionException;
import com.library.ReadFlow.payload.dtos.SubscriptionDTO;
import com.library.ReadFlow.repositories.SubscriptionPlanRepository;
import com.library.ReadFlow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {

    private final SubscriptionPlanRepository planRepository;
    private final UserRepository userRepository;

    public SubscriptionDTO toDTO(Subscription subscription){
        if(subscription == null){
            return null;
        }

        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(subscription.getId());

        // User Information
        if(subscription.getUser() != null){
            dto.setUserId(subscription.getUser().getId());
            dto.setUserName(subscription.getUser().getFullName());
            dto.setUserEmail(subscription.getUser().getEmail());
        }

        // Plan Information
        if(subscription.getPlan() != null){
            dto.setPlanId(subscription.getPlan().getId());
        }
        dto.setPlanName(subscription.getPlanName());
        dto.setPlanCode(subscription.getPlanCode());
        dto.setPrice(subscription.getPrice());

        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setIsActive(subscription.getIsActive());
        dto.setMaxBooksAllowed(subscription.getMaxBooksAllowed());
        dto.setMaxDaysPerBook(subscription.getMaxDaysPerBook());
        dto.setAutoRenew(subscription.getAutoRenew());
        dto.setCancelledAt(subscription.getCancelledAt());
        dto.setCancellationReason(subscription.getCancellationReason());
        dto.setNotes(subscription.getNotes());
        dto.setCreatedAt(subscription.getCreatedAt());
        dto.setUpdatedAt(subscription.getUpdatedAt());

        return dto;
    }


    public Subscription toEntity(SubscriptionDTO dto, SubscriptionPlan plan, User user){
        if(dto == null){
            return null;
        }
        Subscription subscription = new Subscription();
        subscription.setId(dto.getId());

        subscription.setUser(user);
        subscription.setPlan(plan);

        subscription.setNotes(dto.getNotes());
        return subscription;
    }

    public List<SubscriptionDTO> toDTOList(List<Subscription> subscriptions){
        if(subscriptions == null){
            return null;
        }
        return subscriptions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
