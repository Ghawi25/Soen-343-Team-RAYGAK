package com.raygak.server.controllers;

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

    @DeleteMapping(path = "/zone")
    public ResponseEntity deleteZoneById(@RequestParam("id") String zoneId) {
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
}
