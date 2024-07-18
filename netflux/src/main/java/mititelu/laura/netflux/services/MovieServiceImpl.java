package mititelu.laura.netflux.services;

import lombok.RequiredArgsConstructor;
import mititelu.laura.netflux.domain.Movie;
import mititelu.laura.netflux.domain.MovieEvent;
import mititelu.laura.netflux.repositories.MovieRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public Mono<Movie> getMovieById(String id) {
        return movieRepository.findById(id);
    }

    @Override
    public Flux<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Flux<MovieEvent> streamMovieEvents(String id) {
        return Flux.<MovieEvent>generate(movieEventSynchronousSink -> {
            movieEventSynchronousSink.next(new MovieEvent(id, LocalDateTime.now()));
        }).delayElements(Duration.ofSeconds(1)); //will emit a new movie event every second!
    }
}
