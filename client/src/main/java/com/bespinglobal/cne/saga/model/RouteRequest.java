/**
 * Project: Appbricks
 *
 * Copyright (c) 2021 CNE
 *
 * This software is saga manager
 */
package com.bespinglobal.cne.saga.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.Map;

/**
 * <pre>
 * service           : Runner
 * package           : com.bespinglobal.cne.saga.model
 * class             : RouteRequest
 * description       : A message to notify Route request
 *
 * ====================================================================================
 *
 * </pre>
 * @date 2021/07/12
 * @author hyeongjunkim
 * @version 1.0.0
 **/
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RouteRequest {

    /**
     * Route ID
     */
    private String routeId;

    /**
     * Parameter information transfer to the service
     * ex) It is used by passing the result value of the previous service
     */
    private RoutePayload routePayload;

}
