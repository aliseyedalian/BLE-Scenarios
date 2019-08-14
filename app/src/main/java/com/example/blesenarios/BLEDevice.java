package com.example.blesenarios;

public class BLEDevice {
    private String name;
    private String mac;
    private String rssi;

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String toString(){
        return getName()+"\n"+getMac()+"\n"+getRssi();
    }
}
