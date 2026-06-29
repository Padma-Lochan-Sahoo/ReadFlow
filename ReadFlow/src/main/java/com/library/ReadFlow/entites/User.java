package com.library.ReadFlow.entites;

import com.library.ReadFlow.domain.AuthProvider;
import com.library.ReadFlow.domain.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String email;
    private String fullName;

    private UserRole role;
    private String phone;

    private AuthProvider authProvider = AuthProvider.LOCAL;
    private String googleId;
    private String profileImage;
    private String password;
    private LocalDateTime lastLogin;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
