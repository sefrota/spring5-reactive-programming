package com.sletras.mongostockquoteservice.service;

import com.sletras.mongostockquoteservice.domain.QuoteHistory;
import com.sletras.mongostockquoteservice.model.Quote;
import com.sletras.mongostockquoteservice.repositories.QuoteHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author sergioletras
 */
@Service
@RequiredArgsConstructor
public class QuoteHistoryServiceImpl implements QuoteHistoryService {

    private final QuoteHistoryRepository repository;

    @Override
    public Mono<QuoteHistory> saveQuoteToMongo(Quote quote) {
        return repository.save(QuoteHistory.builder()
                        .price(quote.getPrice())
                        .ticker(quote.getTicker())
                        .instant(quote.getInstant())
                        .build());
    }
}
