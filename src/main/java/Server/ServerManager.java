package Server;

import Interfaces.IConnection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerManager extends UnicastRemoteObject implements IConnection {

    protected ServerManager() throws RemoteException {
        super();
    }


    @Override
    public String getPublicKey() {
        return null;
    }

    @Override
    public boolean connect(byte[] payload) {
        return false;
    }

    @Override
    public void sendData(byte[] payload) {

    }
}
