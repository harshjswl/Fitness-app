package com.fitness.activity_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final WebClient.Builder webClientBuilder;

    public boolean validateUser(String userId) {
        try {
            Boolean response = webClientBuilder
                    .baseUrl("http://user-service")
                    .build()
                    .get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .onErrorResume(WebClientResponseException.class, e -> {
                        System.err.println("Error validating user: " + e.getStatusCode());
                        return Mono.just(false);
                    })
                    .block();

            return response != null && response;

        } catch (Exception e) {
            System.err.println("User validation request failed: " + e.getMessage());
            return false;
        }
    }
}
