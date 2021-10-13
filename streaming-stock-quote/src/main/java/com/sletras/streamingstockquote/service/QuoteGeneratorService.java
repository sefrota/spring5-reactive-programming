package com.sletras.streamingstockquote.service;

import com.sletras.streamingstockquote.model.Quote;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author sergioletras
 */
public interface QuoteGeneratorService {

    Flux<Quote> fetchQuoteStream(Duration period);
}
