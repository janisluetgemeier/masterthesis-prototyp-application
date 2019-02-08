package de.luetgemeier.masterthesis.prototype.application.services;

import de.luetgemeier.masterthesis.prototype.application.entities.Benutzer;
import de.luetgemeier.masterthesis.prototype.application.entities.Datei;
import de.luetgemeier.masterthesis.prototype.application.entities.Link;
import de.luetgemeier.masterthesis.prototype.application.repositories.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private PasswordService passwordService;

    public Link createLink(Datei datei, Benutzer benutzer) {

        String rndHash = passwordService.createToken();

        Link link = Link.builder().hash(rndHash).fileid(datei.getId()).userid(benutzer.getId()).build();
        linkRepository.save(link);
        return link;
    }
}
