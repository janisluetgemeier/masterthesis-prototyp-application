package de.luetgemeier.masterthesis.prototype.application.services;

import de.luetgemeier.masterthesis.prototype.application.entities.Benutzer;
import de.luetgemeier.masterthesis.prototype.application.repositories.BenutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private BenutzerRepository benutzerRepository;

    public Benutzer checkToken(String token) {

        Benutzer benutzer = benutzerRepository.findByToken(token);
        if (null == benutzer) {
            throw new IllegalArgumentException();
        } else {
            return benutzer;
        }
    }
}
