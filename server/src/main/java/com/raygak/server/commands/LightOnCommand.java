package com.raygak.server.commands;

import com.raygak.server.smarthome.Door;
import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.Light;
import com.raygak.server.smarthome.Room;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class LightOnCommand extends Command {
    private House house;
    private String lightName;

    public LightOnCommand(House house, String lightName) {
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
            Room r = rooms.get(i);
            Light l = r.getLight();
            if (l.getName().equals(lightName)) {
                l.turnOn();
                r.setLight(l);
                rooms.set(i, r);
                this.house.setRooms(rooms);
                ArrayList<Light> houseLights = this.house.getLights();
                for (int j = 0;j < houseLights.size();j++) {
                    if (houseLights.get(j).getName().equals(lightName)) {
                        houseLights.set(j, l);
                    }
                    break;
                }
                this.house.setLights(houseLights);
                return;
            }
        }
        throw new IllegalArgumentException("Error: No light with the name " + lightName + " exists.");
    }
}
