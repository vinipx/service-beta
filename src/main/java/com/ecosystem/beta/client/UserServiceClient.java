package com.ecosystem.beta.client;

import com.ecosystem.common.dto.UserDto;
import com.ecosystem.common.exception.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class UserServiceClient {

    private final RestClient restClient;

    public UserServiceClient(RestClient serviceAlphaRestClient) {
        this.restClient = serviceAlphaRestClient;
    }

    public UserDto getUserById(Long userId) {
        try {
            return restClient.get()
                    .uri("/api/v1/users/{id}", userId)
                    .retrieve()
                    .body(UserDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("User not found with id: " + userId);
        }
    }
}
