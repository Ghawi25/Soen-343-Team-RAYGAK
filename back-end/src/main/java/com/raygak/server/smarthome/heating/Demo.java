package com.raygak.server.smarthome.heating;

import com.raygak.server.smarthome.*;
import com.raygak.server.smarthome.security.TestSHPListener;
import com.raygak.server.timers.SmartHomeTimer;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class Demo {

    public static void main(String[] args) {
        User p1 = new User("james@gmail.com", "james123", UserType.PARENT);
        User p2 = new User("quinn@gmail.com", "quinn456", UserType.CHILD);
        User p3 = new User("dennis@gmail.com", "dennis789", UserType.STRANGER);
        User sim_user = new User("sim_user@gmail.com", "sim_user123", UserType.SIM_USER);

        ArrayList<User> userList1 = new ArrayList<User>();
        userList1.add(p1);
        userList1.add(p2);

        ArrayList<User> userList2 = new ArrayList<User>();

        Window w1 = new Window("1", false, false, "west");
        Window w2 = new Window("2", true, false, "west");
        Window w3 = new Window("3", false, true, "west");
        Window w4 = new Window("4", true, true, "west");

        ArrayList<Window> windowList1 = new ArrayList<Window>();
        windowList1.add(w1);
        windowList1.add(w2);

        ArrayList<Window> windowList2 = new ArrayList<Window>();
        windowList2.add(w3);
        windowList2.add(w4);

        Light l1 = new Light("Room 1 Light", "Centre");
        Light l2 = new Light("Room 2 Light", "Centre");

        Door d1 = new Door("Room 1 Door", "Bottom Centre");
        Door d2 = new Door("Room 2 Door", "Bottom Centre");

        ArrayList<Door> doorList1 = new ArrayList<Door>();
        doorList1.add(d1);

        ArrayList<Door> doorList2 = new ArrayList<Door>();
        doorList2.add(d2);
        Room r1 = new Room("1", "room1", 1, 1, l1, true, 45.0, false, windowList1, doorList1, userList1, null, null, null, null);
        Room r2 = new Room("2", "room2", 1, 1, l2, true, 42.0, false, windowList2, doorList2, userList2, r1, null, null, null);
//        Room r1 = new Room("1", "room1", 1, 1, l1, true, 345.0, false, windowList1, doorList1, userList1, null, null, null, null);
//        Room r2 = new Room("2", "room2", 1, 1, l2, true, 345.0, false, windowList2, doorList2, userList2, r1, null, null, null);
        r1.setBottomAdjacentRoom(r2);

        ArrayList<Room> roomList1 = new ArrayList<Room>();
        roomList1.add(r1);
        ArrayList<Room> roomList2 = new ArrayList<Room>();
        roomList2.add(r2);
        ArrayList<Room> allRooms = new ArrayList<Room>();
        allRooms.add(r1);
        allRooms.add(r2);

        TemperatureSetting temp1 = new TemperatureSetting("1", 26.0, 0, 0, 8, 0);
        TemperatureSetting temp2 = new TemperatureSetting("2", 27.5, 8, 1, 16, 0);
        TemperatureSetting temp3 = new TemperatureSetting("3", 24.5, 16, 1, 23, 59);
//        TemperatureSetting temp1 = new TemperatureSetting("1", 326.0, 0, 0, 8, 0);
//        TemperatureSetting temp2 = new TemperatureSetting("2", 327.5, 8, 1, 16, 0);
//        TemperatureSetting temp3 = new TemperatureSetting("3", 324.5, 16, 1, 23, 59);
        ArrayList<TemperatureSetting> settingList1 = new ArrayList<TemperatureSetting>();
        settingList1.add(temp1);
        settingList1.add(temp2);
        settingList1.add(temp3);
        Zone z1 = new Zone("1", ZoneType.HEATING, settingList1, roomList1);

        TemperatureSetting temp4 = new TemperatureSetting("4", 39.0, 0, 0, 8, 0);
        TemperatureSetting temp5 = new TemperatureSetting("5", 42.5, 8, 1, 16, 0);
        TemperatureSetting temp6 = new TemperatureSetting("6", 40.5, 16, 1, 23, 59);
//        TemperatureSetting temp4 = new TemperatureSetting("4", 339.0, 0, 0, 8, 0);
//        TemperatureSetting temp5 = new TemperatureSetting("5", 342.5, 8, 1, 16, 0);
//        TemperatureSetting temp6 = new TemperatureSetting("6", 340.5, 16, 1, 23, 59);
        ArrayList<TemperatureSetting> settingList2 = new ArrayList<TemperatureSetting>();
        settingList2.add(temp4);
        settingList2.add(temp5);
        settingList2.add(temp6);
        Zone z2 = new Zone("2", ZoneType.COOLING, settingList2, roomList2);

        ArrayList<Zone> zoneList = new ArrayList<Zone>();
        zoneList.add(z1);
        zoneList.add(z2);

        House h1 = new House(allRooms, zoneList, 35.0, Season.SUMMER);
//        House h1 = new House(allRooms, zoneList, 335.0, Season.SUMMER);
        TestSHPListener testListener = new TestSHPListener();
        h1.addSHPListener(testListener);
        //The current date
        Date today = new Date();
        LocalTime now = LocalTime.now();
        Simulator simulator = new Simulator(today.getDay(), today.getMonth(), today.getYear(), now.getHour(), now.getMinute(), h1);
        sim_user.setAssociatedHouse(h1);
        simulator.setCurrentUser(sim_user);
        simulator.turnOn();
        simulator.turnOnSHH();
        simulator.turnOnSHP();
        simulator.enableAwayMode();
        simulator.setTimeForAlert(3);
//        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//        System.out.println("House temperature: " + h1.getIndoorTemperature());
//        System.out.println("Outdoor temperature: " + h1.getOutdoorTemperature());
        simulator.removeInhabitant("james@gmail.com");
        simulator.removeInhabitant("quinn@gmail.com");
//        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//        System.out.println("House temperature: " + h1.getIndoorTemperature());
//        System.out.println("Outdoor temperature: " + h1.getOutdoorTemperature());
        simulator.addInhabitantToRoom(p3, "1");
//        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//        System.out.println("House temperature: " + h1.getIndoorTemperature());
//        System.out.println("Outdoor temperature: " + h1.getOutdoorTemperature());
        simulator.addInhabitantToRoom(p2, "2");
        simulator.removeInhabitant("quinn@gmail.com");
//        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//        System.out.println("House temperature: " + h1.getIndoorTemperature());
//        System.out.println("Outdoor temperature: " + h1.getOutdoorTemperature());
//        simulator.removeInhabitant("dennis@gmail.com");
//        simulator.removeInhabitant("quinn@gmail.com");
        simulator.setCurrentSeason(Season.WINTER);
//        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//        System.out.println("House temperature: " + h1.getIndoorTemperature());
//        System.out.println("Outdoor temperature: " + h1.getOutdoorTemperature());
        simulator.setCurrentSeason(Season.SUMMER);
//        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//        System.out.println("House temperature: " + h1.getIndoorTemperature());
//        System.out.println("Outdoor temperature: " + h1.getOutdoorTemperature());
        simulator.setOutdoorTemperature(10.0);
//        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//        System.out.println("House temperature: " + h1.getIndoorTemperature());
//        System.out.println("Outdoor temperature: " + h1.getOutdoorTemperature());
        simulator.setOutdoorTemperature(50.0);
//        simulator.setOutdoorTemperature(350.0);

        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
        System.out.println("House temperature: " + h1.getIndoorTemperature());
        System.out.println("Outdoor temperature: " + h1.getOutdoorTemperature());

        System.out.println(h1.getDoorByName("Room 1 Door").isOpen() ? "Door 1 Open" : "Door 1 Closed");
        simulator.openDoorWithName("Room 1 Door");
        System.out.println(h1.getDoorByName("Room 1 Door").isOpen() ? "Door 1 Open" : "Door 1 Closed");
        System.out.println(h1.getDoorByName("Room 2 Door").isOpen() ? "Door 2 Open" : "Door 2 Closed");
        simulator.openDoorWithName("Room 2 Door");
        System.out.println(h1.getDoorByName("Room 2 Door").isOpen() ? "Door 2 Open" : "Door 2 Closed");
        simulator.closeDoorWithName("Room 2 Door");
        System.out.println(h1.getDoorByName("Room 2 Door").isOpen() ? "Door 2 Open" : "Door 2 Closed");

        System.out.println(h1.getLightByName("Room 1 Light").isOn() ? "Light 1 On" : "Light 1 Off");
        simulator.turnOnLightWithName("Room 1 Light");
        System.out.println(h1.getLightByName("Room 1 Light").isOn() ? "Light 1 On" : "Light 1 Off");
        System.out.println(h1.getLightByName("Room 2 Light").isOn() ? "Light 2 On" : "Light 2 Off");
        simulator.turnOnLightWithName("Room 2 Light");
        System.out.println(h1.getLightByName("Room 2 Light").isOn() ? "Light 2 On" : "Light 2 Off");
        simulator.turnOffLightWithName("Room 2 Light");
        System.out.println(h1.getLightByName("Room 2 Light").isOn() ? "Light 2 On" : "Light 2 Off");

        System.out.println(h1.getRoomByID("1").isHVACOn() ? "Room 1 - HVAC On" : "Room 1 - HVAC Off");
        simulator.turnOnHVACInRoomWithID("1");
        System.out.println(h1.getRoomByID("1").isHVACOn() ? "Room 1 - HVAC On" : "Room 1 - HVAC Off");
        System.out.println(h1.getRoomByID("2").isHVACOn() ? "Room 2 - HVAC On" : "Room 2 - HVAC Off");
        simulator.turnOnHVACInRoomWithID("2");
        System.out.println(h1.getRoomByID("2").isHVACOn() ? "Room 2 - HVAC On" : "Room 2 - HVAC Off");
        simulator.turnOffHVACInRoomWithID("2");
        System.out.println(h1.getRoomByID("2").isHVACOn() ? "Room 2 - HVAC On" : "Room 2 - HVAC Off");

        System.out.println(h1.getWindowByID("1").isOpen() ? "Window 1 Open" : "Window 1 Closed");
        simulator.openWindowWithID("1");
        System.out.println(h1.getWindowByID("1").isOpen() ? "Window 1 Open" : "Window 1 Closed");

        System.out.println(h1.getWindowByID("2").isOpen() ? "Window 2 Open" : "Window 2 Closed");
        simulator.closeWindowWithID("2");
        System.out.println(h1.getWindowByID("2").isOpen() ? "Window 2 Open" : "Window 2 Closed");

        System.out.println(h1.getWindowByID("3").isOpen() ? "Window 3 Open" : "Window 3 Closed");
        simulator.openWindowWithID("3");
        System.out.println(h1.getWindowByID("3").isOpen() ? "Window 3 Open" : "Window 3 Closed");
        simulator.unobstructWindowWithID("3");
        simulator.openWindowWithID("3");
        System.out.println(h1.getWindowByID("3").isOpen() ? "Window 3 Open" : "Window 3 Closed");

        System.out.println(h1.getWindowByID("4").isOpen() ? "Window 4 Open" : "Window 4 Closed");
        simulator.closeWindowWithID("4");
        System.out.println(h1.getWindowByID("4").isOpen() ? "Window 4 Open" : "Window 4 Closed");
        simulator.unobstructWindowWithID("4");
        simulator.closeWindowWithID("4");
        System.out.println(h1.getWindowByID("4").isOpen() ? "Window 4 Open" : "Window 4 Closed");

		int limit1 = 0;
		while (limit1 <= 5) {
			try {
                simulator.updateAllRoomTemperatures();
				System.out.println("Indoor temperature: " + h1.getIndoorTemperature());
				System.out.println("Outdoor temperature: " + h1.getOutdoorTemperature());
                System.out.println("Room 1:");
                simulator.displayTemperatureInRoomWithID("1");
                System.out.println("Room 2:");
                simulator.displayTemperatureInRoomWithID("2");
				limit1++;
				Thread.sleep(1000);
			}
			catch(InterruptedException ie) {
				System.out.println("Limit1 thread interrupted.");
			}
		}
        simulator.setOutdoorTemperature(10.0);
        simulator.setCurrentSeason(Season.WINTER);
        simulator.enableAwayMode();
        simulator.addInhabitantToRoom(p1, "1");
        simulator.addInhabitantToRoom(p2, "2");
        simulator.addInhabitantToRoom(p3, "2");
        simulator.setCurrentUser(p1);
        simulator.changeTemperatureInCurrentRoom_Local(20.0);
        simulator.changeTemperatureInRoom_Remote("2", 20.0);
        simulator.setCurrentUser(p2);
        simulator.changeTemperatureInCurrentRoom_Local(30.0);
        simulator.changeTemperatureInRoom_Remote("1", 30.0);
        simulator.setCurrentUser(p3);
        simulator.changeTemperatureInCurrentRoom_Local(40.0);
        simulator.changeTemperatureInRoom_Remote("1", 40.0);
        simulator.setCurrentUser(sim_user);
//        h1.setTempOfRoom("1", 1000.0);
		int limit2 = 0;
		while (limit2 <= 5) {
			try {
                simulator.updateAllRoomTemperatures();
                System.out.println("Indoor temperature: " + h1.getIndoorTemperature());
                System.out.println("Outdoor temperature: " + h1.getOutdoorTemperature());
                System.out.println("Room 1:");
                simulator.displayTemperatureInRoomWithID("1");
                System.out.println("Room 2:");
                simulator.displayTemperatureInRoomWithID("2");
                if (limit2 == 4) {
                    simulator.enableAwayMode();
                    h1.setTempOfRoom("1", 1000);
                }
				limit2++;
				Thread.sleep(1000);
			}
			catch(InterruptedException ie) {
				System.out.println("Limit2 thread interrupted.");
			}
		}
        h1.stopAll15DegreeTimers();
        h1.stopAllIntruderAlertTimers();
    }

    public House initialize() {
        User p1 = new User("james@gmail.com", "james123", UserType.PARENT);
        User p2 = new User("quinn@gmail.com", "quinn456", UserType.CHILD);
        User p3 = new User("dennis@gmail.com", "dennis789", UserType.STRANGER);

        ArrayList<User> userList1 = new ArrayList<User>();
        userList1.add(p1);
        userList1.add(p2);

        ArrayList<User> userList2 = new ArrayList<User>();

        Window w1 = new Window("1", false, false, "west");
        Window w2 = new Window("2", true, false, "west");
        Window w3 = new Window("3", false, true, "west");
        Window w4 = new Window("4", true, true, "west");

        ArrayList<Window> windowList1 = new ArrayList<Window>();
        windowList1.add(w1);
        windowList1.add(w2);

        ArrayList<Window> windowList2 = new ArrayList<Window>();
        windowList2.add(w3);
        windowList2.add(w4);

        Light l1 = new Light("Room 1 Light", "Centre");
        Light l2 = new Light("Room 2 Light", "Centre");

        Door d1 = new Door("Room 1 Door", "Bottom Centre");
        Door d2 = new Door("Room 2 Door", "Bottom Centre");

        ArrayList<Door> doorList1 = new ArrayList<Door>();
        doorList1.add(d1);

        ArrayList<Door> doorList2 = new ArrayList<Door>();
        doorList2.add(d2);

        Room r1 = new Room("1", "room1", 1, 1, l1, true, 45.0, false, windowList1, doorList1, userList1, null, null, null, null);
        Room r2 = new Room("2", "room2", 1, 1, l2, false, 45.0, false, windowList2, doorList2, userList2, r1, null, null, null);
        r1.setBottomAdjacentRoom(r2);

        ArrayList<Room> roomList1 = new ArrayList<Room>();
        roomList1.add(r1);
        ArrayList<Room> roomList2 = new ArrayList<Room>();
        roomList2.add(r2);
        ArrayList<Room> allRooms = new ArrayList<Room>();
        allRooms.add(r1);
        allRooms.add(r2);

        TemperatureSetting temp1 = new TemperatureSetting("1", 46.0, 0, 0, 8, 0);
        TemperatureSetting temp2 = new TemperatureSetting("2", 43.5, 8, 1, 16, 0);
        TemperatureSetting temp3 = new TemperatureSetting("3", 47.5, 16, 1, 23, 59);
        ArrayList<TemperatureSetting> settingList1 = new ArrayList<TemperatureSetting>();
        settingList1.add(temp1);
        settingList1.add(temp2);
        settingList1.add(temp3);
        Zone z1 = new Zone("1", ZoneType.HEATING, settingList1, roomList1);

        TemperatureSetting temp4 = new TemperatureSetting("4", 39.0, 0, 0, 8, 0);
        TemperatureSetting temp5 = new TemperatureSetting("5", 42.5, 8, 1, 16, 0);
        TemperatureSetting temp6 = new TemperatureSetting("6", 40.5, 16, 1, 23, 59);
        ArrayList<TemperatureSetting> settingList2 = new ArrayList<TemperatureSetting>();
        settingList2.add(temp4);
        settingList2.add(temp5);
        settingList2.add(temp6);
        Zone z2 = new Zone("2", ZoneType.COOLING, settingList2, roomList2);

        ArrayList<Zone> zoneList = new ArrayList<Zone>();
        zoneList.add(z1);
        zoneList.add(z2);

        House h1 = new House(allRooms, zoneList, 40.0, Season.WINTER);
        h1.turnOnSHH();
        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
        System.out.println("House temperature: " + h1.getIndoorTemperature());
        h1.removeInhabitant("james@gmail.com");
        h1.removeInhabitant("quinn@gmail.com");
        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
        System.out.println("House temperature: " + h1.getIndoorTemperature());
        h1.addInhabitantToRoom(p3, "1");
        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
        System.out.println("House temperature: " + h1.getIndoorTemperature());
        h1.addInhabitantToRoom(p2, "2");
        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
        System.out.println("House temperature: " + h1.getIndoorTemperature());
        h1.removeInhabitant("dennis@gmail.com");
        System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
        System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
        System.out.println("House temperature: " + h1.getIndoorTemperature());
        h1.setOutdoorTemperature(10.0);
        h1.setOutdoorTemperature(50.0);

        System.out.println(h1.getDoorByName("Room 1 Door").isOpen() ? "Door 1 Open" : "Door 1 Closed");
        h1.openDoorWithName("Room 1 Door");
        System.out.println(h1.getDoorByName("Room 1 Door").isOpen() ? "Door 1 Open" : "Door 1 Closed");
        System.out.println(h1.getDoorByName("Room 2 Door").isOpen() ? "Door 2 Open" : "Door 2 Closed");
        h1.openDoorWithName("Room 2 Door");
        System.out.println(h1.getDoorByName("Room 2 Door").isOpen() ? "Door 2 Open" : "Door 2 Closed");
        h1.closeDoorWithName("Room 2 Door");
        System.out.println(h1.getDoorByName("Room 2 Door").isOpen() ? "Door 2 Open" : "Door 2 Closed");

        System.out.println(h1.getLightByName("Room 1 Light").isOn() ? "Light 1 On" : "Light 1 Off");
        h1.turnOnLightWithName("Room 1 Light");
        System.out.println(h1.getLightByName("Room 1 Light").isOn() ? "Light 1 On" : "Light 1 Off");
        System.out.println(h1.getLightByName("Room 2 Light").isOn() ? "Light 2 On" : "Light 2 Off");
        h1.turnOnLightWithName("Room 2 Light");
        System.out.println(h1.getLightByName("Room 2 Light").isOn() ? "Light 2 On" : "Light 2 Off");
        h1.turnOffLightWithName("Room 2 Light");
        System.out.println(h1.getLightByName("Room 2 Light").isOn() ? "Light 2 On" : "Light 2 Off");

        System.out.println(h1.getRoomByID("1").isHVACOn() ? "Room 1 - HVAC On" : "Room 1 - HVAC Off");
        h1.turnOnHVACInRoomWithID("1");
        System.out.println(h1.getRoomByID("1").isHVACOn() ? "Room 1 - HVAC On" : "Room 1 - HVAC Off");
        System.out.println(h1.getRoomByID("2").isHVACOn() ? "Room 2 - HVAC On" : "Room 2 - HVAC Off");
        h1.turnOnHVACInRoomWithID("2");
        System.out.println(h1.getRoomByID("2").isHVACOn() ? "Room 2 - HVAC On" : "Room 2 - HVAC Off");
        h1.turnOffHVACInRoomWithID("2");
        System.out.println(h1.getRoomByID("2").isHVACOn() ? "Room 2 - HVAC On" : "Room 2 - HVAC Off");
        return h1;
    }
}
