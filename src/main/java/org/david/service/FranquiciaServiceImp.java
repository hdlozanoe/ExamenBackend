package org.david.service;

import org.david.model.Franquicia;
import org.david.model.Producto;
import org.david.model.ProductoStock;
import org.david.model.Sucursal;
import org.david.repository.FranquiciaRepository;
import org.david.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FranquiciaServiceImp implements FranquiciaService{

    @Autowired
    private FranquiciaRepository franquiciaRepository;
    @Autowired
    private SucursalRepository sucursalRepository;

    public FranquiciaServiceImp(FranquiciaRepository franquiciaRepository, SucursalRepository sucursalRepository){
        this.franquiciaRepository = franquiciaRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public Mono<Franquicia> crearFranquicia(Franquicia franquicia) {
        System.out.println("Recibido: " + franquicia);
        return franquiciaRepository.save(franquicia);
    }

    @Override
    public Mono<Franquicia> crearSucursal(String idFranquicia, Sucursal sucursales) {
        return franquiciaRepository.findById(idFranquicia)
                .flatMap(franquicia -> {
                    if(franquicia.getSucursales() == null){
                        franquicia.setSucursales(new ArrayList<>());
                    }
                    boolean eFranquicia = franquicia.getSucursales().stream().anyMatch(sucursal ->
                            sucursal.getNombreSucursal().equalsIgnoreCase(sucursales.getNombreSucursal()));
                    if (eFranquicia){
                        return Mono.error(new RuntimeException("La sucursal ya esta asociada a una franquicia."));
                    }
                    franquicia.getSucursales().add(sucursales);
                    return franquiciaRepository.save(franquicia);
                });
    }

    @Override
    public Mono<Franquicia> agregarProducto(String franquiciaId, String nombreSucursal, Producto producto) {
        return franquiciaRepository.findById(franquiciaId)
                .flatMap(franquicia -> {
                    if(franquicia.getSucursales() == null){
                        return Mono.error(new RuntimeException("No hay sucursales asociadas a la franquicia"));
                    }
                    Optional<Sucursal> sucursalOptional = franquicia.getSucursales().stream()
                            .filter(sucursal -> sucursal.getNombreSucursal().equalsIgnoreCase(nombreSucursal))
                            .findFirst();
                    if(sucursalOptional.isEmpty()){
                        return Mono.error(new RuntimeException("Sucursal no existe"));
                    }
                    Sucursal sucursal = sucursalOptional.get();
                    if(sucursal.getProductos() == null){
                        sucursal.setProductos(new ArrayList<>());
                    }
                    boolean productoExistente = sucursal.getProductos().stream()
                            .anyMatch(productoNuevo ->
                                    productoNuevo.getNombreProducto()
                                            .equalsIgnoreCase(productoNuevo.getNombreProducto()));
                    if(productoExistente){
                        return Mono.error(new RuntimeException("El producto ingresado ya existe en la sucursal"));
                    }
                    sucursal.getProductos().add(producto);
                    return franquiciaRepository.save(franquicia);
                });
    }

    @Override
    public Mono<Franquicia> eliminarProducto(String franquiciaId, String nombreSucursal, String nombreProducto) {
        return franquiciaRepository.findById(franquiciaId)
                .flatMap(franquicia -> {
                    Optional<Sucursal> sucursalOpcional = franquicia.getSucursales().stream()
                            .filter(sucursal -> sucursal.getNombreSucursal().equals(nombreSucursal))
                            .findFirst();
                    if(sucursalOpcional.isPresent()){
                        Sucursal sucursal = sucursalOpcional.get();
                        List<Producto> productos = sucursal.getProductos().stream()
                                .filter(producto -> !producto.getNombreProducto().equals(nombreProducto))
                                .collect(Collectors.toList());
                        sucursal.setProductos(productos);
                        franquicia.setSucursales(Collections.singletonList(sucursal));
                        return franquiciaRepository.save(franquicia);
                    }else{
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "La sucursal del producto no fue encontrada"));
                    }
                });
    }

    @Override
    public Mono<Franquicia> modificarProducto(String franquiciaId, String nombreSucursal, String nombreProducto, int stockAdicional) {
        return franquiciaRepository.findById(franquiciaId)
                .flatMap(franquicia -> {
                    Optional<Sucursal> optionalSucursal = franquicia.getSucursales().stream()
                            .filter(sucursal -> sucursal.getNombreSucursal().equals(nombreSucursal))
                            .findFirst();
                    if(optionalSucursal.isPresent()){
                        Sucursal sucursal = optionalSucursal.get();
                        Optional<Producto> optionalProducto = sucursal.getProductos().stream()
                                .filter(producto -> producto.getNombreProducto().equals(nombreProducto))
                                .findFirst();
                        if(optionalProducto.isPresent()){
                            Producto producto = optionalProducto.get();
                            producto.setCantidadStock(stockAdicional);
                            List<Producto> productoActualizado = sucursal.getProductos().stream()
                                    .map(productos -> productos.getNombreProducto().equals(nombreProducto) ? producto : productos)
                                    .collect(Collectors.toList());
                            sucursal.setProductos(productoActualizado);
                            return franquiciaRepository.save(franquicia);
                        }else{
                            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no ha sido encontrado"));
                        }
                    }else {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "La Sucursal no fue encontrada"));
                    }
                });
    }

    @Override
    public Mono<List<ProductoStock>> obtenerProductoStock(String franquiciaId) {
        return franquiciaRepository.findById(franquiciaId)
                .flatMap(franquicia -> {
                    List<ProductoStock> resultadoStock = franquicia.getSucursales().stream()
                            .map(sucursal -> {
                                Producto producto = sucursal.getProductos().stream()
                                        .max(Comparator.comparingInt(Producto::getCantidadStock))
                                        .orElse(null);
                                return new ProductoStock(sucursal.getNombreSucursal(), producto);
                            })
                            .collect(Collectors.toList());
                    return Mono.just(resultadoStock);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "La franquicia no fue encontrada")));
    }

    @Override
    public Mono<Franquicia> actualizarFranquicia(String franquiciaId, String nombreFranquicia) {
        return franquiciaRepository.findById(franquiciaId)
                .flatMap(franquicia -> {
                    franquicia.setNombre(nombreFranquicia);
                    return franquiciaRepository.save(franquicia);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "La franquicia no existe")));
    }

    @Override
    public Mono<Franquicia> actualizarSucursal(String franquiciaId, String nombreSucursal, String nuevaSucursal) {
        return franquiciaRepository.findById(franquiciaId)
                .flatMap(franquicia -> {
                    Optional<Sucursal> optionalSucursal = franquicia.getSucursales().stream()
                            .filter(sucursal -> sucursal.getNombreSucursal().equals(nombreSucursal))
                            .findFirst();
                    if(optionalSucursal.isPresent()){
                        Sucursal sucursal = optionalSucursal.get();
                        sucursal.setNombreSucursal(nuevaSucursal);
                        return franquiciaRepository.save(franquicia);
                    }else{
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "La sucursal no existe"));
                    }
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "La franquicia no existe")));
    }

    @Override
    public Mono<Franquicia> actualizarProducto(String franquiciaId, String nombreSucursal, String nombreProducto, String nuevoProducto) {
        return franquiciaRepository.findById(franquiciaId)
                .flatMap(franquicia -> {
                    Optional<Sucursal> optionalSucursal = franquicia.getSucursales().stream()
                            .filter(sucursal -> sucursal.getNombreSucursal().equals(nombreSucursal))
                            .findFirst();
                    if (optionalSucursal.isPresent()){
                        Sucursal sucursal = optionalSucursal.get();
                        Optional<Producto> optionalProducto = sucursal.getProductos().stream()
                                .filter(producto -> producto.getNombreProducto().equals(nombreProducto))
                                .findFirst();
                        if (optionalProducto.isPresent()){
                            Producto producto = optionalProducto.get();
                            producto.setNombreProducto(nuevoProducto);
                            return franquiciaRepository.save(franquicia);
                        }else{
                            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe"));
                        }
                    }else{
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "La sucursal no existe"));
                    }
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "La franquicia no existe")));
    }

}
