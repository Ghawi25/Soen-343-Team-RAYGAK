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

public class SimulationOnAndSHHOnAndSHPOnState extends State {
    public SimulationOnAndSHHOnAndSHPOnState(Simulator simulatorInput) {
        super(simulatorInput);
    }

    public void turnOnSimulator() {
        System.out.println("The simulation is already on.");
    }
    public void turnOffSimulator() {
        System.out.println("The simulation will be turned off.");
        this.simulator.setCurrentState(this.simulator.getSimOff());
    }

    public void setSimulationDate(Date newDate) {
        this.simulator.setCurrentDate(newDate);
    }
    public void setSimulationDate(int newDay, int newMonth, int newYear) {
        this.simulator.setCurrentDate(newDay, newMonth, newYear);
    }
    public void setCurrentTime(LocalTime newTime) {
        this.simulator.setCurrentTime(newTime);
    }
    public void setCurrentTime(int newHours, int newMinutes) {
        this.simulator.setCurrentTime(newHours, newMinutes);
    }
    public void setTimeSpeed(double newTimeSpeed) {
        this.simulator.setTimeSpeed(newTimeSpeed);
    }
    public void setHouse(House newHouse) {
        this.simulator.setHouse(newHouse);
    }
    public void setCurrentUser(User newUser) {
        this.simulator.setCurrentUser(newUser);
    }
    public void addRoom(Room newRoom) {
        House h = this.simulator.getHouse();
        h.addRoom(newRoom);
        this.simulator.setHouse(h);
    }
    public void removeRoom(String roomID) {
        House h = this.simulator.getHouse();
        h.removeRoom(roomID);
        this.simulator.setHouse(h);
    }
    public void addZone(Zone newZone) {
        House h = this.simulator.getHouse();
        h.addZone(newZone);
        this.simulator.setHouse(h);
    }
    public void removeZone(String zoneID) {
        House h = this.simulator.getHouse();
        h.removeZone(zoneID);
        this.simulator.setHouse(h);
    }
    public void addInhabitantToRoom(User newInhabitant, String inhabitedRoomID) {
        House h = this.simulator.getHouse();
        h.addInhabitantToRoom(newInhabitant, inhabitedRoomID);
        this.simulator.setHouse(h);
    }
    public void removeInhabitant(String inhabitantUsername) {
        House h = this.simulator.getHouse();
        h.removeInhabitant(inhabitantUsername);
        this.simulator.setHouse(h);
    }
    public void moveInhabitantToRoom(User inhabitant, String newRoomID) {
        House h = this.simulator.getHouse();
        h.moveInhabitantToRoom(inhabitant, newRoomID);
        this.simulator.setHouse(h);
    }
    public void setIndoorTemperature(double temperatureInput) {
        House h = this.simulator.getHouse();
        h.setIndoorTemperature(temperatureInput);
        this.simulator.setHouse(h);
    }
    public void setOutdoorTemperature(double temperatureInput) {
        House h = this.simulator.getHouse();
        h.setOutdoorTemperature(temperatureInput);
        this.simulator.setHouse(h);
    }
    public void updateAllRoomTemperatures() {
        House h = this.simulator.getHouse();
        h.updateAllRoomTemperatures();
        this.simulator.setHouse(h);
    }
    public void displayTemperatureInRoomWithID(String roomID) {
        this.simulator.getHouse().getRoomByID(roomID).displayTemperature();
    }
    public void setCurrentSeason(Season newSeason) {
        House h = this.simulator.getHouse();
        h.setCurrentSeason(newSeason);
        this.simulator.setHouse(h);
    }
    public void changeZone(String zoneID, ZoneType type, ArrayList<TemperatureSetting> settingList, ArrayList<Room> roomList) {
        House h = this.simulator.getHouse();
        h.changeZone(zoneID, type, settingList, roomList);
        this.simulator.setHouse(h);
    }
    public void changeTemperatureInRoom_Remote(String roomID, double newTemperature) {
        User u = this.simulator.getCurrentUser();
        u.changeTemperatureInRoom_Remote(roomID, newTemperature);
        this.simulator.setHouse(u.getAssociatedHouse());
    }
    public void changeTemperatureInCurrentRoom_Local(double newTemperature) {
        User u = this.simulator.getCurrentUser();
        u.changeTemperatureInCurrentRoom_Local(newTemperature);
        this.simulator.setHouse(u.getAssociatedHouse());

    }
    public void setUpZone(House house, String zoneID, ZoneType type, ArrayList<TemperatureSetting> settingList, ArrayList<Room> roomList)  {
        User u = this.simulator.getCurrentUser();
        u.setUpZone(house, zoneID, type, settingList, roomList);
        this.simulator.setHouse(u.getAssociatedHouse());
    }
    public void turnOnSHH() {
        System.out.println("SHH is already turned on.");
    }
    public void turnOffSHH() {
        User u = this.simulator.getCurrentUser();
        u.turnOffSHH();
        this.simulator.setHouse(u.getAssociatedHouse());
//        House h = this.simulator.getHouse();
//        h.turnOffSHH();
//        this.simulator.setHouse(h);
        this.simulator.setCurrentState(this.simulator.getSimOn_SHPOn());
    }

