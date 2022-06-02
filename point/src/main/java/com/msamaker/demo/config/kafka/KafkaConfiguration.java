package com.msamaker.demo.config.kafka;

import com.msamaker.demo.config.AppProperties;
import com.msamaker.demo.config.AppProperties.Route;
import java.lang.Override;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

@Configuration
public class KafkaConfiguration implements InitializingBean {
  private final AppProperties appProperties;

  private final BeanFactory beanFactory;

  private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  private final KafkaListenerContainerFactory kafkaListenerContainerFactory;

  public KafkaConfiguration(AppProperties appProperties, BeanFactory beanFactory,
      KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
      KafkaListenerContainerFactory kafkaListenerContainerFactory) {
    this.appProperties = appProperties;
    this.beanFactory = beanFactory;
    this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    this.kafkaListenerContainerFactory = kafkaListenerContainerFactory;
  }

  @Override
  public void afterPropertiesSet() {
    appProperties.getRoutes().forEach(this::registryKafkaListener);
  }

  public void registryKafkaListener(AppProperties.Route route) {
            appProperties.getPrefixes().forEach(prefix -> {
                KafkaCustomListener kafkaListener = beanFactory.getBean(KafkaEndpointConfiguration.class);
                kafkaListenerEndpointRegistry.registerListenerContainer(
                    kafkaListener.createListenerEndpoint(route.getId() + prefix.getName()), kafkaListenerContainerFactory, false
                );
            });
  }
}
