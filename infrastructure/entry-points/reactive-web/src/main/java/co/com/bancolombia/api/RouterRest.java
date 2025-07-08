package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(PUT("/api/{id}/close"), handler::close)
                .andRoute(PUT("/api/{id}/open"), handler::open)
                .andRoute(PATCH("/api/{id}"), handler::updateBoxName)
                .andRoute(PUT("/api/{id}/reopen"), handler::reopen)
                .and(route(POST("/api"), handler::createBox))
                .and(route(DELETE("/api/{id}"), handler::deleteBox))
                .and(route(GET("/api/{id}"), handler::getBoxById))
                .and(route(GET("/api"), handler::listAllBox));
    }
}
