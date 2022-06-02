package com.msamaker.demo.config.kafka;

import java.lang.Object;
import java.lang.String;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

public abstract class KafkaCustomListener {
  @Autowired
  private KafkaProperties kafkaProperties;

  protected MethodKafkaListenerEndpoint<String, Object> createMethodListenerEndpoint(String topic) {
      MethodKafkaListenerEndpoint<String, Object> kafkaListenerEndpoint = new MethodKafkaListenerEndpoint<>();
      kafkaListenerEndpoint.setId(topic);
      kafkaListenerEndpoint.setGroupId(kafkaProperties.getConsumer().getGroupId());
      kafkaListenerEndpoint.setAutoStartup(true);
      kafkaListenerEndpoint.setTopics(topic);
      kafkaListenerEndpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
      return kafkaListenerEndpoint;
  }

  public abstract KafkaListenerEndpoint createListenerEndpoint(String topic);
}
