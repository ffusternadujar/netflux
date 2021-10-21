package com.ffuster.netflux;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.ffuster.netflux.controllers.MovieController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@SpringBootApplication
public class NetfluxApplication {

  public static void main(String[] args) {
    SpringApplication.run(NetfluxApplication.class, args);
  }

  @Bean
  RouterFunction<ServerResponse> allApplicationRoutes(MovieController movieController) {
    return route().add(movieController.allRoutes()).build();
  }



}
