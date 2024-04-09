package com.raygak.server.controllers;

import com.raygak.server.models.ZoneWrapper;
import com.raygak.server.models.TemperatureSettingPOJO;
import com.raygak.server.models.ZonePOJO;
import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.HouseView;
import com.raygak.server.smarthome.Room;
import com.raygak.server.smarthome.heating.TemperatureSetting;
import com.raygak.server.smarthome.heating.Zone;
import com.raygak.server.smarthome.heating.ZoneType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/api/SHH")
public class SHHController {
    @GetMapping(path = "/zones")
    public ResponseEntity<ArrayList<ZonePOJO>> getZones() {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        ArrayList<Zone> zoneList = houseRef.getZones();
        ArrayList<ZonePOJO> zonePOJOs = new ArrayList<>();
        for(Zone zone : zoneList) {
            // Get Room Ids inside the zone
            ArrayList<Room> roomsList = zone.getRoomList();
            ArrayList<String> roomIds = new ArrayList<>();
            for(Room room : roomsList) {
                roomIds.add(room.getRoomID());
            }
            // Get temperature settings
            ArrayList<TemperatureSetting> settingList = zone.getSettingList();
            ArrayList<TemperatureSettingPOJO> temperatureSettingPOJOS = new ArrayList<>();
            for(TemperatureSetting setting : settingList) {
                TemperatureSettingPOJO settingPOJO = new TemperatureSettingPOJO(setting.getSettingID(),
                        setting.getDesiredTemperature(), setting.getOriginalDesiredTemperature(), setting.getStart(), setting.getEnd());
                temperatureSettingPOJOS.add(settingPOJO);
            }
            ZonePOJO zonePOJO = new ZonePOJO(zone.getZoneID(),
                    zone.getType(), temperatureSettingPOJOS, roomIds);
            zonePOJOs.add(zonePOJO);
        }
        return new ResponseEntity<ArrayList<ZonePOJO>>(zonePOJOs,
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/zones/{zoneId}")
    public ResponseEntity deleteZoneById(@PathVariable("zoneId") String zoneId) {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        try {
            houseRef.removeZone(zoneId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Error: The zone with the provided ID does not exist.");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/rooms/{roomId}")
    public ResponseEntity<HttpStatus> updateRoomTemp(@PathVariable(value = "roomId") String roomId, @RequestParam("temp") Double temperature) {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        ArrayList<Room> roomsList = houseRef.getRooms();
        for(Room room : roomsList) {
            if(room.getRoomID().equals(roomId)) {
                room.setCurrentTemperature(temperature);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/zones")
    public ResponseEntity<HttpStatus> updateRoomTemp(@RequestBody ZoneWrapper newZone) {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        ArrayList<Room> allRooms = houseRef.getRooms();
        ArrayList<Room> roomsList = new ArrayList<>();

        for(String id: newZone.getRoomIds()) {
            for(Room room : allRooms) {
                if(room.getRoomID().equals(id)) {
                    roomsList.add(room);
                }
            }
        }

        TemperatureSetting temp1 = new TemperatureSetting("1", 46.0, 0, 0, 8, 0);
        TemperatureSetting temp2 = new TemperatureSetting("2", 43.5, 8, 1, 16, 0);
        TemperatureSetting temp3 = new TemperatureSetting("3", 47.5, 16, 1, 23, 59);
        ArrayList<TemperatureSetting> settingList = new ArrayList<TemperatureSetting>();
        settingList.add(temp1);
        settingList.add(temp2);
        settingList.add(temp3);

        Zone zone = new Zone(newZone.getZoneName(), ZoneType.HEATING, settingList, roomsList);
        try {
            houseRef.addZone(zone);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/heatingSystem/{isHeatingOn}")
    public ResponseEntity<Boolean> toggleHeatingSys(@PathVariable(value = "isHeatingOn") Boolean isHeatingOn) {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        if (isHeatingOn) {
            houseRef.turnOnSHH();
        } else {
            houseRef.turnOffSHH();
        }
        return new ResponseEntity<>(houseRef.getShh().isOn(), HttpStatus.OK);
    }
}
