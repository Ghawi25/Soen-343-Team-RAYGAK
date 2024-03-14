package com.raygak.server.commands;

import com.raygak.server.smarthome.Door;
import com.raygak.server.smarthome.House;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class DoorCommand extends Command {
    private House house;
    private String doorName;

    public DoorCommand(House house, String doorName) {
        this.house = house;
        this.doorName = doorName;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    @Override
    public void execute() {
        ArrayList<Door> doors = this.house.getDoors();
        for (int i = 0;i < doors.size();i++) {
            if (doors.get(i).getName().equals(doorName)) {
                Door d = doors.get(i);
                if (d.isOpen()) {
                    d.closeDoor();
                } else {
                    d.openDoor();
                }
                doors.set(i, d);
                house.setDoors(doors);
                break;
            }
        }
    }
}
