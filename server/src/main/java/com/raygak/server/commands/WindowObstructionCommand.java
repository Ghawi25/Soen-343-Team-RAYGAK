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
        ArrayList<Window> windows = this.house.getWindows();
        for (int i = 0;i < windows.size();i++) {
            if (windows.get(i).getWindowID().equals(windowID)) {
                Window w = windows.get(i);
                if (w.isObstructed()) {
                    w.unobstruct();
                } else {
                    w.obstruct();
                }
                windows.set(i, w);
                house.setWindows(windows);
                break;
            }
        }
    }
}
