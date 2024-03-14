package com.raygak.server.smarthome;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public final class HouseView {
    private static HouseView home;
    public House house;
    ArrayList<Room> rooms = new ArrayList<>();

    private HouseView() {
        // Init our home instance here
        // Calls file-reader method and initialize the house.
        Layout layout = new Layout();
        JSONObject houseObj = layout.getLayoutJSON();
        JSONArray roomsArr = (JSONArray) houseObj.get("rooms");
        Iterator<JSONObject> iterator = roomsArr.iterator();
        while (iterator.hasNext()) {
            JSONObject room = iterator.next();
            String id = (String) room.get("_id");
            String name = (String) room.get("name");
            int width = Integer.parseInt(room.get("width").toString());
            int height = Integer.parseInt(room.get("height").toString());
            Boolean light = (Boolean) room.get("lightInRoom");
            Room newRoom = new Room(id, name, width, height, light);
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
