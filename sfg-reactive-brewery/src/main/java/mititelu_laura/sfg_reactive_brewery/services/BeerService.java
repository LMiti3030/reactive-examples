package mititelu_laura.sfg_reactive_brewery.services;


import mititelu_laura.sfg_reactive_brewery.web.model.BeerDto;
import mititelu_laura.sfg_reactive_brewery.web.model.BeerPagedList;
import mititelu_laura.sfg_reactive_brewery.web.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface BeerService {
    BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand);

    BeerDto getById(UUID beerId, Boolean showInventoryOnHand);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);

    BeerDto getByUpc(String upc);

    void deleteBeerById(UUID beerId);
}
