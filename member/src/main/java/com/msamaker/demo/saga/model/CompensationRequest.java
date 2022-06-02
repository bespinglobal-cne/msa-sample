package com.msamaker.demo.saga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(
    ignoreUnknown = true
)
@Getter
@Setter
@ToString
public class CompensationRequest {
  @JsonProperty("tx_id")
  private String txId;

  @JsonProperty("route_payload")
  private RoutePayload routePayload;

  private int index;
}
