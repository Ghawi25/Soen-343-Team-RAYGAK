package com.raygak.server.smarthome.heating;

import lombok.Data;

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.time.LocalTime;

@Data
public class Room {
    private String roomID;
    private String name;
    private int size_X;
    private int size_Y;
    private double currentTemperature;
    private double desiredUnoccupiedTemperature;
    private boolean isAirConditioningOn;
    private boolean hasLight;
    private ArrayList<Window> windows = new ArrayList<Window>();
    private ArrayList<Person> inhabitants = new ArrayList<Person>();
    private Room topAdjacentRoom;
    private Room bottomAdjacentRoom;
    private Room leftAdjacentRoom;
    private Room rightAdjacentRoom;
    private Zone zone = null;
    private DecimalFormat temperatureFormat = new DecimalFormat("0.00");
    private double lastGeneralTempChange = 0.00;

    public Room(String idInput, String name, int width, int height, boolean light) {
        this.roomID = idInput;
        this.name = name;
        this.size_X = width;
        this.size_Y = height;
        this.hasLight = light;
    }

    public Room(String idInput, int sizeX, int sizeY, double desTempInput, boolean isAirConditioningOn, Room topAdjacent, Room bottomAdjacent, Room leftAdjacent, Room rightAdjacent) {
        this.roomID = idInput;
        this.size_X = sizeX;
        this.size_Y = sizeY;
        this.desiredUnoccupiedTemperature = desTempInput;
        this.currentTemperature = desTempInput;
        this.isAirConditioningOn = isAirConditioningOn;
        this.topAdjacentRoom = topAdjacent;
        this.bottomAdjacentRoom = bottomAdjacent;
        this.leftAdjacentRoom = leftAdjacent;
        this.rightAdjacentRoom = rightAdjacent;
    }

    public Room(String idInput, int sizeX, int sizeY, double desTempInput, boolean isAirConditioningOn, ArrayList<Window> windowListInput, ArrayList<Person> inhabitantListInput, Room topAdjacent, Room bottomAdjacent, Room leftAdjacent, Room rightAdjacent) {
        this.roomID = idInput;
        this.size_X = sizeX;
        this.size_Y = sizeY;
        this.desiredUnoccupiedTemperature = desTempInput;
        this.currentTemperature = desTempInput;
        this.isAirConditioningOn = isAirConditioningOn;
        this.windows = windowListInput;
        this.inhabitants = inhabitantListInput;
        this.topAdjacentRoom = topAdjacent;
        this.bottomAdjacentRoom = bottomAdjacent;
        this.leftAdjacentRoom = leftAdjacent;
        this.rightAdjacentRoom = rightAdjacent;
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

    public void addInhabitant(Person newInhabitant) {
        this.inhabitants.add(newInhabitant);
        //The room's temperature should be updated from its desired unoccupied temperature when somebody enters it.
        if (this.inhabitants.size() == 1) {
            System.out.println("FILLING ROOM " + this.roomID + " WITH FIRST INHABITANT");
            updateTemperature();
        }
    }

    public void removeInhabitant(String personID) {
        if (this.inhabitants.isEmpty()) {
            throw new RuntimeException("Error: Cannot remove any inhabitants, as none are present.");
        }
        System.out.println("REMOVING INHABITANT " + personID);
        for (int i = 0; i < this.inhabitants.size(); i++) {
            if (this.inhabitants.get(i).getPersonID().equals(personID)) {
                this.inhabitants.remove(i);
                if (this.inhabitants.isEmpty()) {
                    this.currentTemperature = Double.parseDouble(temperatureFormat.format(this.desiredUnoccupiedTemperature));
                    System.out.println("Room " + this.roomID + " is now empty. New temperature: " + this.currentTemperature);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Error: The inhabitant (person) with the provided ID does not exist.");
    }

    public void turnOnAC() {
        if (this.isAirConditioningOn) {
            System.out.println("Error: Room with ID " + this.roomID + " already has the AC turned on.");
            return;
        }
        this.isAirConditioningOn = true;
    }

    public void turnOffAC() {
        if (!(this.isAirConditioningOn)) {
            System.out.println("Error: Room with ID " + this.roomID + " already has the AC turned off.");
            return;
        }
        this.isAirConditioningOn = false;
    }

    public void setCurrentTemperature(double temperatureInput) {
        double oldTemperature = this.currentTemperature;
        this.currentTemperature = temperatureInput;
        this.lastGeneralTempChange = Math.abs(this.currentTemperature - oldTemperature);
    }

    public void setDesiredUnoccupiedTemperature(double temperatureInput) {
        this.desiredUnoccupiedTemperature = temperatureInput;
    }

    public void openWindowWithID(String windowID) {
        for (int i = 0; i < windows.size(); i++) {
            if (windows.get(i).getWindowID().equals(windowID)) {
                Window tempWindow = windows.get(i);
                tempWindow.open();
                windows.set(i, tempWindow);
            }
        }
    }

    public void closeWindowWithID(String windowID) {
        for (int i = 0; i < windows.size(); i++) {
            if (windows.get(i).getWindowID().equals(windowID)) {
                Window tempWindow = windows.get(i);
                tempWindow.close();
                windows.set(i, tempWindow);
            }
        }
    }

    public void obstructWindowWithID(String windowID) {
        for (int i = 0; i < windows.size(); i++) {
            if (windows.get(i).getWindowID().equals(windowID)) {
                Window tempWindow = windows.get(i);
                tempWindow.obstruct();
                windows.set(i, tempWindow);
            }
        }
    }

    public void unobstructWindowWithID(String windowID) {
        for (int i = 0; i < windows.size(); i++) {
            if (windows.get(i).getWindowID().equals(windowID)) {
                Window tempWindow = windows.get(i);
                tempWindow.unobstruct();
                windows.set(i, tempWindow);
            }
        }
    }

    public void setZone(Zone newZone) {
        this.zone = newZone;
        updateTemperature();
    }


    public void updateTemperature() {
        if (this.zone == null) {
            return;
        }
        System.out.println("UPDATING TEMPERATURE OF ROOM " + this.getRoomID());
        LocalTime currentTime = LocalTime.now();
        //Check each of the room's temperature settings for the one whose start-end time range contains the current time.
        for (TemperatureSetting setting : this.zone.getSettingList()) {
            LocalTime startTime = setting.getStart();
            LocalTime endTime = setting.getEnd();
            if ((currentTime.isAfter(startTime) && currentTime.isBefore(endTime))) {
                //If at least one person is in the room, make use of the setting's preferred temperature.
                //Otherwise, make use of the room's desired unoccupied temperature.
                if (this.inhabitants.size() >= 1) {
                    if (this.zone.getType() == ZoneType.HEATING) {
                        this.currentTemperature = Double.parseDouble(temperatureFormat.format(setting.getDesiredTemperature() * 1.2));
                    }
                    if (this.zone.getType() == ZoneType.COOLING) {
                        this.currentTemperature = Double.parseDouble(temperatureFormat.format(setting.getDesiredTemperature() * 0.8));
                    }
                } else {
                    this.currentTemperature = Double.parseDouble(temperatureFormat.format(this.desiredUnoccupiedTemperature));
                }
            }
        }
        return;
    }
}
