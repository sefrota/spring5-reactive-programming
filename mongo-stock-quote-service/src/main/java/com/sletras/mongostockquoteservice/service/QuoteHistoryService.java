package com.sletras.mongostockquoteservice.service;

import com.sletras.mongostockquoteservice.domain.QuoteHistory;
import com.sletras.mongostockquoteservice.model.Quote;
import reactor.core.publisher.Mono;

/**
 * @author sergioletras
 */
public interface QuoteHistoryService {

    Mono<QuoteHistory> saveQuoteToMongo(Quote quote);
}
