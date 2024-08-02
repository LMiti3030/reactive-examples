package mititelu_laura.sfg_reactive_brewery.web.mappers;

import mititelu_laura.sfg_reactive_brewery.domain.Beer;
import mititelu_laura.sfg_reactive_brewery.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {

    BeerDto beerToBeerDto(Beer beer);
    BeerDto beerToBeerDtoWithInventory(Beer beer);

    Beer beerDtoToBeer(BeerDto dto);

}
