package com.raygak.server.smarthome.heating;

import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.User;

public class SHH {
    private House house;
    private boolean isOn = false;

    public SHH(House house) {
        this.house = house;
    }

    public boolean getIsOn() {
        return this.isOn;
    }

    public void turnOn() {
        this.isOn = true;
    }

    public void turnOff() {
        this.isOn = false;
    }

    public void dangerousTemperatureUpdate(String status) {
        if (status.equals("Too Hot")) {
            System.out.println("[SHH] DANGER: Possible fire!");
        }
        if (status.equals("Too Cold")) {
            System.out.println("[SHH] DANGER: Pipes at possible risk of bursting!");
        }
    }

    public void windowErrorUpdate(String windowID, String status) {
        if (status.equals("Already Open")) {
            System.out.println("[SHH] ERROR: The window with ID " + windowID + " is already open! Cancelling command.");
        }
        if (status.equals("Already Closed")) {
            System.out.println("[SHH] ERROR: The window with ID " + windowID + " is already closed! Cancelling command.");
        }
        if (status.equals("Obstructed, Cannot Open")) {
            System.out.println("[SHH] ERROR: The window with ID " + windowID + " is obstructed and cannot be opened! Cancelling command.");
        }
        if (status.equals("Obstructed, Cannot Close")) {
            System.out.println("[SHH] ERROR: The window with ID " + windowID + " is obstructed and cannot be closed! Cancelling command.");
        }
        if (status.equals("Already Obstructed")) {
            System.out.println("[SHH] ERROR: The window with ID " + windowID + " is already obstructed! Cancelling command.");
        }
        if (status.equals("Already Unobstructed")) {
            System.out.println("[SHH] ERROR: The window with ID " + windowID + " is already unobstructed! Cancelling command.");
        }
    }

    public void HVACErrorUpdate(String roomID, String status) {
        if (status.equals("Already On")) {
            System.out.println("[SHH] ERROR: The room with ID " + roomID + " already has the HVAC on! Cancelling command.");
        }
        if (status.equals("Already Off")) {
            System.out.println("[SHH] ERROR: The room with ID " + roomID + " already has the HVAC off! Cancelling command.");
        }
    }

    public void newInhabitantUpdate(User newInhabitant, String roomID) {
        System.out.println("An inhabitant with user account username " + newInhabitant.getUsername() + "has entered room" + roomID);
    }
}
