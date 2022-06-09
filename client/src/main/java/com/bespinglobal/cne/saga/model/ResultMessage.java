package com.bespinglobal.cne.saga.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResultMessage {

    /**
     * Transaction ID
     */
    private String txId;

    /**
     * Index of Service
     */
    private int index;

    /**
     * processing result : True/False
     */
    private boolean result;

    /**
     * Parameter information of processing result
     */
    private RoutePayload routePayload;
}
