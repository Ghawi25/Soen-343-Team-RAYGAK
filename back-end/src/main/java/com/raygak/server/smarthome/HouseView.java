package com.raygak.server.smarthome;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HouseView {
    private static HouseView home;
    public House house;

    private HouseView() {
        // Init our home instance here
        // Calls file-reader method and initialize the house.
        Layout layout = new Layout();
        JSONObject houseObj = layout.getLayoutJSON();
        JSONArray roomsArr = (JSONArray) houseObj.get("rooms");
        ArrayList<Room> rooms = new ArrayList<>();
        Map<String, Map<String, String>> adjacencyMap = new HashMap<>();
        for (JSONObject room : (Iterable<JSONObject>) roomsArr) {
            ArrayList<Window> windows = new ArrayList<>();
            ArrayList<Door> doors = new ArrayList<>();
            String id = (String) room.get("_id");
            String name = (String) room.get("name");
            int width = Integer.parseInt(room.get("width").toString());
            int height = Integer.parseInt(room.get("height").toString());
            Boolean light = (Boolean) room.get("lightInRoom");
            JSONObject windowsOBJ = (JSONObject) room.get("windows");
            JSONObject doorsOBJ = (JSONObject) room.get("doors");
            JSONObject adjacencyOBJ = (JSONObject) room.get("adjacentTo");
            Map<String, String> currRoomAdjacency = new HashMap<>();

            // Setting adjacencies in map
            String adjacencyString = (String) adjacencyOBJ.get("north");
            currRoomAdjacency.put("north", adjacencyString);
            adjacencyString = (String) adjacencyOBJ.get("south");
            currRoomAdjacency.put("south", adjacencyString);
            adjacencyString = (String) adjacencyOBJ.get("east");
            currRoomAdjacency.put("east", adjacencyString);
            adjacencyString = (String) adjacencyOBJ.get("west");
            currRoomAdjacency.put("west", adjacencyString);
            adjacencyMap.put(id, currRoomAdjacency);

            Room newRoom = new Room(id, name, width, height, light);

            JSONArray windowsArr = (JSONArray) windowsOBJ.get("north");
            for (Object dir : windowsArr) {
                String wId = (String) dir;
                windows.add(new Window(wId, false, false));
            }

            windowsArr = (JSONArray) windowsOBJ.get("south");
            for (Object dir : windowsArr) {
                String wId = (String) dir;
                windows.add(new Window(wId, false, false));
            }

            windowsArr = (JSONArray) windowsOBJ.get("east");
            for (Object dir : windowsArr) {
                String wId = (String) dir;
                windows.add(new Window(wId, false, false));
            }

            windowsArr = (JSONArray) windowsOBJ.get("west");
            for (Object dir : windowsArr) {
                String wId = (String) dir;
                windows.add(new Window(wId, false, false));
            }

            newRoom.setWindows(windows);

            String doorName = (String) doorsOBJ.get("north");
            if (!doorName.equals("")) {
                doors.add(new Door(doorName, "north"));
            }

            doorName = (String) doorsOBJ.get("south");
            if (!doorName.equals("")) {
                doors.add(new Door(doorName, "south"));
            }

            doorName = (String) doorsOBJ.get("east");
            if (!doorName.equals("")) {
                doors.add(new Door(doorName, "east"));
            }

            doorName = (String) doorsOBJ.get("west");
            if (!doorName.equals("")) {
                doors.add(new Door(doorName, "west"));
            }

            newRoom.setDoors(doors);

            rooms.add(newRoom);
        }
        for (Room room : rooms) {
            // get map of adjacencies
            Map<String, String> roomAdjacencies = adjacencyMap.get(room.getRoomID());
            for (Map.Entry<String, String> adjacencyEntry : roomAdjacencies.entrySet()) {
                // Skip if there is no adjacency
                if(adjacencyEntry.getValue().isEmpty()) continue;
                // get room id of adjacent room
                String adjacentRoomId = "";
                int adjacentRoomWidth = 0, adjacentRoomHeight = 0;
                for (Room adjacentRoom : rooms) {
                    if(adjacencyEntry.getValue().equals(adjacentRoom.getName())) {
                        adjacentRoomId = adjacentRoom.getRoomID();
                        adjacentRoomWidth = adjacentRoom.getWidth();
                        adjacentRoomHeight = adjacentRoom.getHeight();
                        break;
                    }
                }
                Room adjacentRoom = new Room(adjacentRoomId, adjacencyEntry.getValue(), adjacentRoomWidth, adjacentRoomHeight, true);
                switch (adjacencyEntry.getKey()) {
                    case "north" -> room.setTopAdjacentRoom(adjacentRoom);
                    case "south" -> room.setBottomAdjacentRoom(adjacentRoom);
                    case "west" -> room.setLeftAdjacentRoom(adjacentRoom);
                    default -> room.setRightAdjacentRoom(adjacentRoom);
                }
            }
        }

        house = new House(rooms);
    }

    public static HouseView getHome() {
        if (home == null) {
            home = new HouseView();
        }
        return home;
    }
}
