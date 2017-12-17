package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

public interface IConnection extends Remote {

    PublicKey getPublicKey() throws RemoteException;
    String getToken(String id, PublicKey key) throws RemoteException;
    void sendData(String payload, String token) throws RemoteException;

}
