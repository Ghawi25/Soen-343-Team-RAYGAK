package com.raygak.server.smarthome;

import com.raygak.server.commands.*;
import com.raygak.server.mediation.SHSMediator;
import com.raygak.server.modules.SHH;
import com.raygak.server.smarthome.heating.*;
import com.raygak.server.modules.SHP;
import com.raygak.server.smarthome.security.SHPEventType;
import com.raygak.server.smarthome.security.SHPListener;
import com.raygak.server.timers.IntruderAlertTimer;
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
    private SHSMediator mediator = new SHSMediator();
    private SHH shh;
    private SHP shp;
    private HouseControl houseControl = new HouseControl();
    private Simulator associatedSimulator;

    //Boolean to check if the house is currently empty during winter.
    private boolean isInWinterAndEmptyHouseProtocol = false;

    //Boolean to check if the house is currently in its summer-related temperature protocol.
    private boolean isInSummerProtocol = false;

    public House(double outdoorTempInput, Season seasonInput) {
        this.outdoorTemperature = outdoorTempInput;
        this.indoorTemperature = outdoorTempInput;
        this.currentSeason = seasonInput;
        this.isInWinterAndEmptyHouseProtocol = true;
        this.shh = new SHH(this.mediator, this);
        this.shp = new SHP(this.mediator, this);
        this.shp.setHouse(this);
    }

    public House(ArrayList<Room> roomListInput) {
        this.rooms = roomListInput;
        this.shh = new SHH(this.mediator, this);
        this.shp = new SHP(this.mediator, this);
        this.shp.setHouse(this);
    }

    public House(ArrayList<Room> roomListInput, ArrayList<Zone> zoneListInput, double outdoorTempInput, Season seasonInput) {
        this.rooms = roomListInput;
        //Every User, window and light in every room in the input room list is considered to be a part of the house itself.
        for (Room r : roomListInput) {
            for (User p : r.getInhabitants()) {
                p.setAssociatedHouse(this);
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
        this.indoorTemperature = outdoorTempInput;
        this.outdoorTemperature = outdoorTempInput;
        this.currentSeason = seasonInput;
        if (this.inhabitants.isEmpty() && this.currentSeason == Season.WINTER) {
            isInWinterAndEmptyHouseProtocol = true;
        }
        this.shh = new SHH(this.mediator, this);
        this.shp = new SHP(this.mediator, this);
        this.shp.setHouse(this);
        this.mediator.setSHH(this.shh);
        this.mediator.setSHP(this.shp);
        for (Room r : this.rooms) {
            r.setAssociatedHouse(this);
            r.setCurrentTemperature(outdoorTempInput, false);
        }
    }

    public void setAssociatedSimulator(Simulator newSimulator) {
        this.associatedSimulator = newSimulator;
    }


    //Compute the indoor temperature, calculating it to be the average of all rooms' current temperatures.
    public void computeIndoorTemperature() {
        double averageIndoorTemp = 0;
        for (Room r : this.rooms) {
            averageIndoorTemp += r.getCurrentTemperature();
        }
        this.indoorTemperature = Double.parseDouble(temperatureFormat.format(averageIndoorTemp / this.rooms.size()));
        this.shh.setCurrentHouseTemperature(this.indoorTemperature);
        checkForDangerousTemperature();
    }

    public void summerProtocol() {
        LogFileMaker logger = this.associatedSimulator.getLogger();
        if (this.shh.isOn()) {
            if ((this.outdoorTemperature < this.indoorTemperature && this.currentSeason == Season.SUMMER) && this.inhabitants.size() > 0) {
                if (this.isInWinterAndEmptyHouseProtocol) {
                    reverseWinterAndEmptyHouseProtocol();
                }
                isInSummerProtocol = true;
                System.out.println("SUMMER PROTOCOL.");
                for (Room r : this.rooms) {
                    boolean isHVACInitiallyOpen = r.isHVACOn();
                    turnOffHVACInRoomWithID(r.getRoomID());
                    boolean isHVACOpenAfter = r.isHVACOn();
                    if (isHVACInitiallyOpen != isHVACOpenAfter) {
                        logger.openCloseLog("HVAC Unit in Room", r.getRoomID(), isHVACOpenAfter ? "Open" : "Closed", "The outside temperature of the house has become equal to or greater than the inside temperature, or nobody is home, during the summer season.");
                    }
                    if (this.inhabitants.size() > 0) {
                        for (Window w : r.getWindows()) {
                            if (this.shh.isSHPInAwayMode()) {
                                logger.unableToOpenOrCloseLog("Window", w.getWindowID(), w.isOpen() ? "Opened" : "Closed", "Away Mode");
                            } else {
                                boolean isInitiallyOpen = w.isOpen();
                                openWindowWithID(w.getWindowID());
                                boolean isOpenAfter = w.isOpen();
                                if (isInitiallyOpen != isOpenAfter) {
                                    logger.openCloseLog("Window", w.getWindowID(), isOpenAfter ? "Open" : "Closed", "The outside temperature of the house has become equal to or greater than the inside temperature, or nobody is home, during the summer season.");
                                }
                                if (isInitiallyOpen == isOpenAfter) {
                                    logger.unableToOpenOrCloseLog("Window", w.getWindowID(), isInitiallyOpen ? "Opened" : "Closed", "Obstruction");
                                }
                            }
                        }
                    }
                }
                this.associatedSimulator.getLogger().protocolLog(this.shh.isOn(), "The outside temperature of the house has become lower than the inside temperature and at least one person is home during the summer season.");
            }
        }
    }

    public void reverseSummerProtocol() {
        LogFileMaker logger = this.associatedSimulator.getLogger();
        if (this.shh.isOn()) {
            if ((this.outdoorTemperature >= this.indoorTemperature || this.currentSeason == Season.SUMMER) || this.inhabitants.size() == 0) {
                System.out.println("REVERSE SUMMER PROTOCOL.");
                isInSummerProtocol = false;
                for (Room r : this.rooms) {
                    boolean isHVACInitiallyOpen = r.isHVACOn();
                    turnOnHVACInRoomWithID(r.getRoomID());
                    boolean isHVACOpenAfter = r.isHVACOn();
                    if (isHVACInitiallyOpen != isHVACOpenAfter) {
                        logger.openCloseLog("HVAC Unit in Room", r.getRoomID(), isHVACOpenAfter ? "Open" : "Closed", "The outside temperature of the house has become equal to or greater than the inside temperature, or nobody is home, during the summer season.");
                    }
                    for (Window w : r.getWindows()) {
                        boolean isInitiallyOpen = w.isOpen();
                        closeWindowWithID(w.getWindowID());
                        boolean isOpenAfter = w.isOpen();
                        if (isInitiallyOpen != isOpenAfter) {
                            logger.openCloseLog("Window", w.getWindowID(), isOpenAfter ? "Open" : "Closed", "The outside temperature of the house has become equal to or greater than the inside temperature, or nobody is home, during the summer season.");
                        }
                        if (isInitiallyOpen == isOpenAfter) {
                            logger.unableToOpenOrCloseLog("Window", w.getWindowID(), isInitiallyOpen ? "Opened" : "Closed", "Obstruction");
                        }
                    }
                }
                logger.protocolLog(this.shh.isOn(), "The outside temperature of the house has become equal to or greater than the inside temperature, or nobody is home, during the summer season.");
            }
        }
    }

    public void updateAllRoomTemperatures() {
        if (this.shh.isOn()) {
            for (Room r : this.rooms) {
                r.changeCurrentTemperature(this.outdoorTemperature);
            }
            computeIndoorTemperature();
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
        if (this.shh.isOn()) {
            //Source of potential temperature at which there could be a house fire present: https://www.ready.gov/home-fires
            //Temperature units used: Celsius
            if (this.indoorTemperature > 135.0) {
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
        LogFileMaker logger = this.associatedSimulator.getLogger();
        if (this.shh.isOn()) {
            if (this.currentSeason == Season.WINTER && this.inhabitants.size() == 0) {
                if (this.isInSummerProtocol) {
                    reverseSummerProtocol();
                }
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
            this.associatedSimulator.getLogger().protocolLog(this.shh.isOn(), "The house has become uninhabited during the winter season.");
            computeIndoorTemperature();
        }
    }

    //When the season transitions from Winter to Spring or somebody enters the house
    //during the "winter and empty house protocol", reverse the changes made to the room temperatures via that protocol.
    public void reverseWinterAndEmptyHouseProtocol() {
        if (this.shh.isOn()) {
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
            this.associatedSimulator.getLogger().protocolLog(this.shh.isOn(), "The house has become inhabited once again during the winter season.");
            computeIndoorTemperature();
        }
    }


    //In adding a new room, add every inhabitant of that room to the house's inhabitant list
    public void addRoom(Room newRoom) {
        newRoom.setAssociatedHouse(this);
        this.rooms.add(newRoom);
        for (User p : newRoom.getInhabitants()) {
            p.setAssociatedHouse(this);
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
        if (this.shh.isOn() && this.isInWinterAndEmptyHouseProtocol == true) {
            reverseWinterAndEmptyHouseProtocol();
        }
        this.inhabitants.add(newInhabitant);
        for (Room r : this.rooms) {
            if (r.getRoomID().equals(inhabitedRoomID)) {
                newInhabitant.setAssociatedHouse(this);
                r.addInhabitant(newInhabitant);
                newInhabitant.setCurrentRoom(r);
                //Due to how the indoor temperature is computed in the "reverseWinterAndEmptyHouseProtocol" method, it needs
                //to be computed via an explicit function call if the aforementioned method is not called.
                if (this.shh.isOn()) {
                    if (oldIsInWinterAndEmptyHouseProtocol == false) {
                        computeIndoorTemperature();
                    }
                    summerProtocol();
                }
                if (this.shp.isOn() && r.isMotionDetectorInstalled()) {
                    this.associatedSimulator.getLogger().motionDetectedLog(r.getRoomID(), newInhabitant.getUsername());
                    this.shp.notify(SHPEventType.MOTION_DETECTED, newInhabitant.getUsername());
                    IntruderAlertTimer intruderAlertTimer = new IntruderAlertTimer(newInhabitant.getUsername(), this);
                    r.setIntruderAlertTimer(intruderAlertTimer);
                    r.startIntruderAlertTimer();
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
                            if (this.inhabitants.size() == 0) {
                                System.out.println("The house is now empty.");
                            }
                            if (this.shh.isOn()) {
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

    public void moveInhabitantToRoom(User inhabitant, String newRoomID) {
        int inhabitantPosition = 0;
        for (int i = 0;i < this.inhabitants.size();i++) {
            if (this.inhabitants.get(i).getUsername().equals(inhabitant.getUsername())) {
                inhabitantPosition = i;
                break;
            }
        }
        int oldRoomPosition = 0;
        for (int i = 0;i < this.rooms.size();i++) {
            Room r = this.rooms.get(i);
            if (r.getRoomID().equals(inhabitant.getCurrentRoom().getRoomID())) {
                oldRoomPosition = i;
                break;
            }
        }
        int newRoomPosition = 0;
        for (int i = 0;i < this.rooms.size();i++) {
            Room r = this.rooms.get(i);
            if (r.getRoomID().equals(newRoomID)) {
                newRoomPosition = i;
                break;
            }
        }
        Room oldRoom = this.rooms.get(oldRoomPosition);
        oldRoom.removeInhabitant(inhabitant.getUsername());
        this.rooms.set(oldRoomPosition, oldRoom);
        Room roomToBeMovedTo = this.rooms.get(newRoomPosition);
        roomToBeMovedTo.addInhabitant(inhabitant);
        this.rooms.set(newRoomPosition, roomToBeMovedTo);
        inhabitant.setCurrentRoom(roomToBeMovedTo);
        this.inhabitants.set(inhabitantPosition, inhabitant);
    }

    public void setIndoorTemperature(double temperatureInput) {
        this.indoorTemperature = temperatureInput;
        this.shh.setCurrentHouseTemperature(temperatureInput);
        checkForDangerousTemperature();
    }

    public void setOutdoorTemperature(double temperatureInput) {
        double oldTemperature = this.outdoorTemperature;
        this.outdoorTemperature = temperatureInput;
        double newTemperature = this.outdoorTemperature;
        this.associatedSimulator.getLogger().temperatureUpdateLog("Outside", oldTemperature, newTemperature, this.shh.isOn(), "The outside temperature has been changed by a simulator user.", "Simulator User");
        if (isInSummerProtocol == false) {
            summerProtocol();
        } else {
            reverseSummerProtocol();
        }
    }

    public void setCurrentSeason(Season newSeason) {
        this.currentSeason = newSeason;
        if (this.shh.isOn()) {
            if (this.currentSeason != Season.SUMMER && isInWinterAndEmptyHouseProtocol) {
                reverseSummerProtocol();
            }
            if (this.currentSeason != Season.WINTER && isInWinterAndEmptyHouseProtocol) {
                reverseWinterAndEmptyHouseProtocol();
            }
            if (this.currentSeason == Season.WINTER) {
                if (this.isInSummerProtocol) {
                    reverseSummerProtocol();
                }
                winterAndEmptyHouseProtocol();
            }
            if (this.currentSeason == Season.SUMMER) {
                if (this.isInWinterAndEmptyHouseProtocol) {
                    reverseWinterAndEmptyHouseProtocol();
                }
                summerProtocol();
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

    public void openDoorWithName(String doorName) {
        if (this.shh.isSHPInAwayMode()) {
            System.out.println("[ERROR] Cannot open a window while the house is in 'Away Mode'!");
            this.associatedSimulator.getLogger().unableToOpenOrCloseLog("Door", doorName, "Open", "Away Mode");
        } else {
            DoorOpenCommand command = new DoorOpenCommand(this, doorName);
            this.houseControl.setCommand(command);
            this.houseControl.execute();
            this.rooms = ((DoorOpenCommand) this.houseControl.getCommand()).getHouse().getRooms();
            this.doors = ((DoorOpenCommand) this.houseControl.getCommand()).getHouse().getDoors();
            if (this.shp.isOn() && this.getDoorByName(doorName).isOpen()) {
                this.associatedSimulator.getLogger().openCloseLog("Door", doorName, "Open", "The door with name '" + doorName + " has been opened while the home is in 'Away Mode'.");
                this.shp.notify(SHPEventType.DOOR_OPENED);
            }
        }
    }

    public void closeDoorWithName(String doorName) {
        DoorCloseCommand command = new DoorCloseCommand(this, doorName);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((DoorCloseCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.doors = ((DoorCloseCommand)this.houseControl.getCommand()).getHouse().getDoors();
    }

    public void openWindowWithID(String windowID) {
        if (this.shh.isSHPInAwayMode()) {
            System.out.println("[ERROR] Cannot open a window while the house is in 'Away Mode'!");
            this.associatedSimulator.getLogger().unableToOpenOrCloseLog("Window", windowID, "Open", "Away Mode");
        } else {
            WindowOpenCommand command = new WindowOpenCommand(this, windowID);
            this.houseControl.setCommand(command);
            this.houseControl.execute();
            this.rooms = ((WindowOpenCommand) this.houseControl.getCommand()).getHouse().getRooms();
            this.windows = ((WindowOpenCommand) this.houseControl.getCommand()).getHouse().getWindows();
            if (this.shp.isOn() && this.getWindowByID(windowID).isOpen()) {
                this.associatedSimulator.getLogger().openCloseLog("Window", windowID, "Open", "The window with ID '" + windowID + " has been opened while the home is in 'Away Mode'.");
                this.shp.notify(SHPEventType.WINDOW_OPENED);
            }
        }
    }

    public void closeWindowWithID(String windowID) {
        WindowCloseCommand command = new WindowCloseCommand(this, windowID);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((WindowCloseCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.windows = ((WindowCloseCommand)this.houseControl.getCommand()).getHouse().getWindows();
    }

    public void obstructWindowWithID(String windowID) {
        WindowObstructionCommand command = new WindowObstructionCommand(this, windowID);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((WindowObstructionCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.windows = ((WindowObstructionCommand)this.houseControl.getCommand()).getHouse().getWindows();
    }

    public void unobstructWindowWithID(String windowID) {
        WindowUnobstructionCommand command = new WindowUnobstructionCommand(this, windowID);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((WindowUnobstructionCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.windows = ((WindowUnobstructionCommand)this.houseControl.getCommand()).getHouse().getWindows();
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

    public void turnOnLightWithName(String lightName) {
        LightOnCommand command = new LightOnCommand(this, lightName);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((LightOnCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.lights = ((LightOnCommand)this.houseControl.getCommand()).getHouse().getLights();
    }

    public void turnOffLightWithName(String lightName) {
        LightOffCommand command = new LightOffCommand(this, lightName);
        this.houseControl.setCommand(command);
        this.houseControl.execute();
        this.rooms = ((LightOffCommand)this.houseControl.getCommand()).getHouse().getRooms();
        this.lights = ((LightOffCommand)this.houseControl.getCommand()).getHouse().getLights();
    }

    public void turnOnSHH() {
        this.shh.turnOn();
        summerProtocol();
        winterAndEmptyHouseProtocol();
    }

    public void turnOffSHH() {
        this.shh.turnOff();
        if (isInSummerProtocol) {
            reverseSummerProtocol();
        }
        if (isInWinterAndEmptyHouseProtocol) {
            reverseWinterAndEmptyHouseProtocol();
        }
    }

    public void turnOnSHP() {
        this.shp.turnOn();
    }

    public void turnOffSHP() {
        this.shp.turnOff();
    }

    public void closeAllDoorsAndWindows() {
        for (Door d : this.doors) {
            boolean isDoorInitiallyOpen = d.isOpen();
            d.closeDoor();
            boolean isDoorOpenAfter = d.isOpen();
            if (isDoorInitiallyOpen != isDoorOpenAfter) {
                this.associatedSimulator.getLogger().openCloseLog("Door", d.getName(), isDoorOpenAfter ? "Open" : "Closed", "'Away Mode' has been turned on, automatically closing all doors in the house.");
            }
            if (isDoorInitiallyOpen == isDoorOpenAfter) {
                this.associatedSimulator.getLogger().unableToOpenOrCloseLog("Door", d.getName(), isDoorOpenAfter ? "Opened" : "Closed", "Obstruction");
            }
        }
        for (Window w : this.windows) {
            boolean isWindowInitiallyOpen = w.isOpen();
            w.close();
            boolean isWindowOpenAfter = w.isOpen();
            if (isWindowInitiallyOpen != isWindowOpenAfter) {
                this.associatedSimulator.getLogger().openCloseLog("Window", w.getWindowID(), isWindowOpenAfter ? "Open" : "Closed", "'Away Mode' has been turned on, automatically closing all doors in the house.");
            }
            if (isWindowInitiallyOpen == isWindowOpenAfter) {
                this.associatedSimulator.getLogger().unableToOpenOrCloseLog("Window", w.getWindowID(), isWindowOpenAfter ? "Opened" : "Closed", "Obstruction");
            }
        }
    }

    public void enableAwayMode() {
        if (this.shp.isOn()) {
            this.shp.turnOnAwayMode();
        }
    }

    public void disableAwayMode() {
        if (this.shp.isOn()) {
            this.shp.turnOffAwayMode();
        }
    }

    public void setTimeForAlert(int newSeconds) {
        if (this.shp.isOn()) {
            this.shp.setTimeForAlert(newSeconds);
        }
    }

    public void addSHPListener(SHPListener newListener) {
        this.shp.addListener(newListener);
    }

    public void startAll15DegreeTimers() {
        for (Room r : this.rooms) {
            if (r.getFifteenDegreeTimer().isRunning() == false) {
                r.start15DegreeTimer();
            }
        }
    }

    public void stopAll15DegreeTimers() {
        for (Room r : this.rooms) {
            r.stop15DegreeTimer();
        }
    }

    public void stopAllIntruderAlertTimers() {
        for (Room r : this.rooms) {
            r.stopIntruderAlertTimer();
        }
    }

    public void setTempOfRoom(String roomID, double newTemp) {
        for (int i = 0;i < this.rooms.size();i++) {
            Room r = this.rooms.get(i);
            if (r.getRoomID().equals(roomID)) {
                r.setCurrentTemperature(newTemp);
                this.rooms.set(i, r);
                return;
            }
        }
    }
}
