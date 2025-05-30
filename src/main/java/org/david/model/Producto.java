package org.david.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    @Id
    String id;
    String nombreProducto;
    int cantidadStock;
}
