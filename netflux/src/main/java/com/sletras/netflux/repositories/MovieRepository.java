package com.sletras.netflux.repositories;

import com.sletras.netflux.domain.Movie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author sergioletras
 */
public interface MovieRepository extends ReactiveMongoRepository<Movie, String> {
}
