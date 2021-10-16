package com.sletras.mongostockquoteservice.repositories;

import com.sletras.mongostockquoteservice.domain.QuoteHistory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author sergioletras
 */
public interface QuoteHistoryRepository extends ReactiveMongoRepository<QuoteHistory, String> {
}