    public void openDoorWithName(String doorName) {
        House h = this.simulator.getHouse();
        h.openDoorWithName(doorName);
        this.simulator.setHouse(h);
    }

    public void closeDoorWithName(String doorName) {
        House h = this.simulator.getHouse();
        h.closeDoorWithName(doorName);
        this.simulator.setHouse(h);
    }

    public void openWindowWithID(String windowID) {
        House h = this.simulator.getHouse();
        h.openWindowWithID(windowID);
        this.simulator.setHouse(h);
    }

    public void closeWindowWithID(String windowID) {
        House h = this.simulator.getHouse();
        h.closeWindowWithID(windowID);
        this.simulator.setHouse(h);
    }

    public void obstructWindowWithID(String windowID) {
        House h = this.simulator.getHouse();
        h.obstructWindowWithID(windowID);
        this.simulator.setHouse(h);
    }

    public void unobstructWindowWithID(String windowID) {
        House h = this.simulator.getHouse();
        h.unobstructWindowWithID(windowID);
        this.simulator.setHouse(h);
    }

    public void turnOnHVACInRoomWithID(String roomID) {
        House h = this.simulator.getHouse();
        h.turnOnHVACInRoomWithID(roomID);
        this.simulator.setHouse(h);
    }

    public void turnOffHVACInRoomWithID(String roomID) {
        House h = this.simulator.getHouse();
        h.turnOffHVACInRoomWithID(roomID);
        this.simulator.setHouse(h);
    }

    public void turnOnLightWithName(String lightName) {
        House h = this.simulator.getHouse();
        h.turnOnLightWithName(lightName);
        this.simulator.setHouse(h);
    }

    public void turnOffLightWithName(String lightName) {
        House h = this.simulator.getHouse();
        h.turnOffLightWithName(lightName);
        this.simulator.setHouse(h);
    }

    public void turnOnSHP() {
        System.out.println("SHP is already turned on.");
    }

    public void turnOffSHP() {
        User u = this.simulator.getCurrentUser();
        u.turnOffSHP();
        this.simulator.setHouse(u.getAssociatedHouse());
//        House h = this.simulator.getHouse();
//        h.turnOffSHP();
//        this.simulator.setHouse(h);
        this.simulator.setCurrentState(this.simulator.getSimOn_SHHOn());
    }

    public void enableAwayMode() {
        User u = this.simulator.getCurrentUser();
        u.enableAwayMode();
        this.simulator.setHouse(u.getAssociatedHouse());
//        House house = this.simulator.getHouse();
//        house.enableAwayMode();
//        this.simulator.setHouse(house);
    }

    public void disableAwayMode() {
        User u = this.simulator.getCurrentUser();
        u.disableAwayMode();
        this.simulator.setHouse(u.getAssociatedHouse());
//        House house = this.simulator.getHouse();
//        house.disableAwayMode();
//        this.simulator.setHouse(house);
    }

    public void setTimeForAlert(int newSeconds) {
        User u = this.simulator.getCurrentUser();
        u.setTimeForAlert(newSeconds);
//        House h = this.simulator.getHouse();
//        h.setTimeForAlert(newSeconds);
//        this.simulator.setHouse(h);
    }
}
