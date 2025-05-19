package org.david.repository;

import org.david.model.Franquicia;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FranquiciaRepository extends ReactiveMongoRepository<Franquicia, String> {
}
