package de.luetgemeier.masterthesis.prototype.application.services;

import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Profile("minio")
@Slf4j
public class MinioService implements FileStorageService, InitializingBean {

    @Value( "${de.luetgemeier.masterthesis.prototype.application.storage.minio.url}" )
    private String url;

    @Value( "${de.luetgemeier.masterthesis.prototype.application.storage.minio.accesskey}" )
    private String accesskey;

    @Value( "${de.luetgemeier.masterthesis.prototype.application.storage.minio.secretkey}" )
    private String secretkey;

    @Value( "${de.luetgemeier.masterthesis.prototype.application.storage.minio.bucketname}" )
    private String bucketname;

    MinioClient minioClient;

    @Override
    public String uploadFile(String filename, MultipartFile file) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {

        InputStream targetStream = file.getInputStream();
        minioClient.putObject(bucketname, filename, targetStream, file.getContentType());
        return "ok";
    }

    @Override
    public void deleteFile(String identifier) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {

        minioClient.removeObject(bucketname, identifier);

    }

    @Override
    public InputStream getFile(String storedFilename) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return minioClient.getObject(bucketname, storedFilename);

    }

    public void afterPropertiesSet() throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException {

        int tries = 0;

        while(tries < 5) {
            tries++;
            log.info("Trying to establish Minio Connection. Try number " + tries);
            try {
                Thread.sleep(2000);
                minioClient = new MinioClient(url, accesskey, secretkey);

                //Making test put:
                InputStream stream = new ByteArrayInputStream("abcd".getBytes(StandardCharsets.UTF_8));
                minioClient.makeBucket("test");
                minioClient.putObject("test", "test", stream, stream.available(), "application/octet-stream");
                log.info("Minio Connection was successful. This was try number " + tries + ".");
                break;
            } catch (Exception e) {
                log.warn("Minio Connection not successful with E = " + e.getClass().toString() + ". This was try number " + tries + ".");
                log.warn("Error msg is: " + e.getMessage());
            }
        }


        boolean isExist = minioClient.bucketExists(bucketname);
        if(!isExist) {
            minioClient.makeBucket(bucketname);
        }


    }
}
