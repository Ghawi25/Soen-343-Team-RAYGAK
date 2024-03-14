package com.raygak.server.smarthome;

import lombok.Getter;

@Getter
public class Light {
    private String name = "";
    private String position = "";
    private boolean isOn = false;

    public Light(String name, String position) {
        this.name = name;
        this.position = position;
    }

    // Use case 18 - turn on off light
    public void turnOff() {
        this.isOn = false;
    }

    public void turnOn() {this.isOn = true;}
}
