package com.example.wiremock.service.execution;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WiremockServiceExecutionTest {

    @Mock
    private WireMockServer wireMockServer;

    @InjectMocks
    private WiremockServiceExecution wiremockServiceExecution;

    @BeforeEach
    void setUp() {
        // No-op, handled by MockitoExtension
    }

    @Test
    void testInit_shouldStartWireMockServer() {
        wiremockServiceExecution.init();
        verify(wireMockServer).start();
    }

    @Test
    void testRestartServer_shouldResetAll() {
        wiremockServiceExecution.restartServer();
        verify(wireMockServer).resetAll();
    }

    @Test
    void testShutdown_shouldStopWireMockServer() {
        wiremockServiceExecution.shutdown();
        verify(wireMockServer).stop();
    }
}