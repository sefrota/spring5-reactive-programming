package com.sletras.netflux.bootstrap;

import com.sletras.netflux.domain.Movie;
import com.sletras.netflux.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author sergioletras
 */
@Component
@RequiredArgsConstructor
public class InitMovies implements CommandLineRunner {

    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) {
        movieRepository
                .deleteAll()
                .thenMany(
                        Flux.just(
                                        "Silence of the Lambdas",
                                        "AEon Flux",
                                        "Enter the Mono<Void>",
                                        "The Fluxinator",
                                        "Back to the Future",
                                        "Meet the Fluxxes",
                                        "Lord of the Fluxxes"
                                )
                                .map(Movie::new)
                                .flatMap(movieRepository::save))
                .subscribe(null, null, () -> {
                    movieRepository.findAll().subscribe(System.out::println);
                });
    }
}
