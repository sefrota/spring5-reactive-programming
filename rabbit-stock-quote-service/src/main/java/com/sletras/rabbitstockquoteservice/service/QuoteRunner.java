package com.sletras.rabbitstockquoteservice.service;

import com.rabbitmq.client.Delivery;
import com.sletras.rabbitstockquoteservice.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.rabbitmq.Receiver;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sergioletras
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QuoteRunner implements CommandLineRunner {

    private final QuoteMessageSender quoteMessageSender;
    private final Receiver receiver;
    private final QuoteGeneratorService quoteGeneratorService;

    @Override
    public void run(String... args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(25);
        quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100))
                .take(25)
                .log("Got quote")
                .flatMap(quoteMessageSender::sendQuoteMessage)
                .subscribe(result -> {
                    log.debug("Sent message to Rabbit");
                    countDownLatch.countDown();
                }, throwable -> {
                    log.error("Got an error: {}", throwable.getMessage());
                }, () -> {
                    log.debug("All done!");
                });

        countDownLatch.await(1, TimeUnit.SECONDS);

        AtomicInteger receiverCount = new AtomicInteger();

        receiver.consumeAutoAck(RabbitConfig.QUEUE)
                .log("Msg Receiver")
                .subscribe(msg -> {
                    log.debug("Received message # {} - {}", receiverCount.incrementAndGet(), new String(msg.getBody()));
                }, throwable -> {
                    log.debug("Error receiving", throwable);
                }, () -> {
                    log.debug("Complete");
                });
    }
}
