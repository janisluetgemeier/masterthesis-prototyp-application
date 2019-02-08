package de.luetgemeier.masterthesis.prototype.application.repositories;

import de.luetgemeier.masterthesis.prototype.application.entities.Benutzer;
import org.springframework.data.repository.CrudRepository;

public interface BenutzerRepository extends CrudRepository<Benutzer, Integer> {

    Benutzer findByEmail(String email);
    Benutzer findByToken(String token);

}
