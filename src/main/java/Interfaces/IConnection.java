package Interfaces;

public interface IConnection {

    String getPublicKey();
    boolean connect(byte[] payload);
    void sendData(byte[] payload);

}
