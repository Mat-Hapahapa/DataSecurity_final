package Datamodels;

import java.security.PrivateKey;

public class TrafficSensorModel {
    private String sensorID;
    private int CarCount;

    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public int getCarCount() {
        return CarCount;
    }

    public void setCarCount(int carCount) {
        CarCount = carCount;
    }
}
