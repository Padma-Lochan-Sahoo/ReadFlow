package com.library.ReadFlow.controllers;

import com.library.ReadFlow.exceptions.SubscriptionException;
import com.library.ReadFlow.payload.dtos.SubscriptionDTO;
import com.library.ReadFlow.payload.response.ApiResponse;
import com.library.ReadFlow.services.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionDTO> subscribe(
            @RequestBody SubscriptionDTO subscriptionDTO
            ){
        SubscriptionDTO dto = subscriptionService.subscribe(subscriptionDTO);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/active")
    public ResponseEntity<SubscriptionDTO> getUserActivaSubscription(
            @RequestParam(required=false) Long userId
    ){
        SubscriptionDTO dto = subscriptionService.getUsersActiveSubscription(userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions(){
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        List<SubscriptionDTO> dtoList = subscriptionService.getAllSubscriptions(pageable);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/admin/deactivate-expired")
    public ResponseEntity<ApiResponse> deactivateExpiredSubscriptions(){
        subscriptionService.deactivateExpiredSubscriptions();
        ApiResponse response = new ApiResponse("Subscription Deactivated Successfully",true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{subscriptionId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SubscriptionDTO> cancelSubscription(
            @PathVariable Long subscriptionId,
            @RequestParam(required = false) String reason
    ){
        SubscriptionDTO subscriptionDTO = subscriptionService.cancelSubscription(subscriptionId,reason);
        return ResponseEntity.ok(subscriptionDTO);
    }

    @PostMapping("/activate")
    public ResponseEntity<SubscriptionDTO> activateSubscription(
            @RequestParam Long subscriptionId,
            @RequestParam Long paymentId
    ){

        SubscriptionDTO dto = subscriptionService.activateSubscription(subscriptionId,paymentId);
        return ResponseEntity.ok(dto);
    }


}
