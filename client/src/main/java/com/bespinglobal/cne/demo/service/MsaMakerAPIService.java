package com.bespinglobal.cne.demo.service;

import com.bespinglobal.cne.saga.model.RouteRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
public class MsaMakerAPIService {
    private final WebClient webClient;

    @Value("${msamaker.runner.url}")
    private String runnerAPI;

    public MsaMakerAPIService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * MSA Maker Runner API 로 Route 정보 생성 요청.
     * @param routeRequest
     * @return Mono<Map>
     */
    public Map callRunnerAPI(RouteRequest routeRequest) {
        log.info("[callRunnerAPI] {}", routeRequest.toString());
        return webClient.post()
                .uri(runnerAPI + "/runner/routeRequest")
                .body(Mono.just(routeRequest), RouteRequest.class)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

}
