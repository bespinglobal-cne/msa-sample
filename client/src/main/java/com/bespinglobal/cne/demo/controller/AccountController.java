package com.bespinglobal.cne.demo.controller;

import com.bespinglobal.cne.demo.service.DemoAPIService;
import com.bespinglobal.cne.demo.service.MsaMakerAPIService;
import com.bespinglobal.cne.saga.model.RoutePayload;
import com.bespinglobal.cne.saga.model.RouteRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
public class AccountController {
    @Value("${msamaker.route.id}")
    private String routeId;

    @Autowired
    private MsaMakerAPIService msaMakerAPIService;

    @Autowired
    private DemoAPIService demoAPIService;

    @GetMapping("/")
    public String homePage() {
        log.info("[homePage]===========================call");
        return "demoHome";
    }

    @GetMapping("/demo/money")
    @ResponseBody
    public String getDemoAllAccount(@RequestParam("name")String name) {
        log.info("[getDemoAllAccount]===========================call");
        return this.demoAPIService.callDemoAccountAPI(name);
    }

    @GetMapping("/demo/point")
    @ResponseBody
    public String getDemoPointAccount(@RequestParam("name")String name) {
        log.info("[getDemoPointAccount]===========================call");
        return this.demoAPIService.callDemoPointAPI(name);
    }

    @PostMapping("/demo/purchase")
    @ResponseBody
    public Map postPurchase(@RequestBody Map<String, Object> payload) {
        log.info("[postPurchase]===========================call");
        payload.forEach((key, value)->log.info("key : {}, value : {}", key, value));

        RoutePayload routePayload = RoutePayload.builder()
                .serviceName("member")
                .payload(payload).build();
        RouteRequest routeRequest = RouteRequest.builder()
                .routeId(routeId)
                .routePayload(routePayload)
                .build();
        log.info("[routeRequest] : {}", routeRequest.toString());

        return this.msaMakerAPIService.callRunnerAPI(routeRequest);
    }
}
