package com.msamaker.demo.saga.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Map;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

public class ObjectSerializer implements Serializer {
  @Override
  public void configure(Map configs, boolean isKey) {
  }

  @Override
  public byte[] serialize(String s, Object o) {
    ObjectMapper objectMapper = new ObjectMapper();
     byte[] retVal = null;

        try {
          retVal = objectMapper.writeValueAsString(o).getBytes();
        } catch (Exception e) {
          e.printStackTrace();
        }

        return retVal;
  }

  @Override
  public void close() {
  }

  @Override
  public byte[] serialize(String topic, Headers headers, Object data) {
     return this.serialize(topic, data);
  }
}
