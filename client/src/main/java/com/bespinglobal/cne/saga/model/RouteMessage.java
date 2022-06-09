package com.bespinglobal.cne.saga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RouteMessage {
    /**
     * Transaction ID
     */
    private String txId;

    /**
     * The topic name to which the service sends the processing result
     */
    private String topicResult;

    /**
     * The topic name the service receives for compensation processing
     */
    private String topicCompensation;

    /**
     * Index information of the service to be started on this route
     */
    private int index;

    /**
     * Parameter information transfer to the service
     * ex) It is used by passing the result value of the previous service
     */
    private List<RoutePayload> routePayloads;
}
