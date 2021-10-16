package com.sletras.rabbitstockquoteservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sletras.rabbitstockquoteservice.config.RabbitConfig;
import com.sletras.rabbitstockquoteservice.model.Quote;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.OutboundMessageResult;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Sender;

/**
 * @author sergioletras
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QuoteMessageSender {

    private final ObjectMapper objectMapper;
    private final Sender sender;

    @SneakyThrows
    public Mono<Void> sendQuoteMessage(Quote quote) {
        byte[] jsonBytes = objectMapper.writeValueAsBytes(quote);

        Flux<OutboundMessageResult> confirmations = sender.sendWithPublishConfirms(Flux.just(new OutboundMessage("", RabbitConfig.QUEUE, jsonBytes)));

        sender.declareQueue(QueueSpecification.queue(RabbitConfig.QUEUE))
                .thenMany(confirmations)
                .doOnError(e -> log.error("Send failed", e))
                .subscribe(r -> {
                    if (r.isAck()) {
                        log.info("Message sent successfully {}", new String(r.getOutboundMessage().getBody()));
                    }
                });

        return Mono.empty();
    }
}
