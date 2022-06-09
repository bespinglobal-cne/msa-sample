package com.bespinglobal.cne.saga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CompensationResult {
    //Transaction ID
    private String txId;
    // Index information of the service to be started on this route
    private int index;
    // Service Name
    private String service;
    // result
    private boolean result;
}
