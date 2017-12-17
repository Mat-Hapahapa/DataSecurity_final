package Server;

import Cryptography.AsymmetricCryptography;
import Interfaces.IConnection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ServerManager extends UnicastRemoteObject implements IConnection {

    private static String PUBLICKEYPATH = "KeyStorage/publicKey";
    private static String PRIVATEKEYPATH = "KeyStorage/privateKey";

    private AsymmetricCryptography ac;

    protected ServerManager() throws RemoteException {
        super();
        ac = new AsymmetricCryptography();
    }


    @Override
    public PublicKey getPublicKey() {
        return getPublic(PUBLICKEYPATH);
    }

    @Override
    public String getToken(String id, PublicKey key) {
        String sensorID = ac.decryptText(id, getPrivate(PRIVATEKEYPATH));
        //TODO: Match sensorID with enrolled sensors
        return ac.encryptText(sensorID, key);
    }

    @Override
    public void sendData(String payload, String token) {
        System.out.println(ac.decryptText(payload,getPrivate(PRIVATEKEYPATH)));
    }


    // https://docs.oracle.com/javase/8/docs/api/java/security/spec/PKCS8EncodedKeySpec.html
    private PrivateKey getPrivate(String filename) {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html
    private PublicKey getPublic(String filename) {
        byte[] keyBytes ;//= new byte[0];
        try {
            keyBytes = Files.readAllBytes(new File(filename).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }

    }

}
