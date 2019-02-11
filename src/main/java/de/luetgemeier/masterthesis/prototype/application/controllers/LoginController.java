package de.luetgemeier.masterthesis.prototype.application.controllers;

import de.luetgemeier.masterthesis.prototype.application.entities.Benutzer;
import de.luetgemeier.masterthesis.prototype.application.repositories.BenutzerRepository;
import de.luetgemeier.masterthesis.prototype.application.services.AuthService;
import de.luetgemeier.masterthesis.prototype.application.services.PasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    BenutzerRepository benutzerRepository;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordService passwordService;

    @RequestMapping(path = "/login/v1", method = RequestMethod.POST)
    public Benutzer doLogin(@RequestBody Benutzer benutzer) throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException {


        //Check for id:
        Benutzer zielBenutzer = benutzerRepository.findByEmail(benutzer.getEmail());

        if(zielBenutzer == null || zielBenutzer.getId() == null) {
            throw new IllegalArgumentException();
        }


        //Password Check:

        String hashed = passwordService.hashPassword(benutzer.getPassword());

        log.info("Calculated hash is: " + hashed);
        log.info("DB hash is: " + zielBenutzer.getPassword());


        if(!hashed.equals(zielBenutzer.getPassword())) {
            throw new IllegalArgumentException();
        }

        String newToken = passwordService.createToken();

        zielBenutzer.setToken(newToken);

        benutzerRepository.save(zielBenutzer);

        return zielBenutzer;
    }

    @RequestMapping(path = "/logout/v1", method = RequestMethod.POST)
    public void logOut(@RequestHeader("token") String token) {

        Benutzer benutzer = authService.checkToken(token);

        benutzer.setToken(null);

        benutzerRepository.save(benutzer);
    }
}
