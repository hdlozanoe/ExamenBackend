package org.david.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sucursal {
    @Id
    String id;
    String nombreSucursal;
    List<Producto> productos;
}
