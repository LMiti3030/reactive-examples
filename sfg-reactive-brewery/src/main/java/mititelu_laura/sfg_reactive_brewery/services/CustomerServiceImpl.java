package mititelu_laura.sfg_reactive_brewery.services;

import lombok.extern.slf4j.Slf4j;
import mititelu_laura.sfg_reactive_brewery.web.model.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        return CustomerDto.builder().id(UUID.randomUUID()).name("George Popescu").build();
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customerDto) {
        return CustomerDto.builder().id(UUID.randomUUID()).build();
    }

    @Override
    public void updateCustomer(UUID customerId, CustomerDto customerDto) {
        //todo impl
        log.debug("Updating");
    }

    @Override
    public void deleteById(UUID customerId) {
        //todo impl
        log.debug("Deleting");
    }
}
