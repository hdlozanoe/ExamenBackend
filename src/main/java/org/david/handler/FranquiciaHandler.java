package org.david.handler;

import org.david.model.Franquicia;
import org.david.model.Producto;
import org.david.model.Sucursal;
import org.david.service.FranquiciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class FranquiciaHandler {

    @Autowired
    private FranquiciaService franquiciaService;

    public Mono<ServerResponse> crearFranquicia(ServerRequest request){
        return request.bodyToMono(Franquicia.class)
                .flatMap(franquicia -> franquiciaService.crearFranquicia(franquicia))
                .flatMap(franquicia -> ServerResponse.ok().bodyValue(franquicia))
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> agregarSucursal(ServerRequest request){
        String idFranquicia = request.pathVariable("idFranquicia");
        return request.bodyToMono(Sucursal.class)
                .flatMap(sucursal -> franquiciaService.crearSucursal(idFranquicia, sucursal))
                .flatMap(updateFranquicia -> ServerResponse.ok().bodyValue(updateFranquicia))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    public Mono<ServerResponse> agregarProductoSucursal(ServerRequest request){
        String franquiciaId = request.pathVariable("franquiciaId");
        String nombreSucursal = request.pathVariable("nombreSucursal");
        return request.bodyToMono(Producto.class)
                .flatMap(producto -> franquiciaService.agregarProducto(franquiciaId, nombreSucursal, producto))
                .flatMap(updateFranquicia -> ServerResponse.ok().bodyValue(updateFranquicia))
                .onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
    }
    public Mono<ServerResponse> eliminarProducto(ServerRequest request){
        String franquiciaId = request.pathVariable("franquiciaId");
        String nombreSucursal = request.pathVariable("nombreSucursal");
        String nombreProducto = request.pathVariable("nombreProducto");
        return franquiciaService.eliminarProducto(franquiciaId, nombreSucursal, nombreProducto)
                .flatMap(almacenar -> ServerResponse.ok().bodyValue(almacenar))
                .onErrorResume(ResponseStatusException.class, error ->
                        ServerResponse.status(error.getStatusCode()).bodyValue(error.getReason()));
    }
    public Mono<ServerResponse> actualizarProducto(ServerRequest request){
        String franquiciaId = request.pathVariable("franquiciaId");
        String nombreSucursal = request.pathVariable("nombreSucursal");
        String nombreProducto = request.pathVariable("nombreProducto");
        int stock = Integer.parseInt(request.queryParam("stock").orElse("0"));
        return franquiciaService.modificarProducto(franquiciaId, nombreSucursal, nombreProducto, stock)
                .flatMap(almacenar -> ServerResponse.ok().bodyValue(almacenar))
                .onErrorResume(ResponseStatusException.class, error ->
                        ServerResponse.status(error.getStatusCode()).bodyValue(error.getReason()));
    }

    public Mono<ServerResponse> obtenerProductoStock(ServerRequest request){
        String franquiciaId = request.pathVariable("franquiciaId");
        return franquiciaService.obtenerProductoStock(franquiciaId)
                .flatMap(productoStocks -> ServerResponse.ok().bodyValue(productoStocks))
                .onErrorResume(ResponseStatusException.class, error ->
                        ServerResponse.status(error.getStatusCode()).bodyValue(error.getReason()));
    }

    public Mono<ServerResponse> actualizarNombreFranquicia(ServerRequest request){
        String franquiciaId = request.pathVariable("franquiciaId");
        String nombreFranquicia = request.queryParam("nombreFranquicia")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El nombre de la franquicia es obligatorio"));
        return franquiciaService.actualizarFranquicia(franquiciaId, nombreFranquicia)
                .flatMap(franquicia -> ServerResponse.ok().bodyValue(franquicia))
                .onErrorResume(ResponseStatusException.class, error ->
                        ServerResponse.status(error.getStatusCode()).bodyValue(error.getReason()));
    }

    public Mono<ServerResponse> actualizarNombreSucursal(ServerRequest request){
        String franquiciaId = request.pathVariable("franquiciaId");
        String nombreSucursal = request.pathVariable("nombreSucursal");
        String nuevaSucursal = request.queryParam("nuevoNombre")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Es obligatorio agregar el nuevo nombre Sucursal"));
        return franquiciaService.actualizarSucursal(franquiciaId, nombreSucursal, nuevaSucursal)
                .flatMap(franquicia -> ServerResponse.ok().bodyValue(franquicia))
                .onErrorResume(ResponseStatusException.class, error ->
                        ServerResponse.status(error.getStatusCode()).bodyValue(error.getReason()));
    }
    public Mono<ServerResponse> actualizarNombreProducto(ServerRequest request){
        String franquiciaId = request.pathVariable("franquiciaId");
        String nombreSucursal = request.pathVariable("nombreSucursal");
        String nombreProducto = request.pathVariable("nombreProducto");
        String nuevoProducto = request.queryParam("nuevoNombre")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nuevo nombre del producto es obligatorio"));
        return franquiciaService.actualizarProducto(franquiciaId, nombreSucursal, nombreProducto, nuevoProducto)
                .flatMap(franquicia -> ServerResponse.ok().bodyValue(franquicia))
                .onErrorResume(ResponseStatusException.class, error ->
                        ServerResponse.status(error.getStatusCode()).bodyValue(error.getReason()));
    }

}
