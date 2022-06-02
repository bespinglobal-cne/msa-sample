package com.msamaker.demo.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msamaker.demo.domain.MemberPoint;
import com.msamaker.demo.domain.PointTransaction;
import com.msamaker.demo.exception.OutOfBalanceException;
import com.msamaker.demo.saga.model.CompensationRequest;
import com.msamaker.demo.saga.model.CompensationResult;
import com.msamaker.demo.saga.model.ResultMessage;
import com.msamaker.demo.saga.model.RouteMessage;
import com.msamaker.demo.service.PointService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;
import java.util.Optional;

@Component
public class MessageHandler {
    private final AppProperties appProperties;

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    public MessageHandler(AppProperties appProperties, KafkaTemplate<String, Object> kafkaTemplate,
            ObjectMapper objectMapper, PointService pointService) {
        this.appProperties = appProperties;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.pointService = pointService;
    }

    private final PointService pointService;

    public void handleRoute(ConsumerRecord message) {
        // 정상 라우팅 요청
        toData(message.value().toString(), RouteMessage.class).ifPresent(routeMessage ->  {

            AppProperties.Route route = getRoute(message.topic());
            if (route.getIndex() != routeMessage.getIndex()) return;

            ResultMessage resultMessage = ResultMessage.builder()
                    .txId(routeMessage.getTxId())
                    .index(route.getIndex())
                    .build();

            routeMessage.getRoutePayloads().forEach(routePayload -> {
                // 기존 routePayload 설정
                resultMessage.setRoutePayload(routePayload);

                // routing 룰에 따른 point 서비스의 비즈니스 로직 수행
                long point = Long.parseLong(routePayload.getPayload().get("point").toString());
                log.info("update point's information : buy product for {} point", point);

                PointTransaction transaction = PointTransaction.builder()
                        .amount(point*(-1))
                        .notes("상품 구매")
                        .build();
                try {
                    MemberPoint result = pointService.charge("demo_kim", transaction);

                    if (result.getBalance() < 0)
                        throw new OutOfBalanceException();

                    resultMessage.setResult(true);
                } catch (OutOfBalanceException e) {
                    resultMessage.setResult(false);
                }

                // 다음 라우팅에 필요한 데이터를 routePayload에 추가
//                resultMessage.getRoutePayload().getPayload().put("KEY", "VALUE");

            });
            // 라우팅 결과 반영
            log.info("result is {}", resultMessage.isResult());
            send(route.getId() + "_result", resultMessage);
        });
    }

    public void handleCompensation(ConsumerRecord message) {
        // 라우팅 보상처리 요청
        toData(message.value().toString(), CompensationRequest.class).ifPresent(compensation -> {

            if (getRoute(message.topic()).getIndex() != compensation.getIndex()) return;

            CompensationResult compensationResult = CompensationResult.builder()
                    .service(appProperties.getService().getName())
                    .txId(compensation.getTxId())
                    .index(compensation.getIndex())
                    .build();

            // routing 룰에 따른 member 서비스의 보상처리 비즈니스 로직 수행
            long point = Long.parseLong(compensation.getRoutePayload().getPayload().get("point").toString());
            log.info("compensate point's information : pay back {} point", point);

            PointTransaction transaction = PointTransaction.builder()
                    .amount(point)
                    .notes("구매 실패 금액 포인트에 복구")
                    .build();
            MemberPoint result = pointService.charge("demo_kim", transaction);
            if (result != null)
                compensationResult.setResult(true);

            // 라우팅 결과 반영
            send("compensation-result", compensationResult);
        });
      }

      private AppProperties.Route getRoute(String topicName) {
        return appProperties.getRoutes().stream()
                        .filter(e -> topicName.startsWith(e.getId()))
                        .findFirst()
                        .orElse(new AppProperties.Route());
      }

      private <T> Optional<T> toData(String message, Class<T> cls) {
        try {
          return Optional.ofNullable(objectMapper.readValue(message, cls));
        } catch (JsonProcessingException e) {
            log.error("Unable to received message=[" + message + "] due to : " + e.getMessage()); 
                      return Optional.empty();
           }
        }

        private <T> Optional<T> toDataMap(Map<String, Object> payload, Class<T> cls) {
          {
                  try {
                      String payloadAsString = objectMapper.writeValueAsString(payload.get(cls.getSimpleName()));
                      return toData(payloadAsString, cls);
                  } catch (JsonProcessingException e) {
                      log.error("Unable to received payload=[" + payload + "] due to : " + e.getMessage());
                      return Optional.empty();
                  }
              }
        }

        public <T> void send(String topic, T data) {
          ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, data);
          future.addCallback(new ListenableFutureCallback<>() {
                        @Override
                        public void onSuccess(SendResult<String, Object> result) {
                            log.info("Sent topic=[" + topic + "], message=[" + data + "] with offset=[" + result.getRecordMetadata().offset() + "]");
                        }

                        @Override
                        public void onFailure(Throwable ex) {
                            log.info("Unable to send message=[" + data + "] due to : " + ex.getMessage());
                        }
                    });
          }
        }
