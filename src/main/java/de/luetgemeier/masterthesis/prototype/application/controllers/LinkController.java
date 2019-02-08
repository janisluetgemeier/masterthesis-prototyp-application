package de.luetgemeier.masterthesis.prototype.application.controllers;

import de.luetgemeier.masterthesis.prototype.application.entities.Benutzer;
import de.luetgemeier.masterthesis.prototype.application.entities.Datei;
import de.luetgemeier.masterthesis.prototype.application.entities.Link;
import de.luetgemeier.masterthesis.prototype.application.repositories.DateiRepository;
import de.luetgemeier.masterthesis.prototype.application.services.AuthService;
import de.luetgemeier.masterthesis.prototype.application.services.DateiService;
import de.luetgemeier.masterthesis.prototype.application.services.LinkService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class LinkController {
    @Autowired
    private AuthService authService;

    @Autowired
    private DateiRepository dateiRepository;

    @Autowired
    private LinkService linkService;

    @PostMapping("/link/file/v1/{fileId}")
    public Link createLinkForFile(@PathVariable("fileId") Integer fileId, @RequestHeader("token") String token, HttpServletRequest req) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {

        Benutzer benutzer = authService.checkToken(token);


        Datei datei = dateiRepository.findById(fileId).get();


        if(benutzer.getId() != datei.getUserid()){
            throw new IllegalArgumentException();
        }

        Link link = linkService.createLink(datei, benutzer);

        return link;
    }
}
