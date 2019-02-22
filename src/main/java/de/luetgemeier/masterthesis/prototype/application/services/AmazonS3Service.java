package de.luetgemeier.masterthesis.prototype.application.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Slf4j
@Service
@Profile("s3")
public class AmazonS3Service implements IFileStorageService, InitializingBean {

    @Value("${de.luetgemeier.masterthesis.prototype.application.storage.s3.bucketname}")
    private String bucketname;

    @Value("${de.luetgemeier.masterthesis.prototype.application.storage.s3.url}")
    private String url;

    @Value("${de.luetgemeier.masterthesis.prototype.application.storage.s3.accesskey}")
    private String accesskey;

    @Value("${de.luetgemeier.masterthesis.prototype.application.storage.s3.secretkey}")
    private String secretkey;

    private AmazonS3 client;


    @Override
    public String uploadFile(String filename, MultipartFile file) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        client.putObject(bucketname, filename, file.getInputStream(), new ObjectMetadata());
        return "ok";
    }

    @Override
    public void deleteFile(String identifier) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {

        client.deleteObject(bucketname, identifier);
    }

    @Override
    public InputStream getFile(String storedFilename) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {

        return client.getObject(bucketname, storedFilename).getObjectContent();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        AWSCredentials creds = new BasicAWSCredentials(accesskey, secretkey);

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).build();
        Bucket b = null;
        if (s3.doesBucketExist(bucketname)) {
            log.info("Bucket for name {} already exists.", bucketname);
        } else {
            try {
                b = s3.createBucket(bucketname);
            } catch (AmazonS3Exception e) {
                log.error("Error during creation of bucket.");
            }
        }
    }
}
