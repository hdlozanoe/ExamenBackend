package org.david.service;

import org.david.model.Franquicia;
import org.david.model.Producto;
import org.david.model.ProductoStock;
import org.david.model.Sucursal;
import reactor.core.publisher.Mono;
import java.util.List;

public interface FranquiciaService {
    Mono<Franquicia> crearFranquicia(Franquicia franquicia);
    Mono<Franquicia> crearSucursal(String franquiciaId, Sucursal sucursal);
    Mono<Franquicia> agregarProducto(String franquiciaId, String nombreSucursal, Producto producto);
    Mono<Franquicia> eliminarProducto(String franquiciaId, String nombreSucursal, String nombreProducto);
    Mono<Franquicia> modificarProducto(String franquiciaId, String nombreSucursal, String nombreProducto, int stockAdicional);
    Mono<List<ProductoStock>> obtenerProductoStock(String franquiciaId);
    Mono<Franquicia> actualizarFranquicia(String franquiciaId, String nombreFranquicia);
    Mono<Franquicia> actualizarSucursal(String franquiciaId, String nombreSucursal, String nuevaSucursal);
    Mono<Franquicia> actualizarProducto(String franquiciaId, String nombreSucursal, String nombreProducto, String nuevoProducto);
}
