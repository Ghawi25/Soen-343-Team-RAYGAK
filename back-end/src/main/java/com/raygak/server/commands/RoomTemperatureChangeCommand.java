package com.raygak.server.commands;

import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.Room;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class RoomTemperatureChangeCommand extends Command{
    private House house;
    private String roomID;
    private double newTemperature;

    public RoomTemperatureChangeCommand(House house, String roomID, double newTemperature) {
        this.house = house;
        this.roomID = roomID;
        this.newTemperature = newTemperature;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setNewTemperature(double newTemperature) {
        this.newTemperature = newTemperature;
    }

    @Override
    public void execute() {
        ArrayList<Room> rooms = this.house.getRooms();
        for (int i = 0;i < rooms.size();i++) {
            Room r = rooms.get(i);
            if (r.getRoomID().equals(roomID)) {
                r.setCurrentTemperature(newTemperature);
                rooms.set(i, r);
                this.house.setRooms(rooms);
            }
            return;
        }
    }
}
