package com.bespinglobal.cne.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class DemoAPIService {
    private final WebClient webClient;

    @Value("${msamaker.services[0].url}")
    private String membedrAPI;

    @Value("${msamaker.services[1].url}")
    private String pointAPI;

    public DemoAPIService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * MSA Maker Runner API 로 Route 정보 생성 요청.
     * @param parameter
     * @return Mono<Map>
     */
    public String callDemoAccountAPI(String parameter) {
        log.info("[callDemoAccountAPI] {}", parameter);
        return webClient.get()
                .uri(membedrAPI + "/member/api/account/" + parameter)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String callDemoPointAPI(String parameter) {
        log.info("[callDemoAccountAPI] {}", parameter);
        return webClient.get()
                .uri(pointAPI + "/point/api/" + parameter)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


}
