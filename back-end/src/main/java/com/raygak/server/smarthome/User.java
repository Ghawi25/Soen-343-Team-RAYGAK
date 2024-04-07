package com.raygak.server.smarthome;

import com.raygak.server.smarthome.heating.TemperatureSetting;
import com.raygak.server.smarthome.heating.Zone;
import com.raygak.server.smarthome.heating.ZoneType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {

    private String id;
    private String username;
    private String password;
    private UserType userType; // adult, child, guest
    private Room currentRoom = null;
    private House associatedHouse = null;

    // Constructors
    public User() {}

    public User(String username, String password, UserType userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room newCurrentRoom) {
        this.currentRoom = newCurrentRoom;
    }

    public House getAssociatedHouse() {
        return associatedHouse;
    }

    public void setAssociatedHouse(House newHouse) {
        this.associatedHouse = newHouse;
    }

    public void changeTemperatureInRoom_Remote(String roomID, double newTemperature) {
        if (this.userType != UserType.PARENT) {
            System.out.println("Error: Only a parent can remotely change a room's temperature.");
            return;
        }
        ArrayList<Room> houseRooms = this.associatedHouse.getRooms();
        for (int i = 0;i < houseRooms.size();i++) {
            Room room = houseRooms.get(i);
            if (room.getRoomID().equals(roomID)) {
                double oldTemperature = room.getCurrentTemperature();
                room.setCurrentTemperature(newTemperature, true);
                double temperatureAfter = room.getCurrentTemperature();
                houseRooms.set(i, room);
                this.associatedHouse.setRooms(houseRooms);
                this.associatedHouse.getAssociatedSimulator().getLogger().temperatureUpdateLog(room.getRoomID(), oldTemperature, temperatureAfter, this.associatedHouse.getShh().getIsOn(),"A person has manually changed the temperature of a room in remote fashion.", this.username);
            }
        }
    }

    public void changeTemperatureInCurrentRoom_Local(double newTemperature) {
        if (this.userType == UserType.STRANGER) {
            System.out.println("Error: Only a parent, child or guest can change the temperature of a room which they are in.");
            return;
        }
        this.currentRoom.setCurrentTemperature(newTemperature, true);
        ArrayList<Room> houseRooms = this.associatedHouse.getRooms();
        for (int i = 0;i < houseRooms.size();i++) {
            Room room = houseRooms.get(i);
            if (room.getRoomID().equals(this.currentRoom.getRoomID())) {
                double oldTemperature = room.getCurrentTemperature();
                room.setCurrentTemperature(newTemperature, true);
                double temperatureAfter = room.getCurrentTemperature();
                houseRooms.set(i, room);
                this.associatedHouse.setRooms(houseRooms);
                this.associatedHouse.getAssociatedSimulator().getLogger().temperatureUpdateLog(room.getRoomID(), oldTemperature, temperatureAfter, this.associatedHouse.getShh().getIsOn(), "A person has manually changed the temperature of a room from within it.", this.username);
            }
        }
    }

    public void setUpZone(House house, String zoneID, ZoneType type, ArrayList<TemperatureSetting> settingList, ArrayList<Room> roomList) {
        if (this.userType != UserType.PARENT) {
            System.out.println("Error: Only a parent can set up a heating/cooling zone within the home.");
            return;
        }
        if (house.doesZoneWithIDExist(zoneID)) {
            house.changeZone(zoneID, type, settingList, roomList);
        } else {
            house.addZone(new Zone(zoneID, type, settingList, roomList));
        }
    }

    public void turnOnSHH(House house) {
        if (this.userType != UserType.PARENT) {
            System.out.println("Error: Only a parent can turn on the SHH system within a house.");
            return;
        }
        house.turnOnSHH();
    }

    public void turnOffSHH(House house) {
        if (this.userType != UserType.PARENT) {
            System.out.println("Error: Only a parent can turn off the SHH system within a house.");
            return;
        }
        house.turnOnSHH();
    }

    public void setUpTemperatureSettingWithinZone(Zone zone, String settingID, double tempInput, int hours1, int minutes1, int hours2, int minutes2) {
        if (this.userType != UserType.PARENT) {
            System.out.println("Error: Only a parent can create or modify temperature settings for zones.");
            return;
        }
        if (zone.doesSettingWithIDExist(settingID)) {
            zone.setSetting(settingID, tempInput, hours1, minutes1, hours2, minutes2);
        }
        else {
            zone.addSetting(new TemperatureSetting(settingID, tempInput, hours1, minutes1, hours2, minutes2));
        }
    }
}