package de.luetgemeier.masterthesis.prototype.application.repositories;

import de.luetgemeier.masterthesis.prototype.application.entities.Datei;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DateiRepository extends CrudRepository<Datei, Integer> {

    public List<Datei> findByUserid(Integer userid);
}
