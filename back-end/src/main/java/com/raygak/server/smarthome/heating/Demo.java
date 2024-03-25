package com.raygak.server.smarthome.heating;

import com.raygak.server.commands.*;
import com.raygak.server.smarthome.*;

import java.util.ArrayList;

public class Demo {

    public static void main(String[] args) {
        User p1 = new User("james@gmail.com", "james123", UserType.PARENT);
        User p2 = new User("quinn@gmail.com", "quinn456", UserType.CHILD);
        User p3 = new User("dennis@gmail.com", "dennis789", UserType.GUEST);

        ArrayList<User> userList1 = new ArrayList<User>();
        userList1.add(p1);
        userList1.add(p2);

        ArrayList<User> userList2 = new ArrayList<User>();

        Window w1 = new Window("1", false, false);
        Window w2 = new Window("2", true, false);
        Window w3 = new Window("3", false, true);
        Window w4 = new Window("4", true, true);

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

        Room r1 = new Room("1", "room1", 1, 1, l1, 45.0, false, windowList1, doorList1, userList1, null, null, null, null);
        Room r2 = new Room("2", "room2", 1, 1, l2, 45.0, false, windowList2, doorList2, userList2, r1, null, null, null);
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
//		h1.turnOnSHH();
//		System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//		System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//		System.out.println("House temperature: " + h1.getIndoorTemperature());
//		h1.removeInhabitant("james@gmail.com");
//		h1.removeInhabitant("quinn@gmail.com");
//		System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//		System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//		System.out.println("House temperature: " + h1.getIndoorTemperature());
//		h1.addInhabitantToRoom(p3, "1");
//		System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//		System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//		System.out.println("House temperature: " + h1.getIndoorTemperature());
//		h1.addInhabitantToRoom(p2, "2");
//		System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//		System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//		System.out.println("House temperature: " + h1.getIndoorTemperature());
//		h1.removeInhabitant("dennis@gmail.com");
//		System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
//		System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
//		System.out.println("House temperature: " + h1.getIndoorTemperature());
//		h1.setOutdoorTemperature(10.0);
//		h1.setOutdoorTemperature(50.0);
//		int limit1 = 0;
//		while (limit1 <= 10) {
//			try {
//				h1.updateAllRoomTemperatures();
//				System.out.println("Indoor temperature: " + h1.getIndoorTemperature());
//				System.out.println("Outdoor temperature: " + h1.getOutdoorTemperature());
//				limit1++;
//				Thread.sleep(1000);
//			}
//			catch(InterruptedException ie) {
//				System.out.println("Thread interrupted.");
//			}
//		}
//		h1.setOutdoorTemperature(10.0);
//		h1.setCurrentSeason(Season.WINTER);
//		h1.removeInhabitant("quinn@gmail.com");
//		int limit2 = 0;
//		while (limit2 <= 10) {
//			try {
//				h1.updateAllRoomTemperatures();
//				limit2++;
//				Thread.sleep(1000);
//			}
//			catch(InterruptedException ie) {
//				System.out.println("Thread interrupted.");
//			}
//		}
    }

    public House initialize() {
        User p1 = new User("james@gmail.com", "james123", UserType.PARENT);
        User p2 = new User("quinn@gmail.com", "quinn456", UserType.CHILD);
        User p3 = new User("dennis@gmail.com", "dennis789", UserType.GUEST);

        ArrayList<User> userList1 = new ArrayList<User>();
        userList1.add(p1);
        userList1.add(p2);

        ArrayList<User> userList2 = new ArrayList<User>();

        Window w1 = new Window("1", false, false);
        Window w2 = new Window("2", true, false);
        Window w3 = new Window("3", false, true);
        Window w4 = new Window("4", true, true);

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

        Room r1 = new Room("1", "room1", 1, 1, l1, 45.0, false, windowList1, doorList1, userList1, null, null, null, null);
        Room r2 = new Room("2", "room2", 1, 1, l2, 45.0, false, windowList2, doorList2, userList2, r1, null, null, null);
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

        return h1;
    }
}
