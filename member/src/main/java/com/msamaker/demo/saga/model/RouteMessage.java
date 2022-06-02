package com.msamaker.demo.saga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.String;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(
    ignoreUnknown = true
)
@Getter
@Setter
@ToString
public class RouteMessage {
  @JsonProperty("tx_id")
  private String txId;

  @JsonProperty("topic_result")
  private String topic_result;

  @JsonProperty("topic_compensation")
  private String topicCompensation;

  @JsonProperty("route_payloads")
  private List<RoutePayload> routePayloads;

  private int index;
}
