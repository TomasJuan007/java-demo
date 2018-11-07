package com.example.demo.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;

public final class EncryptionUtils {

    private static final String INVALID_CHAR = "\\s*|\t|\r|\n";
    private static final String ENCRYPTED = "encrypted:";
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtils.class);
    private static final String SECRET_KEY = "Blowfish";
    private static final int RADIX_NUMBER = 16;
    private static final int BYTE_LENGTH = 8;

    private EncryptionUtils(){

    }
    
    public static String encode(String secret) {
        if(secret == null || (StringUtils.EMPTY).equals(secret)){
            throw new IllegalArgumentException("encode text can not be empty");
        }
        byte[] kbytes = "jaas is the way".getBytes();
        SecretKeySpec key = new SecretKeySpec(kbytes, SECRET_KEY);
        try {
            Cipher cipher = Cipher.getInstance(SECRET_KEY);
            cipher.init(1, key);
            byte[] encoding = cipher.doFinal(secret.getBytes());
            BigInteger n = new BigInteger(encoding);
            return ENCRYPTED + n.toString(RADIX_NUMBER);
        } catch (Exception e) {
            LOGGER.error("Encode exception: {}", e);
            return secret;
        }

    }

    public static String decode(String secret) {
        if(secret == null || (StringUtils.EMPTY).equals(secret)){
            throw new IllegalArgumentException("decode text can not be empty");
        }
        if (!secret.contains(ENCRYPTED)) {
            return secret;
        }
        return doDecode(secret);
    }

    private static String doDecode(String secret) {
        String decodedSecret = secret.replace(ENCRYPTED, StringUtils.EMPTY);
        decodedSecret =  decodedSecret.trim().replaceAll( INVALID_CHAR, StringUtils.EMPTY );
        byte[] kbytes = "jaas is the way".getBytes();
        SecretKeySpec key = new SecretKeySpec(kbytes, SECRET_KEY);

        BigInteger n = new BigInteger(decodedSecret, RADIX_NUMBER);
        byte[] encoding = n.toByteArray();

        if (encoding.length % BYTE_LENGTH != 0) {
            int length = encoding.length;
            int newLength = (length / BYTE_LENGTH + 1) * BYTE_LENGTH;
            int pad = newLength - length;
            byte[] old = encoding;
            encoding = new byte[newLength];
            System.arraycopy(old, 0, encoding, pad, old.length);

            if (n.signum() == -1) {
                for (int i = 0; i < newLength - length; ++i) {
                    encoding[i] = -1;
                }
            }
        }

        try {
            Cipher cipher = Cipher.getInstance(SECRET_KEY);
            cipher.init(2, key);
            byte[] decode = cipher.doFinal(encoding);
            return new String(decode);
        } catch (Exception e) {
            LOGGER.error("Encode exception: {}", e);
            return decodedSecret;
        }
    }
}
