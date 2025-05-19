Franquicias API – Examen Backend

Descripción

Se requiere construir un API para manejar una lista de franquicias, Una franquicia se compone por un nombre y una lista de 
sucursales y, a su vez, una sucursal está compuesta por un nombre y un listado de productos ofertados en la sucursal. Un 
producto se compone de un nombre y una cantidad de Stock.  

Como desplegar la aplicación 

Se requiere desplegar una base de datos MongoBD para almacenar la colección, es importante tener en cuenta que es necesario tener la conexión abierta para la ip desde la clual se va a realizar la prueba, el cambio se debe realizar en el archivo 'application.properties' de la siguiente forma:

server.port = 8989

# MongoDB Configuration
spring.data.mongodb.uri=<uri de la mongodb>
spring.data.mongodb.database=<nombre de la base de datos>

Se debe remplazar los datos especificados en <> por ejemplo:

server.port = 8989

# MongoDB Configuration
spring.data.mongodb.uri=mongodb+srv://hdlozanoe:VS4YfGEjLt4AqLLo@cluster0.q6qfjve.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0
spring.data.mongodb.database=Test

Ademas de esto es importante tener en cuenta que el servicio se va a exponer en el puerto 8989 por lo tanto debe estar libre o cambiar el puerto

Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring WebFlux**
- **MongoDB reactivo**
- **Docker**

Ejemplo de la estructura del postman


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


La franquicia contiene sucursales las cuales tiene productos.

Es necesario poblar la collección para realizar las pruebas, para esto se puede hacer apartir de la creacion de la franquicia con la estructura que se mostro antes.


Endpoints

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

