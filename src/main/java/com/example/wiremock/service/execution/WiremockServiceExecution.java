package com.example.wiremock.service.execution;


import com.github.tomakehurst.wiremock.WireMockServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "wiremock", name = "enabled", havingValue = "true", matchIfMissing = false)
public class WiremockServiceExecution {

    private final WireMockServer wireMockServer;

    public WiremockServiceExecution(@Qualifier("wireMockServer") WireMockServer wireMockServer) {
        this.wireMockServer = wireMockServer;
    }

    @PostConstruct
    void init() {
        wireMockServer.start();
        log.info("WireMock server started");
    }

    public void restartServer() {
        wireMockServer.resetAll();
        log.info("WireMock server re-started");
    }



    @PreDestroy
    void shutdown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            log.info("WireMock server stopped");
        }
    }
}
