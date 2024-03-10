package com.raygak.server.smarthome;

import lombok.Getter;

@Getter
public class Window {
    private String name = "";
    private String position = "";
    private boolean isOpen = false;
    public Window(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public void openWindow() {
        this.isOpen = false;
    }

    public void closeWindow() {this.isOpen = true;}
}
