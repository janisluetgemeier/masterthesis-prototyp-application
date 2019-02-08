package de.luetgemeier.masterthesis.prototype.application.controllers;

import com.google.api.client.util.Lists;
import com.google.common.collect.FluentIterable;
import de.luetgemeier.masterthesis.prototype.application.entities.Benutzer;
import de.luetgemeier.masterthesis.prototype.application.entities.Datei;
import de.luetgemeier.masterthesis.prototype.application.entities.Link;
import de.luetgemeier.masterthesis.prototype.application.repositories.DateiRepository;
import de.luetgemeier.masterthesis.prototype.application.repositories.LinkRepository;
import de.luetgemeier.masterthesis.prototype.application.services.AuthService;
import de.luetgemeier.masterthesis.prototype.application.services.DateiService;
import io.minio.errors.*;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class DateiController {

    @Autowired
    private DateiService dateiService;

    @Autowired
    DateiRepository dateiRepository;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    private AuthService authService;

    @PostMapping("/datei/upload/v1")
    public Datei uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("token") String token) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {

        Benutzer benutzer = authService.checkToken(token);

        Datei datei = dateiService.storeFile(benutzer, file);

        return datei;
    }

    @GetMapping("/datei/v1")
    public List<Datei> getDateien(@RequestHeader("token") String token) {

        Benutzer benutzer = authService.checkToken(token);

        List<Datei> dateien;
        if(!benutzer.isIsadmin()) {
            dateien = dateiRepository.findByUserid(benutzer.getId());
        } else {
            dateien = FluentIterable.from(dateiRepository.findAll()).toList();
        }

        if (null == dateien || dateien.size() < 1) {
            dateien = Lists.newArrayList();
        }

        return dateien;
    }

    @DeleteMapping("/datei/download/v1/{fileId:.+}")
    public void getDateien(@PathVariable Integer fileId, @RequestHeader("token") String token) {

        Benutzer benutzer = authService.checkToken(token);

        Datei datei = dateiRepository.findById(fileId).get();

        if (null == benutzer) {
            throw new IllegalArgumentException();
        }

        if (datei == null) {
            throw new IllegalArgumentException();
        }

        if (benutzer.getId() != datei.getUserid()) {
            if (!benutzer.isIsadmin())
                throw new IllegalArgumentException();
        }

        dateiRepository.delete(datei);

    }

    @GetMapping("/datei/download/v1/{linkHash:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String linkHash) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {

        Link link = linkRepository.findByHash(linkHash);

        if (null == link) {
            throw new IllegalArgumentException();
        }


        Datei datei = dateiRepository.findById(link.getFileid()).get();

        if (null == datei) {
            throw new IllegalArgumentException();
        }


        InputStreamResource inputStreamResource = dateiService.getFile(datei.getStoredfilename());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + datei.getOriginalfilename() + "\"").body(inputStreamResource);
    }

    @GetMapping("/datei/download/byId/v1/{fileId:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFileById(@PathVariable Integer fileId, @RequestParam("token") String token) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {

        Benutzer benutzer = authService.checkToken(token);


        Datei datei = dateiRepository.findById(fileId).get();
        if (null == datei) {
            throw new IllegalArgumentException();
        }

        if (benutzer.getId() != datei.getUserid()) {
            throw new IllegalArgumentException();
        }


        InputStreamResource inputStreamResource = dateiService.getFile(datei.getStoredfilename());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + datei.getOriginalfilename() + "\"").body(inputStreamResource);
    }

    @PostMapping("/datei/v1/{fileId:.+}")
    public void deleteFile(@PathVariable Integer fileId, @RequestHeader("token") String token) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {

        Benutzer benutzer = authService.checkToken(token);


        Datei datei = dateiRepository.findById(fileId).get();
        if (null == datei) {
            throw new IllegalArgumentException();
        }

        if(!benutzer.isIsadmin()) {
            if (benutzer.getId() != datei.getUserid()) {
                throw new IllegalArgumentException();
            }
        }

        dateiRepository.delete(datei);
        Link link = linkRepository.findByFileid(datei.getId());
        if(null != link) {
            linkRepository.delete(link);
        }
        dateiService.deleteFile(datei.getStoredfilename());
    }
}
