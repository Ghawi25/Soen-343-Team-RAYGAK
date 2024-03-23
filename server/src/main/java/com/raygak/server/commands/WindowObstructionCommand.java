package com.raygak.server.commands;

import com.raygak.server.smarthome.Door;
import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.Window;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class WindowObstructionCommand extends Command {
    private House house;
    private String windowID;

    public WindowObstructionCommand(House house, String windowID) {
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
        if (w.isObstructed()) {
            this.house.unobstructWindowWithID(this.windowID);
        } else {
            this.house.obstructWindowWithID(this.windowID);
        }
    }
}
