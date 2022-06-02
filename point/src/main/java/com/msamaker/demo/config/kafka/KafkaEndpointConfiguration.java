package com.msamaker.demo.config.kafka;

import com.msamaker.demo.config.AppProperties;
import com.msamaker.demo.config.MessageHandler;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListener;

@Configuration
public class KafkaEndpointConfiguration extends KafkaCustomListener {
  private final MessageHandler messageHandler;

  private final AppProperties appProperties;

  public KafkaEndpointConfiguration(MessageHandler messageHandler, AppProperties appProperties) {
    this.messageHandler = messageHandler;
    this.appProperties = appProperties;
  }

  @Override
  public KafkaListenerEndpoint createListenerEndpoint(String topic) {
            MethodKafkaListenerEndpoint<String, Object> kafkaListenerEndpoint = createMethodListenerEndpoint(topic);
            kafkaListenerEndpoint.setBean(new CustomMessageListener(messageHandler, appProperties));
            try {
                kafkaListenerEndpoint.setMethod(CustomMessageListener.class.getMethod("onMessage", ConsumerRecord.class));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return kafkaListenerEndpoint;
  }

  private static class CustomMessageListener implements MessageListener<String, Object> {
    private final MessageHandler messageHandler;

    private final AppProperties appProperties;

    private final Logger log = LoggerFactory.getLogger(CustomMessageListener.class);

    public CustomMessageListener(MessageHandler messageHandler, AppProperties appProperties) {
      this.messageHandler = messageHandler;
      this.appProperties = appProperties;
    }

    @Override
    public void onMessage(ConsumerRecord message) {
       if (!message.key().equals(appProperties.getService().getName())) return;
             log.info("Received ConsumerRecord Message : topic=[" + message.topic() + "], key=[" + message.key() + "], message=[" + message.value() + "] with offset=[" + message.offset() + "]");
       if (message.topic().contains("_route")) {
             messageHandler.handleRoute(message);
       } else if (message.topic().contains("_compensation")) {
             messageHandler.handleCompensation(message);
       }
    }
  }
}
