package com.inkwell.post.config;

import static org.assertj.core.api.Assertions.assertThat;

import feign.RequestTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class FeignConfigTest {

    private final FeignConfig feignConfig = new FeignConfig();

    @AfterEach
    void resetRequestAttributes() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void forwardsAuthorizationAndGatewayHeadersWhenRequestExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        request.addHeader("X-auth-user", "author");
        request.addHeader("X-auth-role", "AUTHOR");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        RequestTemplate template = new RequestTemplate();

        // Act
        feignConfig.apply(template);

        // Assert
        assertThat(template.headers().get("Authorization")).containsExactly("Bearer token");
        assertThat(template.headers().get("X-auth-user")).containsExactly("author");
        assertThat(template.headers().get("X-auth-role")).containsExactly("AUTHOR");
    }

    @Test
    void doesNothingWhenNoServletRequestIsBound() {
        // Arrange
        RequestTemplate template = new RequestTemplate();

        // Act
        feignConfig.apply(template);

        // Assert
        assertThat(template.headers()).isEmpty();
    }
}
