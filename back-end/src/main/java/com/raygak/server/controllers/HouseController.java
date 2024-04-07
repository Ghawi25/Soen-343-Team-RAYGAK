package com.raygak.server.controllers;

import com.raygak.server.models.LogsPOJO;
import com.raygak.server.models.RoomPOJO;
import com.raygak.server.models.TemperatureSettingPOJO;
import com.raygak.server.models.ZonePOJO;
import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.HouseView;
import com.raygak.server.smarthome.Room;
import com.raygak.server.smarthome.heating.TemperatureSetting;
import com.raygak.server.smarthome.heating.Zone;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/api/house")
public class HouseController {
    HouseView houseView = HouseView.getHome();

    @GetMapping
    public ResponseEntity<House> getHouse() {
        return new ResponseEntity<House>(houseView.house, HttpStatus.OK);
    }

    @GetMapping(path = "/temperature")
    public ResponseEntity<LogsPOJO> getTemperature() {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        LogsPOJO logsPOJO = new LogsPOJO("House temperature: " + houseRef.getIndoorTemperature());
        logsPOJO.addMsg("Room 1 temperature: " + houseRef.getRooms().get(0).getCurrentTemperature());
        logsPOJO.addMsg("Room 2 temperature: " + houseRef.getRooms().get(1).getCurrentTemperature());
        logsPOJO.addMsg(houseRef.getRoomByID("1").isHVACOn() ? "Room 1 - HVAC On" : "Room 1 - HVAC Off");
        logsPOJO.addMsg(houseRef.getDoorByName("Room 1 Door").isOpen() ? "Door 1 Open" : "Door 1 Closed");
        logsPOJO.addMsg(houseRef.getDoorByName("Room 2 Door").isOpen() ? "Door 2 Open" : "Door 2 Closed");
        return new ResponseEntity<LogsPOJO>(logsPOJO,
                HttpStatus.OK);
    }

    @GetMapping(path = "/rooms")
    public ResponseEntity<ArrayList<RoomPOJO>> getRooms() {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        ArrayList<Room> roomsList = houseRef.getRooms();
        ArrayList<RoomPOJO> roomPOJOs = new ArrayList<>();
        for(Room room : roomsList) {
            RoomPOJO roomPOJO = new RoomPOJO(room.getRoomID(),
                    room.getName(), room.getWidth(), room.getHeight(),
                    room.getCurrentTemperature());
            roomPOJOs.add(roomPOJO);
        }
        return new ResponseEntity<ArrayList<RoomPOJO>>(roomPOJOs,
                HttpStatus.OK);
    }

    @GetMapping(path = "/room")
    public ResponseEntity<RoomPOJO> getRoomsById(@RequestParam("id") String roomId) {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        ArrayList<Room> roomsList = houseRef.getRooms();
        for(Room room : roomsList) {
            if(room.getRoomID().equals(roomId)) {
                RoomPOJO roomPOJO = new RoomPOJO(room.getRoomID(),
                        room.getName(), room.getWidth(), room.getHeight(),
                        room.getCurrentTemperature());
                return new ResponseEntity<RoomPOJO>(roomPOJO,
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
