package com.library.ReadFlow.services.impl;

import com.library.ReadFlow.entites.SubscriptionPlan;
import com.library.ReadFlow.entites.User;
import com.library.ReadFlow.mapper.SubscriptionPlanMapper;
import com.library.ReadFlow.payload.dtos.SubscriptionPlanDTO;
import com.library.ReadFlow.repositories.SubscriptionPlanRepository;
import com.library.ReadFlow.services.SubscriptionPlanService;
import com.library.ReadFlow.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    private final SubscriptionPlanRepository planRepository;
    private final SubscriptionPlanMapper planMapper;
    private final UserService userService;

    @Override
    public SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO planDTO) {
        if(planRepository.existsByPlanCode(planDTO.getPlanCode())){
            throw new RuntimeException("Plan code is already exists");
        }
        SubscriptionPlan plan = planMapper.toEntity(planDTO);

        User currentUser = userService.getCurrentUser();
        plan.setCreatedBy(currentUser.getFullName());
        plan.setUpdatedBy(currentUser.getFullName());
        SubscriptionPlan savedPlan = planRepository.save(plan);
        return planMapper.toDTO(savedPlan);
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlane(Long planId, SubscriptionPlanDTO planDTO) {
        SubscriptionPlan existingPlan = planRepository.findById(planId)
                .orElseThrow(
                        () -> new RuntimeException("Plan not found!")
                );
        planMapper.updateEntity(existingPlan,planDTO);
        User currentUser = userService.getCurrentUser();
        existingPlan.setUpdatedBy(currentUser.getFullName());
        SubscriptionPlan updatedPlan = planRepository.save(existingPlan);
        return planMapper.toDTO(updatedPlan);
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) {
        SubscriptionPlan existingPlan = planRepository.findById(planId)
                .orElseThrow(
                        () -> new RuntimeException("Plan not found!")
                );
        planRepository.delete(existingPlan);
    }

    @Override
    public List<SubscriptionPlanDTO> getAllSubscriptionPlan() {
        List<SubscriptionPlan> planList = planRepository.findAll();
        return planList.stream().map(
                planMapper::toDTO
        ).collect(Collectors.toList());
    }
}
