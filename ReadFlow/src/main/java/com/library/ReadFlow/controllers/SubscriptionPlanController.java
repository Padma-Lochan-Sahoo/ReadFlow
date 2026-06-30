package com.library.ReadFlow.controllers;

import com.library.ReadFlow.payload.dtos.SubscriptionPlanDTO;
import com.library.ReadFlow.payload.response.ApiResponse;
import com.library.ReadFlow.services.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping
    public ResponseEntity<?> getAllSubscriptionPlans(){
        List<SubscriptionPlanDTO> plans = subscriptionPlanService.getAllSubscriptionPlan();
        return ResponseEntity.ok(plans);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createSubscriptionPlan(
           @Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO
    ){
        SubscriptionPlanDTO plan = subscriptionPlanService.createSubscriptionPlan(subscriptionPlanDTO);
        return ResponseEntity.ok(plan);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateSubscriptionPlan(
            @PathVariable long id,
            @RequestBody SubscriptionPlanDTO subscriptionPlanDTO
    ){
        SubscriptionPlanDTO plan = subscriptionPlanService.updateSubscriptionPlane(id, subscriptionPlanDTO);
        return ResponseEntity.ok(plan);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse> deleteSubscriptionPlan(
            @PathVariable long id
    ){
        subscriptionPlanService.deleteSubscriptionPlan(id);
        ApiResponse response = new ApiResponse("Subscription Deleted Successfully",true);
        return ResponseEntity.ok(response);
    }


}
