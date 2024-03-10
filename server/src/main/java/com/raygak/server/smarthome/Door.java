package com.raygak.server.smarthome;

import lombok.Getter;

@Getter
public class Door {
    private String name = "";
    private String position = "";
    private boolean isOpen = false;
    public Door(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public void openDoor() {
        this.isOpen = false;
    }

    public void closeDoor() {this.isOpen = true;}
}
