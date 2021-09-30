package com.sletras.reactiveexamples;

import com.sletras.reactiveexamples.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sergioletras
 */
class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;

    @BeforeEach
    void setup(){
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock(){
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();

        System.out.println(person.toString());;
    }

    @Test
    void getByIdSubscribe(){
        Mono<Person> personMono = personRepository.getById(1);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void getByIdMapFunction(){
        Mono<Person> personMono = personRepository.getById(1);


        personMono.map(person -> {
            System.out.println(person.toString());
            return person.getFirstName();
        }).subscribe(System.out::println);
    }

    @Test
    void testFluxBlockFirst(){
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst();
        System.out.println(person.toString());
    }

    @Test
    void testFluxSubscribe(){
        Flux<Person> personFlux = personRepository.findAll();

        StepVerifier.create(personFlux).expectNextCount(4).verifyComplete();

        personFlux
                .subscribe(System.out::println);
    }

    @Test
    void testFluxToListMono(){
        Flux<Person> personFlux = personRepository.findAll();

        Mono<List<Person>> listMono = personFlux.collect(Collectors.toList());

        listMono
                .subscribe(list -> list.forEach(System.out::println));
    }

    @Test
    void testFindPersonById(){
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 3;

        Mono<Person> personMono = personFlux.filter(person -> person.getId().equals(id)).next();

        personMono
                .subscribe(System.out::println);
    }

    @Test
    void testFindPersonByIdNotFound(){
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId().equals(id)).next();

        personMono
                .subscribe(System.out::println);
    }

    @Test
    void testFindPersonByIdNotFoundWithException(){
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId().equals(id)).single();

        personMono
                .doOnError(throwable -> System.out.println(throwable.getMessage()))
                .onErrorReturn(Person.builder().build())
                .subscribe(System.out::println);
    }

    @Test
    void testGetById(){
        Mono<Person> personMono = personRepository.getById(1);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono
                .subscribe(System.out::println);
    }

    @Test
    void testGetByIdNotFound(){
        Mono<Person> personMono = personRepository.getById(5);

        StepVerifier.create(personMono).verifyComplete();
        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

        personMono
                .subscribe(System.out::println);
    }
}