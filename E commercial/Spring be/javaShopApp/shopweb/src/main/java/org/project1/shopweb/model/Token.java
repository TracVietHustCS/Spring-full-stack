package org.project1.shopweb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tokens")
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", length = 255)
    private String token;

    @Column(name = "token_type", length = 50)
    private String tokenType;

    @Column(name = "refresh_token", length = 255)
    private String refreshToken;

    @Column(name = "refresh_expiration_date")
    private LocalDateTime refreshExpirationDate;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    private boolean revoked; // thu hoi
    private boolean expired;

    @Column(name = "is_mobile", columnDefinition = "TINYINT(1)")
    private boolean isMobile;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;



}
