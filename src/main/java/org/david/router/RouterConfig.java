package org.david.router;

import org.david.handler.FranquiciaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> rutasFranquicia(FranquiciaHandler franquiciaHandler){
        return route()
                .POST("/api/franquicias/", franquiciaHandler::crearFranquicia)
                .POST("/api/franquicias/{idFranquicia}/sucursales/", franquiciaHandler::agregarSucursal)
                .POST("/api/franquicias/{idFranquicia}/sucursales/{nombreSucursal}/productos/",
                        franquiciaHandler::agregarProductoSucursal)
                .DELETE("/api/franquicias/{idFranquicia}/sucursales/{nombreSucursal}/productos/{nombreProducto}",
                        franquiciaHandler::eliminarProducto)
                .PUT("/api/franquicias/{idFranquicia}/sucursales/{nombreSucursal}/productos/{nombreProducto}/stock",
                        franquiciaHandler::actualizarProducto)
                .GET("/api/franquicias/{idFranquicia}/producto-stock", franquiciaHandler::obtenerProductoStock)
                .PUT("/api/franquicias/{idFranquicia}/nombre", franquiciaHandler::actualizarNombreFranquicia)
                .PUT("/api/franquicias/{idFranquicia}/sucursal/{nombreSucursal}/nombre", franquiciaHandler::actualizarNombreSucursal)
                .PUT("/api/franquicias/{idFranquicia}/sucursales/{nombreSucursal}/productos/{nombreProducto}/nombre",
                        franquiciaHandler::actualizarNombreProducto)
                .build();
    }
}
