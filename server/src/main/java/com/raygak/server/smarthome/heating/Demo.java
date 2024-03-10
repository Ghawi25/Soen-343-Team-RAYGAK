import java.util.ArrayList;

public class Demo {

	public static void main(String[] args) {
		Person p1 = new Person("1", "Mike", 28);
		Person p2 = new Person("2", "Leanne", 30);
		Person p3 = new Person("3", "Chuck", 48);
		
		ArrayList<Person> personList1 = new ArrayList<Person>();
		personList1.add(p1);
		personList1.add(p2);
		
		ArrayList<Person> personList2 = new ArrayList<Person>();
		
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
		
		Room r1 = new Room("1", 1, 1, 45.0, false, windowList1, personList1, null, null, null, null);
		Room r2 = new Room("2", 1, 1, 41.0, true, windowList2, personList2, r1, null, null, null);
		
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
		h1.removeInhabitant("1");
		h1.removeInhabitant("2");
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
		h1.removeInhabitant("3");
		System.out.println("Room 1 temperature: " + h1.getRooms().get(0).getCurrentTemperature());
		System.out.println("Room 2 temperature: " + h1.getRooms().get(1).getCurrentTemperature());
		System.out.println("House temperature: " + h1.getIndoorTemperature());
		h1.setOutdoorTemperature(10.0);
		h1.setOutdoorTemperature(50.0);
	}

}
