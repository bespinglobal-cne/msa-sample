package com.msamaker.demo.saga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResultMessage {
  private String txId;

  private int index;

  private boolean result;

  private RoutePayload routePayload;
}
