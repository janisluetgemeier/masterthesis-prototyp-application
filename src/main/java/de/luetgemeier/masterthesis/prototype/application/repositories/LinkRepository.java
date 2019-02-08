package de.luetgemeier.masterthesis.prototype.application.repositories;

import de.luetgemeier.masterthesis.prototype.application.entities.Link;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LinkRepository extends CrudRepository<Link, Integer> {
    Link findByHash(String hash);
    Link findByFileid(Integer fileid);
    List<Link> findByUserid(Integer userid);

}
