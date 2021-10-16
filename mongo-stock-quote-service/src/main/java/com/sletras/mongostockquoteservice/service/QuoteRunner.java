package com.sletras.mongostockquoteservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author sergioletras
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QuoteRunner implements CommandLineRunner {

    private final QuoteGeneratorService quoteGeneratorService;
    private final QuoteHistoryService quoteHistoryService;

    @Override
    public void run(String... args) throws Exception {
        quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100))
                .take(50)
                .log("Got Quote:")
                .flatMap(quoteHistoryService::saveQuoteToMongo)
                .subscribe(savedQuote -> {
                    log.debug("Saved Quote: {}", savedQuote);
                }, throwable -> {
                    log.error("Some error occurred: {}", throwable.getMessage());
                }, () -> {
                    log.debug("All done!!");
                });
    }
}
