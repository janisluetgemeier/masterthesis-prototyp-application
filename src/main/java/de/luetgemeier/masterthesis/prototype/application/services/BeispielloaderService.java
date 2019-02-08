package de.luetgemeier.masterthesis.prototype.application.services;

import de.luetgemeier.masterthesis.prototype.application.entities.Benutzer;
import de.luetgemeier.masterthesis.prototype.application.repositories.BenutzerRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeispielloaderService implements InitializingBean {

    @Autowired
    private BenutzerRepository benutzerRepository;

    @Autowired
    private PasswordService passwordService;

    @Override
    public void afterPropertiesSet() throws Exception {

        Benutzer myUser = Benutzer.builder().email("benutzer@benutzer.de").isadmin(false).name("Max Musterbenutzer").password(passwordService.hashPassword("benutzer")).build();
        benutzerRepository.save(myUser);

        Benutzer myAdmin = Benutzer.builder().email("admin@admin.de").isadmin(true).name("Martina Musteradministrator").password(passwordService.hashPassword("admin")).build();
        benutzerRepository.save(myAdmin);
    }
}
