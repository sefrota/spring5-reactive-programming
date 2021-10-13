package com.sletras.streamingstockquote.service;

import com.sletras.streamingstockquote.model.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author sergioletras
 */
class QuoteGeneratorServiceImplTest {

    QuoteGeneratorService quoteGeneratorService;

    @BeforeEach
    void setup() {
        quoteGeneratorService = new QuoteGeneratorServiceImpl();
    }

    @Test
    void fetchQuoteStream() throws InterruptedException {
        Flux<Quote> quoteFlux = quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100l));

        Consumer<Quote> quoteConsumer = System.out::println;

        Consumer<Throwable> throwableConsumer = e -> System.out.println(e.getMessage());

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Runnable done = () -> countDownLatch.countDown();

        quoteFlux.take(30)
                .subscribe(quoteConsumer, throwableConsumer, done);

        countDownLatch.await();
    }
}