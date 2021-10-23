package com.ffuster.netflux.controllers;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.ffuster.netflux.domain.Movie;
import com.ffuster.netflux.domain.MovieEvent;
import com.ffuster.netflux.serivces.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;

  public RouterFunction<ServerResponse> allRoutes() {
    return route().path("/movies", builder -> builder
        .GET("/{id}", accept(MediaType.APPLICATION_JSON), this::getMovie)
        .GET("", accept(MediaType.APPLICATION_JSON), this::listMovies))
        .GET("/{id}/events", accept(MediaType.APPLICATION_JSON), this::streamMovieEvents)
        .build();
  }

  private Mono<ServerResponse> getMovie(ServerRequest request) {
    String id = request.pathVariable("id");
    return movieService.getMovieById(id).flatMap(movie -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(movie))
        .switchIfEmpty(notFound().build());
  }

  private Mono<ServerResponse> listMovies(ServerRequest request) {
    return ok().contentType(MediaType.APPLICATION_JSON).body(movieService.getAllMovies(), Movie.class);
  }

  private Mono<ServerResponse> streamMovieEvents(ServerRequest request){
    String id = request.pathVariable("id");
    return ok().contentType(MediaType.TEXT_EVENT_STREAM).body(movieService.streamMovieEvents(id), MovieEvent.class);
  };
}
