package de.luetgemeier.masterthesis.prototype.application.controllers;

import com.google.common.collect.FluentIterable;
import de.luetgemeier.masterthesis.prototype.application.entities.Benutzer;
import de.luetgemeier.masterthesis.prototype.application.repositories.BenutzerRepository;
import de.luetgemeier.masterthesis.prototype.application.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BenutzerController {

    @Autowired
    private AuthService authService;

    @Autowired
    private BenutzerRepository benutzerRepository;

    @PostMapping("/user/v1/{userId:.+}")
    public void deleteUser(@PathVariable Integer userId, @RequestHeader String token) {

        Benutzer benutzer = authService.checkToken(token);

        if (!benutzer.isIsadmin()) {
            throw new IllegalArgumentException();
        }

        Optional<Benutzer> optional = benutzerRepository.findById(userId);

        if(!optional.isPresent()){
            throw new IllegalArgumentException();
        }

        benutzerRepository.delete(optional.get());
    }
    @GetMapping("/user/v1")
    public List<Benutzer> getUsers(@RequestHeader String token) {

        Benutzer benutzer = authService.checkToken(token);

        if (!benutzer.isIsadmin()) {
            throw new IllegalArgumentException();
        }

       List<Benutzer> alleBenutzer = FluentIterable.from(benutzerRepository.findAll()).toList();

       return alleBenutzer;
    }
}
