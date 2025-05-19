package org.david.repository;

import org.david.model.Sucursal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SucursalRepository extends ReactiveMongoRepository<Sucursal, String> {
}
