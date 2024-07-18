package mititelu.laura.introreactive;

import mititelu.laura.introreactive.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getById() {

        //blocking
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();
        System.out.println(person.toString());

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
        }).subscribe(firstName ->
                System.out.println("from map: "+ firstName));
    }

    @Test
    void fluxTestBlockFirst(){
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst(); //gives us back the first person
        System.out.println(person.toString());

    }

    @Test
    void testFluxSubscribe(){
        Flux<Person> personFlux = personRepository.findAll();

        StepVerifier.create(personFlux).expectNextCount(4).verifyComplete();

        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });

    }

    @Test
    void testFluxToListMono(){
        Flux<Person> personFlux = personRepository.findAll();
        Mono<List<Person>> personListMono = personFlux.collectList();

        personListMono.subscribe(list ->{
            System.out.println("printing list");
            list.forEach(person -> {
                System.out.println(person.toString());
            });
        });
    }

    @Test
    void findPersonById(){
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 3;

        Mono<Person> personMono = personFlux.filter(person ->
                person.getId() == id
        ).next();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });

    }

    @Test
    void findPersonByIdNotFound(){
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person ->
                person.getId() == id
        ).next(); //returns first element that matches or emtpy

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        }); //if not found nothing is being printed

    }

    @Test
    void findPersonByIdNotFoundWithException(){
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person ->
                person.getId() == id
        ).single(); //returns first element or throws NoSuchElementException
        // if empty source or IndexOutOfBoundsException if more than one el

        personMono
                .doOnError(throwable -> {
                    System.out.println("I went boom");
                })
                .onErrorReturn(Person.builder().build())
                .subscribe(person -> {
                    System.out.println(person.toString());
                });

    }

    @Test
    void testNewFindByIdFound(){
        final  Integer id = 3;
        Mono<Person> personMono = personRepository.getById(id);

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testNewFindByIdNotFound(){
        final Integer id = 99;
        Mono<Person> personMono = personRepository.getById(id);

        StepVerifier.create(personMono).verifyComplete();
        // or StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });

    }
}