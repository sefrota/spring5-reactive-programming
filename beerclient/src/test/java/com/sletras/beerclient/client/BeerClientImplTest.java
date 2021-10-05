package com.sletras.beerclient.client;

import com.sletras.beerclient.config.WebclientConfig;
import com.sletras.beerclient.model.BeerDto;
import com.sletras.beerclient.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author sergioletras
 */
class BeerClientImplTest {

    BeerClientImpl beerClient;

    @BeforeEach
    public void setup(){
        beerClient = new BeerClientImpl(new WebclientConfig().webClient());
    }

    @Test
    void getBeerByIdShowInventory() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 1, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(1);

        BeerDto beerDto = pagedList.getContent().get(0);

        Mono<BeerDto> beerDtoMono = beerClient.getBeerById(beerDto.getId(), true);

        BeerDto beerDtoById = beerDtoMono.block();

        assertThat(beerDtoById).isNotNull();
        assertThat(beerDtoById.getId()).isEqualTo(beerDto.getId());
        assertThat(beerDtoById.getQuantityOnHand()).isNotNull();
    }

    @Test
    void functionalTestGetBeerById() throws InterruptedException {
        AtomicReference<String> beerName = new AtomicReference<>("");
        CountDownLatch countDownLatch = new CountDownLatch(1);

        beerClient.listBeers(null, null, null, null, null)
                .map(beerPagedList -> beerPagedList.getContent().get(0).getId())
                .map(beerId -> beerClient.getBeerById(beerId, false))
                .flatMap(mono -> mono)
                .subscribe(beerDto -> {
                    beerName.set(beerDto.getBeerName());
                    System.out.println(beerDto);
                    countDownLatch.countDown();
                });

        countDownLatch.await();
        assertThat(beerName.get()).isEqualTo("Mango Bobs");

    }

    @Disabled("Api returning inventory no matter what")
    @Test
    void getBeerByIdDoNotShowInventory() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 1, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(1);

        BeerDto beerDto = pagedList.getContent().get(0);

        Mono<BeerDto> beerDtoMono = beerClient.getBeerById(beerDto.getId(), false);

        BeerDto beerDtoById = beerDtoMono.block();

        assertThat(beerDtoById).isNotNull();
        assertThat(beerDtoById.getId()).isEqualTo(beerDto.getId());
        assertThat(beerDtoById.getQuantityOnHand()).isNull();
    }

    @Test
    void getBeerByUPC() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 1, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(1);

        BeerDto beerDto = pagedList.getContent().get(0);

        Mono<BeerDto> beerDtoMono = beerClient.getBeerByUPC(beerDto.getUpc());

        BeerDto beerDtoById = beerDtoMono.block();

        assertThat(beerDtoById).isNotNull();
        assertThat(beerDtoById.getUpc()).isEqualTo(beerDto.getUpc());
    }

    @Test
    void listBeers() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isGreaterThan(0);
        System.out.println(pagedList.getContent());
    }

    @Test
    void listBeersPageSize10() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 10, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(10);
        System.out.println(pagedList.getContent());
    }

    @Test
    void listBeersNoRecords() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(100, 10, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(0);
        System.out.println(pagedList.getContent());
    }

    @Test
    void createBeer() {
        BeerDto beerDto = BeerDto
                .builder()
                .beerName("Heineken")
                .beerStyle("STOUTd")
                .upc("3434343434333")
                .price(new BigDecimal("10.99"))
                .build();

        Mono<ResponseEntity<Void>> beer = beerClient.createBeer(beerDto);
        ResponseEntity<Void> responseEntity = beer.block();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    void updateBeer() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 1, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(1);

        BeerDto beerDto = pagedList.getContent().get(0);

        BeerDto updatedBeer = BeerDto.builder()
                .beerName("Really good beer")
                .beerStyle(beerDto.getBeerStyle())
                .price(beerDto.getPrice())
                .upc(beerDto.getUpc())
                .build();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.updateBeer(beerDto.getId(), updatedBeer);

        ResponseEntity<Void> responseEntity = responseEntityMono.block();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    void deleteBeerById() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 1, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(1);

        BeerDto beerDto = pagedList.getContent().get(0);

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(beerDto.getId());
        ResponseEntity<Void> responseEntity = responseEntityMono.block();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteBeerByIdNotFound () {
        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(UUID.randomUUID());
        assertThrows(WebClientResponseException.class, () -> {
            ResponseEntity<Void> responseEntity = responseEntityMono.block();
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        });
    }

    @Test
    void testDeleteBeerHandlesException () {
        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(UUID.randomUUID());
        ResponseEntity<Void> responseEntity = responseEntityMono
                .onErrorResume(throwable -> {
                    if (throwable instanceof WebClientResponseException) {
                        WebClientResponseException exception = (WebClientResponseException) throwable;
                        return Mono.just(ResponseEntity.status(exception.getStatusCode()).build());
                    } else {
                        throw new RuntimeException(throwable);
                    }
                })
                .block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}