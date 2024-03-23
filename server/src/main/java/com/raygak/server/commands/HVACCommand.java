package com.raygak.server.commands;

import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.Room;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class HVACCommand extends Command {
    private House house;
    private String roomID;

    public HVACCommand(House house, String roomID) {
        this.house = house;
        this.roomID = roomID;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    @Override
    public void execute() {
        ArrayList<Room> rooms = this.house.getRooms();
        for (int i = 0;i < rooms.size();i++) {
            if (rooms.get(i).getRoomID().equals(this.roomID)) {
                Room r = rooms.get(i);
                if (r.isHVACOn()) {
                    r.turnOffHVAC();
                } else {
                    r.turnOnHVAC();
                }
                rooms.set(i, r);
                house.setRooms(rooms);
                return;
            }
        }
    }
}
