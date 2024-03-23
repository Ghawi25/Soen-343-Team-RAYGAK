package com.raygak.server.commands;

import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.Window;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class WindowOpenCommand extends Command {
    private House house;
    private String windowID;

    public WindowOpenCommand(House house, String windowID) {
        this.house = house;
        this.windowID = windowID;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public void setWindowID(String windowID) {
        this.windowID = windowID;
    }

    @Override
    public void execute() {
        Window w = this.house.getWindowByID(this.windowID);
        if (w.isOpen()) {
            this.house.closeWindowWithID(this.windowID);
        } else {
            this.house.openWindowWithID(this.windowID);
        }
    }
}
