package com.raygak.server.states;

import com.raygak.server.smarthome.*;
import com.raygak.server.smarthome.heating.*;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

@Getter
public abstract class State {
    protected Simulator simulator;

    public State(Simulator simulatorInput) {
        this.simulator = simulatorInput;
    }
    public abstract void turnOnSimulator();
    public abstract void turnOffSimulator();
    public abstract void setSimulationDate(Date newDate);
    public abstract void setSimulationDate(int newDay, int newMonth, int newYear);
    public abstract void setCurrentTime(LocalTime newTime);
    public abstract void setCurrentTime(int newHours, int newMinutes);
    public abstract void setTimeSpeed(double newTimeSpeed);
    public abstract void setHouse(House newHouse);
    public abstract void setCurrentUser(User newUser);
    public abstract void addRoom(Room newRoom);
    public abstract void removeRoom(String roomID);
    public abstract void addZone(Zone newZone);
    public abstract void removeZone(String zoneID);
    public abstract void addInhabitantToRoom(User newInhabitant, String inhabitedRoomID);
    public abstract void removeInhabitant(String inhabitantUsername);
    public abstract void moveInhabitantToRoom(User inhabitant, String newRoomID);
    public abstract void setIndoorTemperature(double temperatureInput);
    public abstract void setOutdoorTemperature(double temperatureInput);
    public abstract void updateAllRoomTemperatures();
    public abstract void displayTemperatureInRoomWithID(String roomID);
    public abstract void setCurrentSeason(Season newSeason);
    public abstract void changeZone(String zoneID, ZoneType type, ArrayList<TemperatureSetting> settingList, ArrayList<Room> roomList);
    public abstract void changeTemperatureInRoom_Remote(String roomID, double newTemperature);
    public abstract void changeTemperatureInCurrentRoom_Local(double newTemperature);
    public abstract void setUpZone(House house, String zoneID, ZoneType type, ArrayList<TemperatureSetting> settingList, ArrayList<Room> roomList);
    public abstract void turnOnSHH();
    public abstract void turnOffSHH();
    public abstract void openDoorWithName(String doorName);

    public abstract void closeDoorWithName(String doorName);

    public abstract void openWindowWithID(String windowID);

    public abstract void closeWindowWithID(String windowID);
    public abstract void obstructWindowWithID(String windowID);

    public abstract void unobstructWindowWithID(String windowID);

    public abstract void turnOnHVACInRoomWithID(String roomID);

    public abstract void turnOffHVACInRoomWithID(String roomID);

    public abstract void turnOnLightWithName(String lightName);

    public abstract void turnOffLightWithName(String lightName);

    public abstract void turnOnSHP();

    public abstract void turnOffSHP();

    public abstract void enableAwayMode();

    public abstract void disableAwayMode();

    public abstract void setTimeForAlert(int newSeconds);
}
