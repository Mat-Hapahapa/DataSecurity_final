package Sensors;

import Interfaces.IConnection;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Traffic_Sensor {

    private static IConnection connection;


    public static void main(String[] args){
        try {
            connection = (IConnection) Naming.lookup("rmi://localhost:5099/server");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //connection.connect();

    }
}
