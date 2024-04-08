package com.raygak.server;

import com.raygak.server.smarthome.*;
import com.raygak.server.smarthome.heating.Season;
import com.raygak.server.smarthome.heating.TemperatureSetting;
import com.raygak.server.smarthome.heating.Zone;
import com.raygak.server.smarthome.heating.ZoneType;
import com.raygak.server.states.SimulationOnAndSHHOffAndSHPOffState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@SpringBootTest
class ServerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testStateChange() {
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
		Room r2 = new Room("2", "room2", 1, 1, l2, true, 42.0, false, windowList2, doorList2, userList2, r1, null, null, null);
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

		House h1 = new House(allRooms, zoneList, 35.0, Season.SUMMER);
		Date today = new Date();
		LocalTime now = LocalTime.now();
		Simulator simulator = new Simulator(today.getDay(), today.getMonth(), today.getYear(), now.getHour(), now.getMinute(), h1);
		simulator.turnOn();
		assertTrue(simulator.getCurrentState() instanceof SimulationOnAndSHHOffAndSHPOffState);

	}

}
