package com.raygak.server.smarthome;

import com.raygak.server.timers.IntruderAlertTimer;
import com.raygak.server.timers.SHP15DegreeTimer;
import lombok.Data;
import com.raygak.server.smarthome.heating.*;

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.Timer;

@Data
public class Room {
    private String roomID;
    private String name;
    private int width;
    private int height;
    private double currentTemperature;
    private double desiredUnoccupiedTemperature;
    private double desiredZoneSpecificTemperature = 0.0;
    private boolean isOverridden = false;
    private boolean isHVACOn;
    private Light light;
    private boolean motionDetectorInstalled;
    private ArrayList<Window> windows = new ArrayList<Window>();
    private ArrayList<Door> doors = new ArrayList<Door>();
    private ArrayList<User> inhabitants = new ArrayList<User>();
    private Room topAdjacentRoom;
    private Room bottomAdjacentRoom;
    private Room leftAdjacentRoom;
    private Room rightAdjacentRoom;
    private Zone zone = null;
    private DecimalFormat temperatureFormat = new DecimalFormat("0.00");
    private double lastGeneralTempChange = 0.00;
    private House associatedHouse;
    private SHP15DegreeTimer fifteenDegreeTimer;
    private IntruderAlertTimer intruderAlertTimer;

    public Room(String idInput, String name, int width, int height, boolean hasLight) {
        this.roomID = idInput;
        this.name = name;
        this.width = width;
        this.height = height;
        this.light = hasLight ? new Light("New Light", "Centre") : null;
        this.motionDetectorInstalled = false;
    }

    public Room(String idInput, String name, int width, int height, Light light) {
        this.roomID = idInput;
        this.name = name;
        this.width = width;
        this.height = height;
        this.light = light;
        this.motionDetectorInstalled = false;
    }

    public Room(String idInput, String name, int width, int height, boolean hasLight, boolean motionDetectorInstalled) {
        this.roomID = idInput;
        this.name = name;
        this.width = width;
        this.height = height;
        this.light = hasLight ? new Light("New Light", "Centre") : null;
        this.motionDetectorInstalled = motionDetectorInstalled;
    }

    public Room(String idInput, String name, int width, int height, Light light, boolean motionDetectorInstalled) {
        this.roomID = idInput;
        this.name = name;
        this.width = width;
        this.height = height;
        this.light = light;
        this.motionDetectorInstalled = motionDetectorInstalled;
    }

    public Room(String idInput, String name, int width, int height, Light light, boolean motionDetectorInstalled, double desTempInput, boolean isHVACOn, Room topAdjacent, Room bottomAdjacent, Room leftAdjacent, Room rightAdjacent) {
        this.roomID = idInput;
        this.name = name;
        this.width = width;
        this.height = height;
        this.light = light;
        this.motionDetectorInstalled = motionDetectorInstalled;
        this.desiredUnoccupiedTemperature = desTempInput;
        this.currentTemperature = desTempInput;
        this.isHVACOn = isHVACOn;
        this.topAdjacentRoom = topAdjacent;
        this.bottomAdjacentRoom = bottomAdjacent;
        this.leftAdjacentRoom = leftAdjacent;
        this.rightAdjacentRoom = rightAdjacent;
    }

    public Room(String idInput, String name, int width, int height, Light light, boolean motionDetectorInstalled, double desTempInput, boolean isHVACOn, ArrayList<Window> windowListInput, ArrayList<Door> doorListInput, ArrayList<User> inhabitantListInput, Room topAdjacent, Room bottomAdjacent, Room leftAdjacent, Room rightAdjacent) {
        this.roomID = idInput;
        this.name = name;
        this.width = width;
        this.height = height;
        this.light = light;
        this.motionDetectorInstalled = motionDetectorInstalled;
        this.desiredUnoccupiedTemperature = desTempInput;
        this.currentTemperature = desTempInput;
        this.isHVACOn = isHVACOn;
        this.windows = windowListInput;
        this.doors = doorListInput;
        this.inhabitants = inhabitantListInput;
        this.topAdjacentRoom = topAdjacent;
        this.bottomAdjacentRoom = bottomAdjacent;
        this.leftAdjacentRoom = leftAdjacent;
        this.rightAdjacentRoom = rightAdjacent;
    }

