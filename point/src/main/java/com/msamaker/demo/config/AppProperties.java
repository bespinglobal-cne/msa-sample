package com.msamaker.demo.config;

import java.lang.Integer;
import java.lang.String;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
    prefix = "application",
    ignoreUnknownFields = false
)
@Getter
@Setter
@ToString
public class AppProperties {
  List<Route> routes;

  List<Prefix> prefixes;

  public Service service;

  @Getter
  @Setter
  @ToString
  public static class Route {
    Integer index;

    String id;
  }

  @Getter
  @Setter
  @ToString
  public static class Prefix {
    String name;
  }

  @Getter
  @Setter
  @ToString
  public static class Service {
    String name;
  }
}
