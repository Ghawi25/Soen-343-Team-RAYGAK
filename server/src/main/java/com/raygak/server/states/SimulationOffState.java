package com.raygak.server.states;

import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.Room;
import com.raygak.server.smarthome.Simulator;
import com.raygak.server.smarthome.User;
import com.raygak.server.smarthome.heating.Season;
import com.raygak.server.smarthome.heating.TemperatureSetting;
import com.raygak.server.smarthome.heating.Zone;
import com.raygak.server.smarthome.heating.ZoneType;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class SimulationOffState extends State {
    public SimulationOffState(Simulator simulatorInput) {
        super(simulatorInput);
    }
    public void turnOnSimulator() {
        System.out.println("The simulation will be turned on.");
        SimulationOnAndSHHOffState newState = new SimulationOnAndSHHOffState(this.simulator);
        this.simulator.turnOn();
        this.simulator.setCurrentState(newState);
    }
    public void turnOffSimulator() {
        System.out.println("The simulation is already off.");
    }
    public void setSimulationDate(Date newDate) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void setSimulationDate(int newDay, int newMonth, int newYear) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void setCurrentTime(LocalTime newTime) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void setCurrentTime(int newHours, int newMinutes) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void setTimeSpeed(double newTimeSpeed) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void setHouse(House newHouse) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void setCurrentUser(User newUser) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void addRoom(Room newRoom) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void removeRoom(String roomID) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void addZone(Zone newZone) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void removeZone(String zoneID) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void addInhabitantToRoom(User newInhabitant, String inhabitedRoomID) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void removeInhabitant(String inhabitantUsername) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void moveInhabitantToRoom(User inhabitant, String newRoomID) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void setIndoorTemperature(double temperatureInput) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void setOutdoorTemperature(double temperatureInput) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void setCurrentSeason(Season newSeason) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void changeZone(String zoneID, ZoneType type, ArrayList<TemperatureSetting> settingList, ArrayList<Room> roomList) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void changeTemperatureInCurrentRoom_Remote(double newTemperature) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void changeTemperatureInCurrentRoom_Local(double newTemperature) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void setUpZone(House house, String zoneID, ZoneType type, ArrayList<TemperatureSetting> settingList, ArrayList<Room> roomList)  {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void turnOnSHH() {
        System.out.println("Nothing can be done while the simulation is off.");
    }
    public void turnOffSHH() {
        System.out.println("Nothing can be done while the simulation is off.");
    }

    public void openDoorWithName(String doorName) {
        System.out.println("Nothing can be done while the simulation is off.");
    }

    public void closeDoorWithName(String doorName) {
        System.out.println("Nothing can be done while the simulation is off.");
    }

    public void openWindowWithID(String windowID) {
        System.out.println("Nothing can be done while the simulation is off.");
    }

    public void closeWindowWithID(String windowID) {
        System.out.println("Nothing can be done while the simulation is off.");
    }

    public void obstructWindowWithID(String windowID) {
        System.out.println("Nothing can be done while the simulation is off.");
    }

    public void unobstructWindowWithID(String windowID) {
        System.out.println("Nothing can be done while the simulation is off.");
    }

    public void turnOnHVACInRoomWithID(String roomID) {
        System.out.println("Nothing can be done while the simulation is off.");
    }

    public void turnOffHVACInRoomWithID(String roomID) {
        System.out.println("Nothing can be done while the simulation is off.");
    }

    public void turnOnLightWithName(String lightName) {
        System.out.println("Nothing can be done while the simulation is off.");
    }

    public void turnOffLightWithName(String lightName) {
        System.out.println("Nothing can be done while the simulation is off.");
    }
}
