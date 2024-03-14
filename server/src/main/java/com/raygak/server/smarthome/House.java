package com.raygak.server.smarthome;

import com.raygak.server.model.User;
import com.raygak.server.smarthome.heating.*;
import lombok.Getter;

import java.text.DecimalFormat;
import java.util.ArrayList;

@Getter
public class House {
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private ArrayList<Light> lights = new ArrayList<Light>();
    private ArrayList<Window> windows = new ArrayList<Window>();
    private ArrayList<Door> doors = new ArrayList<Door>();
    private ArrayList<Zone> zones = new ArrayList<Zone>();
    private ArrayList<User> inhabitants = new ArrayList<User>();
    private DecimalFormat temperatureFormat = new DecimalFormat("0.00");
    private double indoorTemperature;
    private double outdoorTemperature;
    private Season currentSeason;

    //Boolean to check if the house is currently empty during winter.
    private boolean isInWinterAndEmptyHouseProtocol = false;

    public House(double indoorTempInput, double outdoorTempInput, Season seasonInput) {
        this.indoorTemperature = indoorTempInput;
        this.outdoorTemperature = outdoorTempInput;
        this.currentSeason = seasonInput;
        this.isInWinterAndEmptyHouseProtocol = true;
    }

    public House(ArrayList<Room> roomListInput) {
        this.rooms = roomListInput;
    }

    public House(ArrayList<Room> roomListInput, ArrayList<Zone> zoneListInput, double outdoorTempInput, Season seasonInput) {
        this.rooms = roomListInput;
        //Every User, window and light in every room in the input room list is considered to be a part of the house itself.
        for (Room r : roomListInput) {
            for (User p : r.getInhabitants()) {
                this.inhabitants.add(p);
            }
            for (Window w : r.getWindows()) {
                this.windows.add(w);
            }
            for (Door d : r.getDoors()) {
                this.doors.add(d);
            }
            this.lights.add(r.getLight());
        }
        this.zones = zoneListInput;

        //The indoor temperature is calculated as the average of the temperatures of the rooms in the input room list.
        double averageIndoorTemp = 0;
        for (Room r : roomListInput) {
            averageIndoorTemp += r.getCurrentTemperature();
        }
        this.indoorTemperature = Double.parseDouble(temperatureFormat.format(averageIndoorTemp / roomListInput.size()));

        this.outdoorTemperature = outdoorTempInput;
        this.currentSeason = seasonInput;
        if (this.inhabitants.isEmpty() && this.currentSeason == Season.WINTER) {
            isInWinterAndEmptyHouseProtocol = true;
        }
    }


    //Compute the indoor temperature, calculating it to be the average of all rooms' current temperatures.
    public void computeIndoorTemperature() {
        double averageIndoorTemp = 0;
        for (Room r : this.rooms) {
            averageIndoorTemp += r.getCurrentTemperature();
        }
        this.indoorTemperature = Double.parseDouble(temperatureFormat.format(averageIndoorTemp / this.rooms.size()));
        checkForDangerousTemperature();
    }

    public Light getLightByName(String lightName) {
        for (Light l : this.lights) {
            if (l.getName().equals(lightName)) {
                return l;
            }
        }
        throw new IllegalArgumentException("Error: No light with ID " + lightName + " is present in the house.");
    }

    public Window getWindowByID(String windowID) {
        for (Window w : this.windows) {
            if (w.getWindowID().equals(windowID)) {
                return w;
            }
        }
        throw new IllegalArgumentException("Error: No window with ID " + windowID + " is present in the house.");
    }

