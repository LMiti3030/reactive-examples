package mititelu.laura.beer_client.client;

import mititelu.laura.beer_client.config.WebClientConfig;
import mititelu.laura.beer_client.model.BeerDto;
import mititelu.laura.beer_client.model.BeerPagedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class BeerClientImplTest {


    BeerClientImpl beerClient;


    @BeforeEach
    void setUp() {
        beerClient = new BeerClientImpl(new WebClientConfig().webclient()); //a lot leaner for testing purposes!!
    }



    @Test
    void listBeers() {

        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, false);

        BeerPagedList pagedList = beerPagedListMono.block(); //block and stay on this thread

        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isGreaterThan(0);

        System.out.println(pagedList.toList());
    }

    @Test
    void listBeersPageSize10() {

        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 10, null, null, false);

        BeerPagedList pagedList = beerPagedListMono.block(); //block and stay on this thread

        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(10);

    }

    @Test
    void listBeersNoRecords() {

        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(10, 20, null, null, false);

        BeerPagedList pagedList = beerPagedListMono.block(); //block and stay on this thread

        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(0);

    }

    @Disabled("API is returning inventory  when it should not be")
    @Test
    void getBeerById() {

        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, false);

        List<BeerDto> pagedList = beerPagedListMono.block().toList();

       // UUID firstID = beerPagedListMono.block().getContent().get(0).getId();

        UUID firstID = pagedList.get(0).getId();

        Mono<BeerDto> beerDtoMono = beerClient.getBeerById(firstID, false);

        BeerDto beerDto = beerDtoMono.block();

        assertThat(beerDto).isNotNull();
        assertThat(beerDto.getId().toString()).isEqualTo(firstID.toString());
        assertThat(beerDto.getQuantityOnHand()).isNull();

    }

    @Test
    void functionalTestGetBeerById() throws InterruptedException {

        AtomicReference<String> beerName = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        beerClient.listBeers(null, null, null, null, true)
                .map(beerPagedList -> beerPagedList.getContent().get(0).getId())
                .map(beerId -> beerClient.getBeerById(beerId, false))
                .flatMap( mono -> mono)
                .subscribe(beerDto ->{
                    System.out.println(beerDto.getBeerName());
                    beerName.set(beerDto.getBeerName());
                    assertThat(beerDto.getBeerName()).isEqualTo("Mango Bobs");
                    countDownLatch.countDown();
                });

       countDownLatch.await(); //will stop and wait until that countdown latch has been incremented by a value of 1
        //that is until countDownLatch.countDown(); get executed
        assertThat(beerName.get()).isEqualTo("Mango Bobs");
    }

    @Test
    void getBeerByIdShowInventoryTrue() {

        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, true);

        List<BeerDto> pagedList = beerPagedListMono.block().toList();

        UUID firstID = beerPagedListMono.block().getContent().get(0).getId();

        // UUID firstID = pagedList.get(0).getId();

        Mono<BeerDto> beerDtoMono = beerClient.getBeerById(firstID, true);

        BeerDto beerDto = beerDtoMono.block();

        assertThat(beerDto).isNotNull();
        assertThat(beerDto.getId().toString()).isEqualTo(firstID.toString());
        assertThat(beerDto.getQuantityOnHand()).isNotNull();

    }

    @Test
    void getBeerByUpc() {

        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, false);

        List<BeerDto> pagedList = beerPagedListMono.block().toList();

        String firstUPC = pagedList.get(0).getUpc();

        Mono<BeerDto> beerDtoMono = beerClient.getBeerByUpc(firstUPC);

        BeerDto beerDto = beerDtoMono.block();

        assertThat(beerDto).isNotNull();
        assertThat(beerDto.getUpc()).isEqualTo(firstUPC);

    }

    @Test
    void createBeer() {

        BeerDto beerDto = BeerDto.builder()
                .beerName("Ciuc")
                .beerStyle("IPA")
                .upc("25945652122")
                .price(new BigDecimal("8.99"))
                .build();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.createBeer(beerDto);
        ResponseEntity response = responseEntityMono.block();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    void updateBeer() {

        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        BeerDto beerDto = pagedList.getContent().get(0);

        BeerDto updatedBeerDto = BeerDto.builder()
                .beerName("Ursus")
                .beerStyle(beerDto.getBeerStyle())
                .price(beerDto.getPrice())
                .upc(beerDto.getUpc())
                .build();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.updateBeer(beerDto.getId(), updatedBeerDto);
        ResponseEntity<Void> response = responseEntityMono.block();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


    }

    @Test
    void deleteBeerById() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();
        BeerDto beerDto = pagedList.getContent().get(0);

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(beerDto.getId());
        ResponseEntity<Void> response = responseEntityMono.block();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    void deleteBeerByIdNotFound() {

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(UUID.randomUUID());


        Assertions.assertThrows(WebClientResponseException.NotFound.class,  () -> {
            ResponseEntity<Void> response = responseEntityMono.block();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        });

        //assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND); -> it will fail

    }

    @Test
    void testDeleteBeerHandleException() {

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(UUID.randomUUID());

        ResponseEntity<Void> responseEntity = responseEntityMono
                .onErrorResume(throwable ->{
                    if(throwable instanceof WebClientResponseException){
                        WebClientResponseException exception = (WebClientResponseException) throwable;
                        return  Mono.just(ResponseEntity.status(exception.getStatusCode()).build());
                    }
                    else {
                        throw  new RuntimeException(throwable);
                    }
                })
                .block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

}