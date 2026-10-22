package com.backjuegos.project.repositories;

import com.backjuegos.project.entities.Juego;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoRepository extends MongoRepository<Juego, String> {
}