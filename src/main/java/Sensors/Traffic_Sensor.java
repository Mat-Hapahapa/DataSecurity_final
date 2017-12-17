package Sensors;

import Cryptography.AsymmetricCryptography;
import Cryptography.KeyGen;
import Interfaces.IConnection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Traffic_Sensor {

    private static IConnection connection;
    private static AsymmetricCryptography AC;

    private static PublicKey serverPublicKey;
    private static PublicKey sensorPublicKey;
    private static PrivateKey sensorPrivateKey;

    private static String sensorID = "Traffic_Sensor_1";

    public static void main(String[] args) throws RemoteException {

        //Create public and private key pair for sensor
        initKeys();

        //Get remote object from server
        try {
            connection = (IConnection) Naming.lookup("rmi://localhost:5099/server");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        //Get server's public key
        serverPublicKey = connection.getPublicKey();

        //Encrypt and send sensor ID with sensor's public key
        AC = new AsymmetricCryptography();
        String enryptedID = AC.encryptText(sensorID, serverPublicKey);
        String encryptedToken = connection.getToken(enryptedID, sensorPublicKey);

        //Decrypt unique token
        AC.decryptText(encryptedToken, sensorPrivateKey);

        //Prepare message and token by enrypting them with the server's public key
        String enryptedData = AC.encryptText("", serverPublicKey);
        String encryptedServerToken = AC.encryptText("",serverPublicKey);

        //Encrypt and send data
        connection.sendData(enryptedData, encryptedServerToken);

    }

    private static void initKeys(){
        KeyGen gk;
        try {
            gk = new KeyGen(1024);
            gk.createKeys();
            sensorPublicKey = gk.getPublicKey();
            sensorPrivateKey = gk.getPrivateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            System.err.println(e.getMessage());
        }
    }
}
