package com.example.blesenarios;

import org.jetbrains.annotations.NotNull;

public class BLEDevice {
    private String name;
    private String mac;
    private String rssi;

    private String getRssi() {
        return rssi;
    }

    void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String getMac() {
        return mac;
    }

    void setMac(String mac) {
        this.mac = mac;
    }

    @NotNull
    public String toString(){
        return getName()+"\n"+getMac()+"\n"+getRssi();
    }
}
