package com.sletras.beerclient.client;

import com.sletras.beerclient.config.WebclientConfig;
import com.sletras.beerclient.model.BeerDto;
import com.sletras.beerclient.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;


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
    }

    @Test
    void updateBeer() {
    }

    @Test
    void deleteBeerById() {
    }


}