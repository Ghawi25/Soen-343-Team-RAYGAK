package com.raygak.server.smarthome;

import com.raygak.server.commands.*;
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
    private SHH shh;
    private HouseControl houseControl = new HouseControl();

    //Boolean to check if the house is currently empty during winter.
    private boolean isInWinterAndEmptyHouseProtocol = false;

    //Boolean to check if the house is currently in its summer-related temperature protocol.
    private boolean isInSummerProtocol = false;

    public House(double outdoorTempInput, Season seasonInput) {
        this.outdoorTemperature = outdoorTempInput;
        this.indoorTemperature = outdoorTempInput;
        this.currentSeason = seasonInput;
        this.isInWinterAndEmptyHouseProtocol = true;
        this.shh = new SHH(this);
    }

    public House(ArrayList<Room> roomListInput) {
        this.rooms = roomListInput;
        this.shh = new SHH(this);
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

        for (Room r : this.rooms) {
            r.setCurrentTemperature(outdoorTempInput);
        }
        this.indoorTemperature = outdoorTempInput;
        this.outdoorTemperature = outdoorTempInput;
        this.currentSeason = seasonInput;
        if (this.inhabitants.isEmpty() && this.currentSeason == Season.WINTER) {
            isInWinterAndEmptyHouseProtocol = true;
        }
        this.shh = new SHH(this);
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

    public void summerProtocol() {
        if (this.shh.getIsOn()) {
            if (this.outdoorTemperature < this.indoorTemperature && this.currentSeason == Season.SUMMER) {
                isInSummerProtocol = true;
                System.out.println("SUMMER PROTOCOL.");
                for (Room r : this.rooms) {
                    turnOffHVACInRoomWithID(r.getRoomID());
                    if (this.inhabitants.size() > 0) {
                        for (Window w : r.getWindows()) {
                            openWindowWithID(w.getWindowID());
                        }
                    }
                }
            }
        }
    }

    public void reverseSummerProtocol() {
        if (this.shh.getIsOn()) {
            System.out.println("REVERSE SUMMER PROTOCOL.");
            isInSummerProtocol = false;
            for (Room r : this.rooms) {
                turnOnHVACInRoomWithID(r.getRoomID());
                for (Window w : r.getWindows()) {
                    closeWindowWithID(w.getWindowID());
                }
            }
        }
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
        if (this.shh.getIsOn()) {
            //Source of potential temperature at which there could be a house fire present: https://www.ready.gov/home-fires
            //Temperature units used: Celsius
            if (this.indoorTemperature > 40) {
                this.shh.dangerousTemperatureUpdate("Too Hot");
            }
            //Source of potential temperature at which pipes are susceptible to bursting: https://www.freezemiser.com/blogs/blog/at-what-temperature-do-pipes-burst
            if (this.indoorTemperature < 0) {
                this.shh.dangerousTemperatureUpdate("Too Cold");
            }
        }
    }

    //Lowers the temperature of the house (all of its rooms) in winter if the house is empty. Done for energy-saving reasons.
    public void winterAndEmptyHouseProtocol() {
        if (this.shh.getIsOn()) {
            if (this.currentSeason == Season.WINTER && this.inhabitants.size() == 0) {
                this.isInWinterAndEmptyHouseProtocol = true;
                System.out.println("WINTER AND EMPTY HOUSE PROTOCOL.");
                for (int i = 0; i < this.rooms.size(); i++) {
                    Room r = this.rooms.get(i);
                    Zone z = r.getZone();
                    ArrayList<TemperatureSetting> settingList = z.getSettingList();
                    for (int j = 0; j < settingList.size(); j++) {
                        TemperatureSetting ts = settingList.get(j);
                        ts.setDesiredTemperature(17.0);
                        settingList.set(j, ts);
                    }
                    z.setSettingList(settingList);
                    r.setZone(z);
                    this.rooms.set(i, r);
                }
            }
            computeIndoorTemperature();
        }
    }

    //When the season transitions from Winter to Spring or somebody enters the house
    //during the "winter and empty house protocol", reverse the changes made to the room temperatures via that protocol.
    public void reverseWinterAndEmptyHouseProtocol() {
        if (this.shh.getIsOn()) {
            this.isInWinterAndEmptyHouseProtocol = false;
            System.out.println("REVERSE WINTER AND EMPTY HOUSE PROTOCOL.");
            for (int i = 0; i < this.rooms.size(); i++) {
                Room r = this.rooms.get(i);
                Zone z = r.getZone();
                ArrayList<TemperatureSetting> settingList = z.getSettingList();
                for (int j = 0; j < settingList.size(); j++) {
                    TemperatureSetting ts = settingList.get(j);
                    ts.setDesiredTemperature(ts.getOriginalDesiredTemperature());
                    settingList.set(j, ts);
                }
                z.setSettingList(settingList);
                r.setZone(z);
                this.rooms.set(i, r);
            }
            computeIndoorTemperature();
        }
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
                        if (p.getUsername().equals(this.inhabitants.get(j).getUsername())) {
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
    public void addInhabitantToRoom(User newInhabitant, String inhabitedRoomID) {
        //The old status needs to be logged.
        boolean oldIsInWinterAndEmptyHouseProtocol = this.isInWinterAndEmptyHouseProtocol;
        //Due to how the commencement of this protocol occurred after the removal of the last final inhabitant from the house,
        //the steps need to be reversed in order to restore the temperature to its proper
        if (this.shh.getIsOn() && this.isInWinterAndEmptyHouseProtocol == true) {
            reverseWinterAndEmptyHouseProtocol();
        }
        this.inhabitants.add(newInhabitant);
        for (Room r : this.rooms) {
            if (r.getRoomID().equals(inhabitedRoomID)) {
                r.addInhabitant(newInhabitant);
                //Due to how the indoor temperature is computed in the "reverseWinterAndEmptyHouseProtocol" method, it needs
                //to be computed via an explicit function call if the aforementioned method is not called.
                if (this.shh.getIsOn()) {
                    if (oldIsInWinterAndEmptyHouseProtocol == false) {
                        computeIndoorTemperature();
                    }
                    summerProtocol();
                }
                return;
            }
        }
    }

    public void removeInhabitant(String inhabitantUsername) {
        for (int i = 0; i < this.inhabitants.size(); i++) {
            if (this.inhabitants.get(i).getUsername().equals(inhabitantUsername)) {
                this.inhabitants.remove(i);
                for (int j = 0; j < this.rooms.size(); j++) {
                    Room r = this.rooms.get(j);
                    ArrayList<User> inhabitantList = r.getInhabitants();
                    for (int k = 0; k < inhabitantList.size(); k++) {
                        if (inhabitantList.get(k).getUsername().equals(inhabitantUsername)) {
                            r.removeInhabitant(inhabitantUsername);
                            this.rooms.set(j, r);
                            if (this.shh.getIsOn()) {
                                winterAndEmptyHouseProtocol();
                                if (this.inhabitants.size() == 0 && isInSummerProtocol) {
                                    reverseSummerProtocol();
                                }
                            }
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
        if (this.shh.getIsOn()) {
            if (this.outdoorTemperature < this.indoorTemperature) {
                for (Room r : this.rooms) {
                    turnOffHVACInRoomWithID(r.getRoomID());
                    ArrayList<Window> currentRoomWindows = r.getWindows();
                    for (Window w : currentRoomWindows) {
                        openWindowWithID(w.getWindowID());
                    }
                    r.setWindows(currentRoomWindows);
                }
            } else {
                for (Room r : this.rooms) {
                    turnOnHVACInRoomWithID(r.getRoomID());
                    ArrayList<Window> currentRoomWindows = r.getWindows();
                    for (Window w : currentRoomWindows) {
                        closeWindowWithID(w.getWindowID());
                    }
                    r.setWindows(currentRoomWindows);
                }
            }
        }
    }

    public void setCurrentSeason(Season newSeason) {
        this.currentSeason = newSeason;
        if (this.shh.getIsOn()) {
            winterAndEmptyHouseProtocol();
            if (this.currentSeason != Season.SUMMER && isInSummerProtocol) {
                reverseSummerProtocol();
            }
        }
    }

    public void setRooms(ArrayList<Room> newRoomList) {
        this.rooms = newRoomList;
    }

    public void setZones(ArrayList<Zone> newZoneList) {
        this.zones = newZoneList;
    }

    public boolean doesZoneWithIDExist(String zoneID) {
        for (Zone z : this.zones) {
            if (z.getZoneID().equals(zoneID)) {
                return true;
            }
        }
        return false;
    }

    public void changeZone(String zoneID, ZoneType type, ArrayList<TemperatureSetting> settingList, ArrayList<Room> roomList) {
        for (int i = 0;i < this.zones.size();i++) {
            if (this.zones.get(i).getZoneID().equals(zoneID)) {
                this.zones.set(i, new Zone(zoneID, type, settingList, roomList));
                return;
            }
        }
        throw new IllegalArgumentException("Error: No zone with the provided ID " + zoneID + " exists.");
    }

    public void setDoors(ArrayList<Door> newDoorList) {
        this.doors = newDoorList;
    }

    public void setWindows(ArrayList<Window> newWindowList) {
        this.windows = newWindowList;
    }

    public void setLights(ArrayList<Light> newLightList) {
        this.lights = newLightList;
    }

    public void updateAllRoomTemperatures() {
        if (this.shh.getIsOn()) {
            for (Room r : this.rooms) {
                r.changeCurrentTemperature(this.outdoorTemperature);
            }
            computeIndoorTemperature();
        }
    }

    public void openDoorWithName(String doorName) {
        DoorOpenCommand command = new DoorOpenCommand(this, doorName);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((DoorOpenCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.doors = ((DoorOpenCommand)this.houseControl.getCommand()).getHouse().getDoors();
    }

    public void closeDoorWithName(String doorName) {
        DoorCloseCommand command = new DoorCloseCommand(this, doorName);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((DoorCloseCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.doors = ((DoorCloseCommand)this.houseControl.getCommand()).getHouse().getDoors();
    }

    public void openWindowWithID(String windowID) {
        WindowOpenCommand command = new WindowOpenCommand(this, windowID);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((WindowOpenCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.windows = ((WindowOpenCommand)this.houseControl.getCommand()).getHouse().getWindows();
    }

    public void closeWindowWithID(String windowID) {
        WindowCloseCommand command = new WindowCloseCommand(this, windowID);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((WindowCloseCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.windows = ((WindowOpenCommand)this.houseControl.getCommand()).getHouse().getWindows();
    }

    public void obstructWindowWithID(String windowID) {
        WindowObstructionCommand command = new WindowObstructionCommand(this, windowID);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((WindowObstructionCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.windows = ((WindowOpenCommand)this.houseControl.getCommand()).getHouse().getWindows();
    }

    public void unobstructWindowWithID(String windowID) {
        WindowUnobstructionCommand command = new WindowUnobstructionCommand(this, windowID);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((WindowUnobstructionCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.windows = ((WindowOpenCommand)this.houseControl.getCommand()).getHouse().getWindows();
    }

    public void turnOnHVACInRoomWithID(String roomID) {
        TurnOnHVACCommand command = new TurnOnHVACCommand(this, roomID);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((TurnOnHVACCommand)this.houseControl.getCommand()).getHouse().getRooms();
    }

    public void turnOffHVACInRoomWithID(String roomID) {
        TurnOffHVACCommand command = new TurnOffHVACCommand(this, roomID);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((TurnOffHVACCommand)this.houseControl.getCommand()).getHouse().getRooms();
    }

    public void turnOnSHH() {
        this.shh.turnOn();
    }

    public void turnOffSHH() {
        this.shh.turnOff();
    }
}
