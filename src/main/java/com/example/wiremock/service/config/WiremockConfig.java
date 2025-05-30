package com.example.wiremock.service.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "wiremock", name = "enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class WiremockConfig {

    @Value("${wiremock.server.port}")
    private int port;

    @Value("${wiremock.server.https-port}")
    private int httpsPort;

    @Value("${wiremock.server.host}")
    private String host;

    @Value("${wiremock.server.files-root}")
    private String filesRoot;

    @Bean(name = "wireMockServer")
    public WireMockServer init() {
        return new WireMockServer(
                WireMockConfiguration.options()
                        .bindAddress(host)
                        .port(port)
                        .httpsPort(httpsPort)
                        .withRootDirectory(filesRoot)
                        .stubCorsEnabled(true)
                        .asynchronousResponseEnabled(true)
                        .asynchronousResponseThreads(2000)
                        .maxHttpClientConnections(2000)
                        .containerThreads(2000)
        );

    }


}
