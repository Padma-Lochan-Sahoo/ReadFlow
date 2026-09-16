package com.library.ReadFlow.repositories;

import com.library.ReadFlow.entites.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
