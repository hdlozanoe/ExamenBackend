# Franquicias API – Examen Backend

## Descripción

Se requiere construir un API para manejar una lista de franquicias, Una franquicia se compone por un nombre y una lista de 
sucursales y, a su vez, una sucursal está compuesta por un nombre y un listado de productos ofertados en la sucursal. Un 
producto se compone de un nombre y una cantidad de Stock.  

## Como desplegar la aplicación 

Se requiere desplegar una base de datos MongoBD para almacenar la colección, es importante tener en cuenta que es necesario tener la conexión abierta para la ip desde la cual se va a realizar la prueba. Despues de esto se debe realizar un cambio en el archivo 'application.properties':

```
server.port = 8989

#MongoDB Configuration

spring.data.mongodb.uri = "uri de la mongodb"

spring.data.mongodb.database = "nombre de la base de datos"
```

Se debe remplazar los datos especificados en "" de esta forma:

```
server.port = 8989

#MongoDB Configuration
spring.data.mongodb.uri=mongodb+srv://hdlozanoe:VS4YfGEjLt4AqLLo@cluster0.q6qfjve.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0
spring.data.mongodb.database=Test
```

Ademas de esto es importante tener en cuenta que el servicio se va a exponer en el puerto 8989 por lo tanto debe estar libre o cambiar el puerto.
Se puede ejecutar por el docker o directamente con el main del proyecto. Es importante aclarar que se esta haciendo uso de JAVA 17.

Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring WebFlux**
- **MongoDB reactivo**
- **Docker**

## Ejemplo de la estructura del postman

```json
{
    "nombre": "Gabriel G",
    "sucursales": [
        {
            "nombreSucursal": "La 80",
            "productos": [
                {
                    "nombreProducto": "FSDF",
                    "cantidadStock": 2
                }
            ]
        }
    ]
}
```
Tener en cuenta que:

 - La franquicia contiene sucursales las cuales tiene productos.
 - Se maneja solo una coleccion ya que facilita no solo la estructura de los datos, sino que adicionalmente simplifica las consultas que se van a realizar

Es necesario poblar la colección para realizar las pruebas, para esto se puede hacer apartir de la creacion de la franquicia con la estructura que se mostro antes.


## Endpoints

| Método | Endpoint                  										| Descripción                   	|
|--------|------------------------------------------------------------------------------------------------------|---------------------------------------|
| POST   | `/api/franquicias/`         										| Crear nueva franquicia        	|
| POST   | `/api/franquicias/{idFranquicia}/sucursales/`        						| Agregar una sucursal			|
| POST   | `/api/franquicias/{idFranquicia}/sucursales/{nombreSucursal}/productos/`    				| Agregar un producto a una sucursal 	|
| DELETE | `/api/franquicias/{idFranquicia}/sucursales/{nombreSucursal}/productos/{nombreProducto}`     	| Eliminar un producto		  	|
| PUT    | `/api/franquicias/{idFranquicia}/sucursales/{nombreSucursal}/productos/{nombreProducto}/stock`    	| Actualizar el stock de un producto   	|
| GET    | `/api/franquicias/{idFranquicia}/producto-stock`  							| Obtener el stock mas alto por sucursal|
| PUT    | `/api/franquicias/{idFranquicia}/nombre`    								| Cambiar el nombre de la franquicia    |
| PUT    | `/api/franquicias/{idFranquicia}/sucursal/{nombreSucursal}/nombre`        				| Cambiar el nombre de la sucursal  	|
| PUT    | `/api/franquicias/{idFranquicia}/sucursales/{nombreSucursal}/productos/{nombreProducto}/nombre`    	| Cambiar el nombre del producto     	| 

