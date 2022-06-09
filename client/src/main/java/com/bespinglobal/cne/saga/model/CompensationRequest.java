package com.bespinglobal.cne.saga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CompensationRequest {

    /**
     * Transaction ID
     */
    private String txId;

    /**
     * Index information of the service to be started on this route
     */
    private int index;

    /**
     * Parameter information transfer to the service
     * ex) It is used by passing the result value of the previous service
     */
    private RoutePayload routePayload;

}