    public Room getRoomByID(String roomID) {
        for (Room r : this.rooms) {
            if (r.getRoomID().equals(roomID)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Error: No room with ID " + roomID + " is present in the house.");
    }

    public Door getDoorByName(String doorName) {
        for (Door d : this.doors) {
            if (d.getName().equals(doorName)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Error: No door with name " + doorName + " is present in the house.");
    }

    //Dangerous temperature levels need to be checked for in order for emergency notifications to be sent as needed.
    public void checkForDangerousTemperature() {
        //Source of potential temperature at which there could be a house fire present: https://www.quora.com/How-hot-does-a-house-fire-get
        if (this.indoorTemperature > 900) {
            System.out.println("DANGER: Possible fire!");
        }
        //Source of potential temperature at which pipes are susceptible to bursting: https://www.freezemiser.com/blogs/blog/at-what-temperature-do-pipes-burst
        if (this.indoorTemperature < 20) {
            System.out.println("DANGER: Pipes at possible risk of bursting!");
        }
    }

    //Lowers the temperature of the house (all of its rooms) in winter if the house is empty. Done for energy-saving reasons.
    public void winterAndEmptyHouseProtocol() {
        if (this.currentSeason == Season.WINTER && this.inhabitants.size() == 0) {
            this.isInWinterAndEmptyHouseProtocol = true;
            System.out.println("WINTER AND EMPTY HOUSE PROTOCOL.");
            for (Room r : this.rooms) {
                System.out.println("Room: " + r.getRoomID());
                System.out.println("Before: " + r.getCurrentTemperature());
                //The room temperature is reduced by 30%, which seems like it would be reasonable for ensuring considerable
                //energy-related cost savings without decreasing the temperature too much.
                r.setCurrentTemperature(Double.parseDouble(temperatureFormat.format(r.getCurrentTemperature() * 0.7)));
                System.out.println("After: " + r.getCurrentTemperature());
            }
        }
        computeIndoorTemperature();
    }

    //When the season transitions from Winter to Spring or somebody enters the house
    //during the "winter and empty house protocol", reverse the changes made to the room temperatures via that protocol.
    public void reverseWinterAndEmptyHouseProtocol() {
        System.out.println("REVERSE WINTER AND EMPTY HOUSE PROTOCOL.");
        for (Room r : this.rooms) {
            System.out.println("Room: " + r.getRoomID());
            System.out.println("Before: " + r.getCurrentTemperature());
            r.setCurrentTemperature(Double.parseDouble(temperatureFormat.format(r.getCurrentTemperature() + r.getLastGeneralTempChange())));
            System.out.println("After: " + r.getCurrentTemperature());
        }
        computeIndoorTemperature();
    }


    //In adding a new room, add every inhabitant of that room to the house's inhabitant list
    public void addRoom(Room newRoom) {
        this.rooms.add(newRoom);
        for (User p : newRoom.getInhabitants()) {
            this.inhabitants.add(p);
        }
        computeIndoorTemperature();
    }

    //In removing a room, remove every inhabitant within it from the house itself.
    public void removeRoom(String roomID) {
        for (int i = 0; i < this.rooms.size(); i++) {
            if (this.rooms.get(i).getRoomID().equals(roomID)) {
                this.rooms.remove(i);
                for (User p : this.rooms.get(i).getInhabitants()) {
                    for (int j = 0; j < this.inhabitants.size(); j++) {
                        if (p.getEmail().equals(this.inhabitants.get(j).getEmail())) {
                            this.inhabitants.remove(j);
                        }
                    }
                }
                //In case that the removed room resulted in there being no more inhabitants of the house present.
                winterAndEmptyHouseProtocol();
                return;
            }
        }
        throw new IllegalArgumentException("Error: The room with the provided ID does not exist.");
    }

    public void addZone(Zone newZone) {
        this.zones.add(newZone);
        computeIndoorTemperature();
    }

    public void removeZone(String zoneID) {
        for (int i = 0; i < this.zones.size(); i++) {
            if (this.zones.get(i).getZoneID().equals(zoneID)) {
                this.zones.remove(i);
                computeIndoorTemperature();
                return;
            }
        }
        throw new IllegalArgumentException("Error: The zone with the provided ID does not exist.");
    }

    //Due to how we want to add a User object to a room of the house, a room ID is passed as an argument rather than a Room object
    //to allow for searching of the "rooms" ArrayList.
    // Use case 6 - add user to room
    public void addInhabitantToRoom(User newInhabitant, String inhabitedRoomID) {
        //The old status needs to be logged.
        boolean oldIsInWinterAndEmptyHouseProtocol = this.isInWinterAndEmptyHouseProtocol;
        //Due to how the commencement of this protocol occurred after the removal of the last final inhabitant from the house,
        //the steps need to be reversed in order to restore the temperature to its proper
        if (this.isInWinterAndEmptyHouseProtocol == true) {
            this.isInWinterAndEmptyHouseProtocol = false;
            reverseWinterAndEmptyHouseProtocol();
        }
        this.inhabitants.add(newInhabitant);
        for (Room r : this.rooms) {
            if (r.getRoomID().equals(inhabitedRoomID)) {
                r.addInhabitant(newInhabitant);
                //Due to how the indoor temperature is computed in the "reverseWinterAndEmptyHouseProtocol" method, it needs
                //to be computed via an explicit function call if the aforementioned method is not called.
                if (oldIsInWinterAndEmptyHouseProtocol == false) {
                    computeIndoorTemperature();
                }
                return;
            }
        }
    }

    public void removeInhabitant(String inhabitantEmail) {
        for (int i = 0; i < this.inhabitants.size(); i++) {
            if (this.inhabitants.get(i).getEmail().equals(inhabitantEmail)) {
                this.inhabitants.remove(i);
                for (int j = 0; j < this.rooms.size(); j++) {
                    for (int k = 0; k < this.rooms.get(j).getInhabitants().size(); k++) {
                        if (this.rooms.get(j).getInhabitants().get(k).getEmail().equals(inhabitantEmail)) {
                            //A temporary Room object is created in order to properly replace the to-be-modified Room object in the rooms list.
                            Room tempRoom = this.rooms.get(j);
                            tempRoom.removeInhabitant(inhabitantEmail);
                            this.rooms.set(j, tempRoom);
                            //In the event where the removed inhabitant was the last to be removed.
                            winterAndEmptyHouseProtocol();
                            return;
                        }
                    }
                }
            }
        }
        throw new IllegalArgumentException("Error: The inhabitant with the provided ID does not exist.");
    }

    public void setIndoorTemperature(double temperatureInput) {
        this.indoorTemperature = temperatureInput;
        checkForDangerousTemperature();
    }

    public void setOutdoorTemperature(double temperatureInput) {
        this.outdoorTemperature = temperatureInput;
        //Per the requirements listed in the project description.
        if (this.outdoorTemperature < this.indoorTemperature) {
            for (Room r : this.rooms) {
                r.turnOffAC();
                for (Window w : r.getWindows()) {
                    r.openWindowWithID(w.getWindowID());
                }
            }
        } else {
            for (Room r : this.rooms) {
                r.turnOnAC();
                for (Window w : r.getWindows()) {
                    r.closeWindowWithID(w.getWindowID());
                }
            }
        }
    }

    public void setCurrentSeason(Season newSeason) {
        this.currentSeason = newSeason;
        winterAndEmptyHouseProtocol();
    }

    public void setRooms(ArrayList<Room> newRoomList) {
        this.rooms = newRoomList;
    }

    public void setZones(ArrayList<Zone> newZoneList) {
        this.zones = newZoneList;
    }

    public void setDoors(ArrayList<Door> newDoorList) {
        this.doors = newDoorList;
    }

    public void setWindows(ArrayList<Window> newWindowList) {
        this.windows = newWindowList;
    }
}
