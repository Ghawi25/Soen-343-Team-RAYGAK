package com.raygak.server.smarthome;

import com.raygak.server.states.*;
import lombok.Getter;

import java.time.LocalTime;
import java.util.Date;

@Getter
public class Simulator {
    private boolean isOn = false;
    private Date currentDate;
    private LocalTime currentTime;
    private double timeSpeed = 1.0;
    private HouseView houseView = HouseView.getHome();
    private House house;
    private User currentUser = null;
    private State currentState;

    public Simulator(int dayInput, int monthInput, int yearInput, int hoursInput, int minutesInput, House houseInput) {
        this.currentDate = new Date(yearInput, monthInput, dayInput);
        this.currentTime = LocalTime.of(hoursInput, minutesInput, 0);
        this.house = houseInput;
        this.currentState = new SimulationOffState(this);
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

    public void openDoorWithName(String doorName) {
        this.house.openDoorWithName(doorName);
    }

    public void closeDoorWithName(String doorName) {
        this.house.closeDoorWithName(doorName);
    }

    public void openWindowWithID(String windowID) {
        this.house.openWindowWithID(windowID);
    }

    public void closeWindowWithID(String windowID) {
        this.house.closeWindowWithID(windowID);
    }

    public void obstructWindowWithID(String windowID) {
        this.house.obstructWindowWithID(windowID);
    }

    public void unobstructWindowWithID(String windowID) {
        this.house.unobstructWindowWithID(windowID);
    }

    public void turnOnHVACInRoomWithID(String roomID) {
        this.house.turnOnHVACInRoomWithID(roomID);
    }

    public void turnOffHVACInRoomWithID(String roomID) {
        this.house.turnOffHVACInRoomWithID(roomID);
    }

    public void turnOnLightWithName(String lightName) {
        this.house.turnOnLightWithName(lightName);
    }

    public void turnOffLightWithName(String lightName) {
        this.house.turnOffLightWithName(lightName);
    }

    public void turnOn() {
        this.isOn = true;
    }

    public void turnOff() {
        this.isOn = false;
    }
}
