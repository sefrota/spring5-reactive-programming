package com.sletras.netflux.services;

import com.sletras.netflux.domain.Movie;
import com.sletras.netflux.domain.MovieEvent;
import com.sletras.netflux.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author sergioletras
 */
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public Mono<Movie> getMovieById(String id) {
        return movieRepository.findById(id);
    }

    @Override
    public Flux<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    //This will emit a movie event every second
    @Override
    public Flux<MovieEvent> streamMovieEvents(String id) {
        return Flux.<MovieEvent>generate(movieEventSynchronousSink -> {
           movieEventSynchronousSink.next(new MovieEvent(id, LocalDateTime.now()));
        }).delayElements(Duration.ofSeconds(1));
    }
}
