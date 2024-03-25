package com.raygak.server.commands;

import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.Room;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class TurnOnHVACCommand extends Command {
    private House house;
    private String roomID;

    public TurnOnHVACCommand(House house, String roomID) {
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
            Room r = rooms.get(i);
            if (r.getRoomID().equals(roomID)) {
                if (r.isHVACOn()) {
                    this.house.getShh().HVACErrorUpdate(r.getRoomID(), "Already On");
                } else {
                    r.turnOnHVAC();
                }
                rooms.set(i, r);
                this.house.setRooms(rooms);
            }
            return;
        }
    }
}
