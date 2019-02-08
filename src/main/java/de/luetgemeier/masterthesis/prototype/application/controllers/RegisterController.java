package de.luetgemeier.masterthesis.prototype.application.controllers;

import de.luetgemeier.masterthesis.prototype.application.entities.Benutzer;
import de.luetgemeier.masterthesis.prototype.application.repositories.BenutzerRepository;
import de.luetgemeier.masterthesis.prototype.application.services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class RegisterController {

    @Autowired
    PasswordService passwordService;

    @Autowired
    BenutzerRepository benutzerRepository;

    @Value( "${de.luetgemeier.masterthesis.prototype.application.register.active}" )
    private Boolean registerPossible = false;

    @RequestMapping(path = "/register/v1", method = RequestMethod.POST)
   public Benutzer register( @RequestBody Benutzer benutzer) throws InvalidKeySpecException, NoSuchAlgorithmException, InterruptedException, UnsupportedEncodingException {

        if(!registerPossible.booleanValue()) {
            throw new IllegalArgumentException();
        }

        //Check Email:
        Pattern p = Pattern.compile("^(.+)@(.+)$");
        Matcher m = p.matcher(benutzer.getEmail());

        if(!m.matches()) {
            throw new IllegalArgumentException();
        }

        if(StringUtils.isEmpty(benutzer.getName())) {
            throw new IllegalArgumentException();
        }

        if(StringUtils.isEmpty(benutzer.getPassword()) || benutzer.getPassword().length() < 2) {
            throw new IllegalArgumentException();
        }




        Thread.sleep(2000);
        //Hash Password before in DB:
        benutzer.setPassword(passwordService.hashPassword(benutzer.getPassword()));

        benutzerRepository.save(benutzer);

        return benutzer;
   }
}
