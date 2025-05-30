package com.example.wiremock.service.config;

// src/test/java/com/example/wiremock/service/config/WiremockConfigTest.java

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WiremockConfigTest {

    @InjectMocks
    private WiremockConfig wiremockConfig;

    @Test
    void init_shouldReturnWireMockServerWithConfiguredProperties() {
        ReflectionTestUtils.setField(wiremockConfig, "port", 8080);
        ReflectionTestUtils.setField(wiremockConfig, "httpsPort", 8443);
        ReflectionTestUtils.setField(wiremockConfig, "host", "localhost");
        ReflectionTestUtils.setField(wiremockConfig, "filesRoot", "src\\test\\resources");

        WireMockServer server = wiremockConfig.init();

        assertThat(server).isNotNull();
        assertThat(server.getOptions().portNumber()).isEqualTo(8080);
        assertThat(server.getOptions().httpsSettings().port()).isEqualTo(8443);
        assertThat(server.getOptions().bindAddress()).isEqualTo("localhost");
        assertThat(server.getOptions().filesRoot().getPath()).endsWith("src\\test\\resources");
    }
}
