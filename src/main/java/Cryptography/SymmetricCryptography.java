package Cryptography;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

//https://www.mkyong.com/java/java-symmetric-key-cryptography-example/
public class SymmetricCryptography {


    public byte[] generateSecretKey(String secret) {
        SecretKey secretKey = null;
        byte[] key;
        try {
            key = fixSecret(secret, 32);
            secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
        } catch (UnsupportedEncodingException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return secretKey.getEncoded();
    }

    private byte[] fixSecret(String s, int length) throws UnsupportedEncodingException {
        if (s.length() < length) {
            int missingLength = length - s.length();
            for (int i = 0; i < missingLength; i++) {
                s += " ";
            }
        }
        return s.substring(0, length).getBytes("UTF-8");
    }

    public byte[] encryptText(String text, byte[] secretKey) {

        byte[] tmp = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,  new SecretKeySpec(secretKey, 0, secretKey.length, "AES"));
            tmp = cipher.doFinal(text.getBytes());
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public String decryptText(byte[] text, byte[] secretKey){
        byte[] tmp = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, 0, secretKey.length, "AES"));
            tmp = cipher.doFinal(text);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new String(tmp);

    }
}

