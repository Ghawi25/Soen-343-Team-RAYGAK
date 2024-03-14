package com.raygak.server.commands;

import com.raygak.server.smarthome.Door;
import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.Light;
import com.raygak.server.smarthome.Room;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class SwitchLightCommand extends Command {
    private House house;
    private String lightName;

    public SwitchLightCommand(House house, String lightName) {
        this.house = house;
        this.lightName = lightName;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public void setLightName(String lightName) {
        this.lightName = lightName;
    }

    @Override
    public void execute() {
        ArrayList<Room> rooms = this.house.getRooms();
        for (int i = 0;i < rooms.size();i++) {
            if (rooms.get(i).getLight().getName().equals(this.lightName)) {
                Room r = rooms.get(i);
                Light l = r.getLight();
                if (l.isOn()) {
                    r.turnOffLight();
                } else {
                    r.turnOnLight();
                }
                rooms.set(i, r);
                house.setRooms(rooms);
                break;
            }
        }
    }
}
