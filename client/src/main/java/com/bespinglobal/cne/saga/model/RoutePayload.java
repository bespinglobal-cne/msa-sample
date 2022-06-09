package com.bespinglobal.cne.saga.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoutePayload {

    /**
     * Specify which services should be started
     */
    private String serviceName;

    /**
     * Map of Service parameters
     */
    private Map<String, Object> payload;
}
