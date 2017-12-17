package Sensors;

import Cryptography.AsymmetricCryptography;
import Cryptography.KeyGen;
import Cryptography.SymmetricCryptography;
import Datamodels.TrafficSensorModel;
import Interfaces.IConnection;

import java.awt.datatransfer.StringSelection;
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
    private static SymmetricCryptography SC;

    private static PublicKey serverPublicKey;
    private static PublicKey sensorPublicKey;
    private static PrivateKey sensorPrivateKey;
    private static String encryptedUniqueToken;
    private static String uniqueToken;

    private static TrafficSensorModel data;
    private static String sensorID = "Traffic_Sensor_1";

    public static void main(String[] args) throws RemoteException {

        //Create public and private key pair for sensor
        initKeys();

        collectData();

        //Get remote object from server
        try {
            connection = (IConnection) Naming.lookup("rmi://localhost:5099/server");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        authenticate();

        //Encrypt data with symmetric key and send
        SC = new SymmetricCryptography();
        byte[] message = SC.encryptText("data",uniqueToken.getBytes());
        connection.sendData(message);

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

    private static void authenticate() throws RemoteException {
        //Get server's public key
        serverPublicKey = connection.getPublicKey();

        //Encrypt and send sensor ID with sensor's public key
        AC = new AsymmetricCryptography();
        String enryptedID = AC.encryptText(sensorID, serverPublicKey);

        //Request encrypted token (symmetric)
        encryptedUniqueToken = connection.getToken(enryptedID, sensorPublicKey);

        //Decrypt symmetric
        uniqueToken = AC.decryptText(encryptedUniqueToken, sensorPrivateKey);
    }

    private static void collectData(){
        data.setSensorID(sensorID);
        data.setCarCount(42);
    }
}
