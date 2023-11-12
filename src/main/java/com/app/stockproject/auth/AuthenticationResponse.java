package com.app.stockproject.auth;

import lombok.*;
import org.springframework.context.annotation.Bean;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
}

