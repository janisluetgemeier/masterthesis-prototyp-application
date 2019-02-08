package de.luetgemeier.masterthesis.prototype.application.services;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

@Service
public class PasswordService {


    public String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {


        byte[] bytesOfMessage = password.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);

        StringBuilder buff = new StringBuilder();
        for (byte b : thedigest) {
            String conversion = Integer.toString(b & 0xFF,16);
            while (conversion.length() < 2) {
                conversion = "0" + conversion;
            }
            buff.append(conversion);
        }
        String str1 = buff.toString();

        return  str1;



    }

    public String createToken(){
        byte[] array = new byte[32];
        new Random().nextBytes(array);

        StringBuilder buff = new StringBuilder();
        for (byte b : array) {
            String conversion = Integer.toString(b & 0xFF,16);
            while (conversion.length() < 2) {
                conversion = "0" + conversion;
            }
            buff.append(conversion);
        }
        String token = buff.toString();

        return token;
    }
}
