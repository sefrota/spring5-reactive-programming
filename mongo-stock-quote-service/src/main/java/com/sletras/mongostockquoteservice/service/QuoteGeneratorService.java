package com.sletras.mongostockquoteservice.service;

import com.sletras.mongostockquoteservice.model.Quote;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author sergioletras
 */
public interface QuoteGeneratorService {

    Flux<Quote> fetchQuoteStream(Duration period);
}
