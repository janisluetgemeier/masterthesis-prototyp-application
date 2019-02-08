package de.luetgemeier.masterthesis.prototype.application.services;

import de.luetgemeier.masterthesis.prototype.application.entities.Benutzer;
import de.luetgemeier.masterthesis.prototype.application.entities.Datei;
import de.luetgemeier.masterthesis.prototype.application.repositories.DateiRepository;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Service
@Configuration
public class DateiService {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DateiRepository dateiRepository;

    public Datei storeFile(Benutzer benutzer, MultipartFile file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {

        String storedFilename = benutzer.getId() + "-" + file.getOriginalFilename();

        fileStorageService.uploadFile(storedFilename, file);

        Datei datei = Datei.builder().originalfilename(file.getOriginalFilename()).userid(benutzer.getId()).storedfilename(storedFilename).build();
        dateiRepository.save(datei);
        return datei;
    }

    public InputStreamResource getFile(String storedFilename) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {

        InputStream is = fileStorageService.getFile(storedFilename);

        InputStreamResource isr = new InputStreamResource(is);

        return isr;
    }

    public void deleteFile(String storedFileName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        fileStorageService.deleteFile(storedFileName);
    }

    @Bean(initMethod="start",destroyMethod="stop")
    public org.h2.tools.Server h2WebConsoleServer () throws SQLException {
        return org.h2.tools.Server.createWebServer("-web","-webAllowOthers","-webDaemon","-webPort", "8080");
    }

}
