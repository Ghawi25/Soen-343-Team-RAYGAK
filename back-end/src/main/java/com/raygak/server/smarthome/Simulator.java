package com.raygak.server.smarthome;

import com.raygak.server.controllers.TemperatureController;
import com.raygak.server.smarthome.heating.Season;
import com.raygak.server.states.*;
import lombok.Getter;
import lombok.extern.java.Log;

import java.time.LocalTime;
import java.util.Date;

@Getter
public class Simulator {
    private boolean isOn = false;
    private Date currentDate;
    private LocalTime currentTime;
    private double timeSpeed = 1.0;
//    private HouseView houseView = HouseView.getHome();
    private House house;
    private User currentUser = null;
    private State currentState;
    private LogFileMaker logger = new LogFileMaker();

    private SimulationOffState simOff;
    private SimulationOnAndSHHOffAndSHPOffState simOn_AllOff;
    private SimulationOnAndSHHOnAndSHPOffState simOn_SHHOn;
    private SimulationOnAndSHHOffAndSHPOnState simOn_SHPOn;
    private SimulationOnAndSHHOnAndSHPOnState simOn_AllOn;

    private SimulationOffState simOff;
    private SimulationOnAndSHHOffAndSHPOffState simOn_AllOff;
    private SimulationOnAndSHHOnAndSHPOffState simOn_SHHOn;
    private SimulationOnAndSHHOffAndSHPOnState simOn_SHPOn;
    private SimulationOnAndSHHOnAndSHPOnState simOn_AllOn;

    public Simulator(int dayInput, int monthInput, int yearInput, int hoursInput, int minutesInput, House houseInput) {
        this.currentDate = new Date(yearInput, monthInput, dayInput);
        this.currentTime = LocalTime.of(hoursInput, minutesInput, 0);
        this.house = houseInput;
        this.simOff = new SimulationOffState(this);
        this.currentState = this.simOff;
        this.simOn_AllOff = new SimulationOnAndSHHOffAndSHPOffState(this);
        this.simOn_SHHOn = new SimulationOnAndSHHOnAndSHPOffState(this);
        this.simOn_SHPOn = new SimulationOnAndSHHOffAndSHPOnState(this);
        this.simOn_AllOn = new SimulationOnAndSHHOnAndSHPOnState(this);
        this.house.setAssociatedSimulator(this);
    }

    public void setCurrentDate(Date newDate) {
        this.currentDate = newDate;
    }

    public void setCurrentDate(int newDay, int newMonth, int newYear) {
        this.currentDate = new Date(newYear, newMonth, newDay);
    }

    public void setCurrentTime(LocalTime newTime) {
        this.currentTime = newTime;
    }

    public void setCurrentTime(int newHours, int newMinutes) {
        this.currentTime = LocalTime.of(newHours, newMinutes, 0);
    }

    public void setTimeSpeed(double newTimeSpeed) {
        this.timeSpeed = newTimeSpeed;
    }

    public void setHouse(House newHouse) {
        this.house = newHouse;
    }

    public void setCurrentUser(User newUser) {
        this.currentUser = newUser;
    }

    public void setCurrentState(State newState) {
        this.currentState = newState;
    }

    public void setCurrentSeason(Season newSeason) { this.currentState.setCurrentSeason(newSeason); }
    public void setOutdoorTemperature(double newTemperature) { this.currentState.setOutdoorTemperature(newTemperature); }
    public void updateAllRoomTemperatures() { this.currentState.updateAllRoomTemperatures(); }
    public void displayTemperatureInRoomWithID(String roomID) { this.currentState.displayTemperatureInRoomWithID(roomID); }
    public void changeTemperatureInRoom_Remote(String roomID, double newTemperature) {
        this.currentState.changeTemperatureInRoom_Remote(roomID, newTemperature);
    }
    public void changeTemperatureInCurrentRoom_Local(double newTemperature) {
        this.currentState.changeTemperatureInCurrentRoom_Local(newTemperature);
    }

    public void openDoorWithName(String doorName) {
        this.currentState.openDoorWithName(doorName);
    }

    public void closeDoorWithName(String doorName) {
        this.currentState.closeDoorWithName(doorName);
    }

    public void openWindowWithID(String windowID) {
        this.currentState.openWindowWithID(windowID);
    }

    public void closeWindowWithID(String windowID) {
        this.currentState.closeWindowWithID(windowID);
    }

    public void obstructWindowWithID(String windowID) {
        this.currentState.obstructWindowWithID(windowID);
    }

    public void unobstructWindowWithID(String windowID) {
        this.currentState.unobstructWindowWithID(windowID);
    }

    public void turnOnHVACInRoomWithID(String roomID) {
        this.currentState.turnOnHVACInRoomWithID(roomID);
    }

    public void turnOffHVACInRoomWithID(String roomID) {
        this.currentState.turnOffHVACInRoomWithID(roomID);
    }

    public void addInhabitantToRoom(User inhabitant, String roomID) {
        this.currentState.addInhabitantToRoom(inhabitant, roomID);
    }

    public void removeInhabitant(String userEmail) {
        this.currentState.removeInhabitant(userEmail);
    }

    public void moveInhabitantToOtherRoom(User inhabitant, String roomID) {
        this.currentState.moveInhabitantToRoom(inhabitant, roomID);
    }

    public void turnOnLightWithName(String lightName) {
        this.currentState.turnOnLightWithName(lightName);
    }

    public void turnOffLightWithName(String lightName) {
        this.currentState.turnOffLightWithName(lightName);
    }

    public void turnOn() {
        this.currentState.turnOnSimulator();
    }

    public void turnOff() {
        this.currentState.turnOffSimulator();
    }

    public void turnOnSHH() {
        this.currentState.turnOnSHH();
    }

    public void turnOffSHH() {
        this.currentState.turnOffSHH();
    }

    public void turnOnSHP() {
        this.currentState.turnOnSHP();
    }

    public void turnOffSHP() {
        this.currentState.turnOffSHP();
    }

    public void enableAwayMode() {
        this.currentState.enableAwayMode();
    }

    public void disableAwayMode() {
        this.currentState.disableAwayMode();
    }

    public void setTimeForAlert(int newSeconds) {
        this.currentState.setTimeForAlert(newSeconds);
    }
}
