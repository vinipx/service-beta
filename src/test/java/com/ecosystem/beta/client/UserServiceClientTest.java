package com.ecosystem.beta.client;

import com.ecosystem.common.dto.UserDto;
import com.ecosystem.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceClientTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private UserServiceClient userServiceClient;

    @BeforeEach
    void setUp() {
        userServiceClient = new UserServiceClient(restClient);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnUserDto_whenGetUserByIdCalledWithValidId() {
        UserDto expectedUser = new UserDto(1L, "john.doe", "john@example.com", "John Doe",
                Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));

        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object[].class))).thenReturn((RestClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(UserDto.class)).thenReturn(expectedUser);

        UserDto result = userServiceClient.getUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.username()).isEqualTo("john.doe");
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldThrowNotFoundException_whenGetUserByIdCalledWithInvalidId() {
        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object[].class))).thenReturn((RestClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(UserDto.class)).thenThrow(HttpClientErrorException.NotFound.class);

        assertThatThrownBy(() -> userServiceClient.getUserById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }
}