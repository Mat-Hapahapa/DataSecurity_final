package Server;

import Cryptography.AsymmetricCryptography;
import Cryptography.KeyGen;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Main {

    public static void main(String[] args) throws RemoteException {

        generateKeys();

        testfunc();

        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("server", new ServerManager());
        System.out.println("Server started");
    }

    private static void generateKeys(){
        KeyGen gk;
        try {
            gk = new KeyGen(1024);
            gk.createKeys();
            gk.writeToFile("KeyStorage/publicKey", gk.getPublicKey().getEncoded());
            gk.writeToFile("KeyStorage/privateKey", gk.getPrivateKey().getEncoded());
        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void testfunc(){
        AsymmetricCryptography ac = null;
        try {
            ac = new AsymmetricCryptography();
            PrivateKey privateKey = ac.getPrivate("KeyStorage/privateKey");
            PublicKey publicKey = ac.getPublic("KeyStorage/publicKey");

            String msg = "Cryptography is fun!";
            String encrypted_msg = ac.encryptText(msg, privateKey);
            //String decrypted_msg = ac.decryptText(encrypted_msg, publicKey);
            System.out.println("Original Message: " + msg +
                    "\nEncrypted Message: " + encrypted_msg
                    /*+ "\nDecrypted Message: " + decrypted_msg*/);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
