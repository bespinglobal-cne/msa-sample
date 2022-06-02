package com.msamaker.demo.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msamaker.demo.domain.Account;
import com.msamaker.demo.domain.AccountTransaction;
import com.msamaker.demo.exception.OutOfBalanceException;
import com.msamaker.demo.saga.model.CompensationRequest;
import com.msamaker.demo.saga.model.CompensationResult;
import com.msamaker.demo.saga.model.ResultMessage;
import com.msamaker.demo.saga.model.RouteMessage;
import com.msamaker.demo.service.AccountService;
import com.msamaker.demo.service.MemberService;
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
                          ObjectMapper objectMapper, MemberService memberService, AccountService accountService) {
        this.appProperties = appProperties;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.memberService = memberService;
        this.accountService = accountService;
    }

    private final MemberService memberService;
    private final AccountService accountService;

    public void handleRoute(ConsumerRecord message) {
        // 정상 라우팅 요청
        toData(message.value().toString(), RouteMessage.class).ifPresent(routeMessage ->  {

            AppProperties.Route route = getRoute(message.topic());
            if (route.getIndex() != routeMessage.getIndex()) return;

            ResultMessage resultMessage = ResultMessage.builder()
                    .txId(routeMessage.getTxId())
                    .index(route.getIndex())
                    .build() ;

            routeMessage.getRoutePayloads().forEach(routePayload -> {
                // 기존 routePayload 설정
                resultMessage.setRoutePayload(routePayload);

                // routing 룰에 따른 member 서비스의 비즈니스 로직 수행
                long money = Long.parseLong(routePayload.getPayload().get("money").toString());
                log.info("update account's information : buy product for {}", money);

                AccountTransaction transaction = AccountTransaction.builder()
                        .amount(money*(-1))
                        .notes("상품 구매")
                        .build();
                try {
                    Account result = accountService.charge("demo_kim", transaction);

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
            long money = Long.parseLong(compensation.getRoutePayload().getPayload().get("money").toString());
            log.info("compensate account's information : pay back {}", money);

            AccountTransaction transaction = AccountTransaction.builder()
                    .amount(money)
                    .notes("구매 실패 금액 계좌에 복구")
                    .build();
            Account result = accountService.charge("demo_kim", transaction);
            if(result != null)
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
