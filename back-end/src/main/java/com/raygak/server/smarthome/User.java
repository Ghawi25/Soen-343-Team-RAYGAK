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

    public void changeTemperatureInCurrentRoom_Remote(double newTemperature) throws IllegalAccessException {
        if (this.userType != UserType.PARENT) {
            throw new IllegalAccessException("Error: Only a parent can remotely change a room's temperature.");
        }
        this.currentRoom.setCurrentTemperature(newTemperature);
    }

    public void changeTemperatureInCurrentRoom_Local(double newTemperature) throws IllegalAccessException {
        if (this.userType == UserType.STRANGER) {
            throw new IllegalAccessException("Error: Only a parent, child and guest can change the temperature of a room which they are in.");
        }
        this.currentRoom.setCurrentTemperature(newTemperature);
    }

    public void setUpZone(House house, String zoneID, ZoneType type, ArrayList<TemperatureSetting> settingList, ArrayList<Room> roomList) throws IllegalAccessException {
        if (this.userType != UserType.PARENT) {
            throw new IllegalAccessException("Error: Only a parent can set up a heating/cooling zone within the home.");
        }
        if (house.doesZoneWithIDExist(zoneID)) {
            house.changeZone(zoneID, type, settingList, roomList);
        } else {
            house.addZone(new Zone(zoneID, type, settingList, roomList));
        }
    }

    public void turnOnSHH(House house) throws IllegalAccessException {
        if (this.userType != UserType.PARENT) {
            throw new IllegalAccessException("Error: Only a parent can turn on the SHH system within a house.");
        }
        house.turnOnSHH();
    }

    public void turnOffSHH(House house) throws IllegalAccessException {
        if (this.userType != UserType.PARENT) {
            throw new IllegalAccessException("Error: Only a parent can turn off the SHH system within a house.");
        }
        house.turnOnSHH();
    }

    public void setUpTemperatureSettingWithinZone(Zone zone, String settingID, double tempInput, int hours1, int minutes1, int hours2, int minutes2) throws IllegalAccessException {
        if (this.userType != UserType.PARENT) {
            throw new IllegalAccessException("Error: Only a parent can create or modify temperature settings for zones.");
        }
        if (zone.doesSettingWithIDExist(settingID)) {
            zone.setSetting(settingID, tempInput, hours1, minutes1, hours2, minutes2);
        }
        else {
            zone.addSetting(new TemperatureSetting(settingID, tempInput, hours1, minutes1, hours2, minutes2));
        }
    }
}