    public void setCurrentTemperature(double newCurrentTemperature) {
        this.currentTemperature = newCurrentTemperature;
        if (this.associatedHouse != null) {
            this.fifteenDegreeTimer.setCurrentTemperature(newCurrentTemperature);
        }
    }

    public void setAssociatedHouse(House newHouse) {
        this.associatedHouse = newHouse;
        this.fifteenDegreeTimer = new SHP15DegreeTimer(this);
    }

    public void installMotionDetector() {
        this.motionDetectorInstalled = true;
    }

    public void removeMotionDetector() {
        this.motionDetectorInstalled = false;
    }

    public void setIntruderAlertTimer(IntruderAlertTimer newTimer) {
        this.intruderAlertTimer = newTimer;
    }

    public void displayTemperature() {
        System.out.println(this.currentTemperature + (this.isOverridden ? " (overridden)" : ""));
    }

    public void addWindow(Window newWindow) {
        this.windows.add(newWindow);
    }

    public void removeWindow(String windowID) {
        for (int i = 0; i < this.windows.size(); i++) {
            if (this.windows.get(i).getWindowID().equals(windowID)) {
                this.windows.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("Error: The window with the provided ID does not exist.");
    }

    public void addDoor(Door newDoor) {
        this.doors.add(newDoor);
    }

    public void removeDoor(String doorName) {
        for (int i = 0; i < this.doors.size(); i++) {
            if (this.doors.get(i).getName().equals(doorName)) {
                this.doors.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("Error: The window with the provided ID does not exist.");
    }

    public void addInhabitant(User newInhabitant) {
        newInhabitant.setCurrentRoom(this);
        newInhabitant.setAssociatedHouse(this.associatedHouse);
        this.inhabitants.add(newInhabitant);
        //The room's temperature should be updated from its desired unoccupied temperature when somebody enters it.
        if (this.inhabitants.size() == 1) {
            double oldTemp = this.currentTemperature;
            updateTemperature();
            double newTemp = this.currentTemperature;
            if (oldTemp != newTemp) {
                this.associatedHouse.getAssociatedSimulator().getLogger().temperatureUpdateLog(this.roomID, oldTemp, newTemp, this.associatedHouse.getShh().isOn(), "A new person has entered the room when it was originally uninhabited.", newInhabitant.getUsername());
            }
        }
    }

    public void removeInhabitant(String username) {
        if (this.inhabitants.isEmpty()) {
            throw new RuntimeException("Error: Cannot remove any inhabitants, as none are present.");
        }
        for (int i = 0; i < this.inhabitants.size(); i++) {
            if (this.inhabitants.get(i).getUsername().equals(username)) {
                this.inhabitants.remove(i);
                if (this.inhabitants.isEmpty()) {
                    double oldTemp = this.currentTemperature;
                    updateTemperature();
                    double newTemp = this.currentTemperature;
                    if (oldTemp != newTemp) {
                        this.associatedHouse.getAssociatedSimulator().getLogger().temperatureUpdateLog(this.roomID, oldTemp, newTemp, this.associatedHouse.getShh().isOn(), "A person has exited a room, leaving it uninhabited.", username);
                    }

                }
                return;
            }
        }
        throw new IllegalArgumentException("Error: The inhabitant (User) with the provided ID does not exist.");
    }

    public void turnOnHVAC() {
        this.isHVACOn = true;
    }

    public void turnOffHVAC() {
        this.isHVACOn = false;
    }

    public void turnOnLight() {
        if (this.light.isOn()) {
            System.out.println("Error: Room with ID " + this.roomID + " already has the light turned on.");
            return;
        }
        this.light.turnOn();
    }

    public void turnOffLight() {
        if (!(this.light.isOn())) {
            System.out.println("Error: Room with ID " + this.roomID + " already has the light turned off.");
            return;
        }
        this.light.turnOff();
    }

    public void setCurrentTemperature(double temperatureInput, boolean isManualChange) {
        double oldTemperature = this.currentTemperature;
        this.setCurrentTemperature(temperatureInput);
        this.lastGeneralTempChange = Math.abs(this.currentTemperature - oldTemperature);
        this.desiredZoneSpecificTemperature = temperatureInput;
        if (isManualChange) {
            this.isOverridden = true;
        }
    }

    public void setDesiredUnoccupiedTemperature(double temperatureInput) {
        this.desiredUnoccupiedTemperature = temperatureInput;
    }

    public void setZone(Zone newZone) {
        this.zone = newZone;
        this.desiredZoneSpecificTemperature = this.zone.getCurrentZoneSetting();
        updateTemperature();
    }

    public void setWindows(ArrayList<Window> newWindowList) {
        this.windows = newWindowList;
    }

    public void setDoors(ArrayList<Door> newDoorList) {
        this.doors = newDoorList;
    }

    public void setLight(Light newLight) {
        this.light = newLight;
    }

    public void changeCurrentTemperature(double outsideTemperature) {
        if (this.isHVACOn == true) {
            if (this.currentTemperature == this.desiredZoneSpecificTemperature) {
                System.out.println("Turning HVAC off for room " + this.roomID);
                this.isHVACOn = false;
            }
            if (this.currentTemperature < this.desiredZoneSpecificTemperature) {
                this.setCurrentTemperature(Math.round((this.currentTemperature + 0.1) * 100.0) / 100.0);
            } else {
                this.setCurrentTemperature(Math.round((this.currentTemperature - 0.1) * 100.0) / 100.0);
            }
        } else {
            if (this.currentTemperature <= (this.desiredZoneSpecificTemperature - 0.25) || this.currentTemperature >= (this.desiredZoneSpecificTemperature + 0.25)) {
                System.out.println("Turning HVAC on for room " + this.roomID);
                this.isHVACOn = true;
                changeCurrentTemperature(outsideTemperature);
            }
            if (this.currentTemperature < outsideTemperature) {
                this.setCurrentTemperature(Math.round((this.currentTemperature + 0.05) * 100.0) / 100.0);
            } else if (this.currentTemperature > outsideTemperature) {
                this.setCurrentTemperature(Math.round((this.currentTemperature - 0.05) * 100.0) / 100.0);
            } else {
                System.out.println("The temperature remains unchanged.");
            }
        }
        System.out.println("Room " + this.roomID);
        System.out.println("HVAC (" + (this.zone.getType() == ZoneType.HEATING ? "Heating" : "Cooling") + ") is " + (this.isHVACOn ? "on." : "off"));
        System.out.println("Temperature of room is: " + this.currentTemperature);
    }

    public void updateTemperature() {
        if (this.inhabitants.size() >= 1) {
            if (this.zone.getType() == ZoneType.HEATING) {
                this.setCurrentTemperature(Double.parseDouble(temperatureFormat.format(this.desiredZoneSpecificTemperature * 1.2)));
            }
            if (this.zone.getType() == ZoneType.COOLING) {
                this.setCurrentTemperature(Double.parseDouble(temperatureFormat.format(this.desiredZoneSpecificTemperature * 0.8)));
            }
        } else {
            this.setCurrentTemperature(Double.parseDouble(temperatureFormat.format(this.desiredUnoccupiedTemperature)));
        }
    }

    public void start15DegreeTimer() {
        if (this.fifteenDegreeTimer.isRunning() == false) {
            this.fifteenDegreeTimer = new SHP15DegreeTimer(this);
            this.fifteenDegreeTimer.start();
        }
    }

    public void stop15DegreeTimer() {
        this.fifteenDegreeTimer.stopTimer();
    }

    public void startIntruderAlertTimer() { this.intruderAlertTimer.start(); }

    public void stopIntruderAlertTimer() {
        if (this.intruderAlertTimer != null) {
            this.intruderAlertTimer.stopTimer();
        }
    }
}
