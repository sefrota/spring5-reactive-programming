package com.sletras.rabbitstockquoteservice.service;


import com.sletras.rabbitstockquoteservice.model.Quote;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author sergioletras
 */
public interface QuoteGeneratorService {

    Flux<Quote> fetchQuoteStream(Duration period);
}
