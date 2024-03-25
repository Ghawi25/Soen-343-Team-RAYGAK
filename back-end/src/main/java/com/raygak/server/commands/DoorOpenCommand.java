package com.raygak.server.commands;

import com.raygak.server.smarthome.Door;
import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.Room;
import com.raygak.server.smarthome.Window;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class DoorOpenCommand extends Command {
    private House house;
    private String doorName;

    public DoorOpenCommand(House house, String doorName) {
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
        ArrayList<Room> rooms = this.house.getRooms();
        for (int i = 0;i < rooms.size();i++) {
            Room r = rooms.get(i);
            ArrayList<Door> currentRoomDoors = r.getDoors();
            for (int j = 0; j < currentRoomDoors.size(); j++) {
                Door d = currentRoomDoors.get(j);
                if (d.getName().equals(doorName)) {
                    d.openDoor();
                    currentRoomDoors.set(j, d);
                    r.setDoors(currentRoomDoors);
                    rooms.set(i, r);
                    this.house.setRooms(rooms);
                    ArrayList<Door> houseDoors = this.house.getDoors();
                    for (int k = 0;k < houseDoors.size();k++) {
                        if (houseDoors.get(k).getName().equals(doorName)) {
                            houseDoors.set(k, d);
                        }
                        break;
                    }
                    this.house.setDoors(houseDoors);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Error: No door with the name " + doorName + " exists.");
    }
}
