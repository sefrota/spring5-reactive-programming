package com.sletras.beerclient.client;

import com.sletras.beerclient.config.WebclientProperties;
import com.sletras.beerclient.model.BeerDto;
import com.sletras.beerclient.model.BeerPagedList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

/**
 * @author sergioletras
 */
@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final WebClient webClient;

    @Override
    public Mono<BeerDto> getBeerById(UUID id, Boolean showInventoryOnHand) {
        return webClient.get()
                .uri(uriBuilder ->
                    uriBuilder.path(WebclientProperties.BEER_V1_PATH + "/" + id.toString())
                            .queryParamIfPresent("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
                            .build()
                    )
                .retrieve()
                .bodyToMono(BeerDto.class);
    }

    @Override
    public Mono<BeerDto> getBeerByUPC(String upc) {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(WebclientProperties.BEER_V1_UPC_PATH + "/" + upc)
                                .build()
                )
                .retrieve()
                .bodyToMono(BeerDto.class);
    }

    @Override
    public Mono<BeerPagedList> listBeers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnHand) {
        return webClient.get()
                .uri(uriBuilder ->
                    uriBuilder.path(WebclientProperties.BEER_V1_PATH)
                            .queryParamIfPresent("pageNumber", Optional.ofNullable(pageNumber))
                            .queryParamIfPresent("pageSize", Optional.ofNullable(pageSize))
                            .queryParamIfPresent("beerName", Optional.ofNullable(beerName))
                            .queryParamIfPresent("beerStyle", Optional.ofNullable(beerStyle))
                            .queryParamIfPresent("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
                            .build()
                )
                .retrieve()
                .bodyToMono(BeerPagedList.class);
    }

    @Override
    public Mono<ResponseEntity> createBeer(BeerDto beerDto) {
        return null;
    }

    @Override
    public Mono<ResponseEntity> updateBeer(BeerDto beerDto) {
        return null;
    }

    @Override
    public Mono<ResponseEntity> deleteBeerById(UUID id) {
        return null;
    }

}
