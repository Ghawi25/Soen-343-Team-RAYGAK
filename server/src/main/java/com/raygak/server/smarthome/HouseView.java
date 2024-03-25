package com.raygak.server.smarthome;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

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
        for (JSONObject room : (Iterable<JSONObject>) roomsArr) {
            ArrayList<Window> windows = new ArrayList<>();
            ArrayList<String> adjacencies = new ArrayList<>();
            String id = (String) room.get("_id");
            String name = (String) room.get("name");
            int width = Integer.parseInt(room.get("width").toString());
            int height = Integer.parseInt(room.get("height").toString());
            Boolean light = (Boolean) room.get("lightInRoom");
            JSONObject windowsOBJ = (JSONObject) room.get("windows");
            JSONObject adjacencyOBJ = (JSONObject) room.get("adjacentTo");

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

//            String adjacentToString = (String) adjacencyOBJ.get("north");
//            adjacentToString = (String) adjacencyOBJ.get("south");
//            adjacentToString = (String) adjacencyOBJ.get("east");
//            adjacentToString = (String) adjacencyOBJ.get("west");

            // Quick fix because setTopAdjacentRoom takes in a Room type not String
            Room newRoom1 = new Room(id, name, width, height, light);
            newRoom.setTopAdjacentRoom(newRoom1);


            newRoom.setBottomAdjacentRoom(newRoom1);

            newRoom.setRightAdjacentRoom(newRoom1);


            newRoom.setLeftAdjacentRoom(newRoom1);

            rooms.add(newRoom);
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
