package mititelu_laura.sfg_reactive_brewery.web.mappers;

import mititelu_laura.sfg_reactive_brewery.domain.Customer;
import mititelu_laura.sfg_reactive_brewery.web.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDto dto);

    CustomerDto customerToCustomerDto(Customer customer);

}
