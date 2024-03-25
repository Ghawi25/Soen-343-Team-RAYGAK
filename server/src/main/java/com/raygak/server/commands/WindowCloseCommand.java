package com.raygak.server.commands;

import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.Room;
import com.raygak.server.smarthome.Window;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class WindowCloseCommand extends Command {
    private House house;
    private String windowID;

    public WindowCloseCommand(House house, String windowID) {
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
        ArrayList<Room> rooms = this.house.getRooms();
        for (int i = 0;i < rooms.size();i++) {
            Room r = rooms.get(i);
            ArrayList<Window> currentRoomWindows = r.getWindows();
            for (int j = 0; j < currentRoomWindows.size(); j++) {
                Window w = currentRoomWindows.get(j);
                if (w.getWindowID().equals(windowID)) {
                    if (w.isOpen() == false) {
                        this.house.getShh().windowErrorUpdate(windowID, "Already Closed");
                    }
                    else if (w.isObstructed()) {
                        this.house.getShh().windowErrorUpdate(windowID, "Obstructed, Cannot Close");
                    }
                    else {
                        w.close();
                        currentRoomWindows.set(j, w);
                        r.setWindows(currentRoomWindows);
                        rooms.set(i, r);
                        this.house.setRooms(rooms);
                        ArrayList<Window> houseWindows = this.house.getWindows();
                        for (int k = 0;k < houseWindows.size();k++) {
                            if (houseWindows.get(k).getWindowID().equals(windowID)) {
                                houseWindows.set(k, w);
                            }
                            break;
                        }
                        this.house.setWindows(houseWindows);
                        return;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Error: No window with the ID " + windowID + " exists.");
    }
}
