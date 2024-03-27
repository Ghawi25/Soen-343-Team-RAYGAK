package com.raygak.server.controllers;

import com.raygak.server.models.Logs;
import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.HouseView;
import com.raygak.server.smarthome.heating.Demo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Logs> getTemperature() {
        Demo temperature = new Demo();
        House houseRef = temperature.initialize();
        Logs logs = new Logs("House temperature: " + houseRef.getIndoorTemperature());
        logs.addMsg("Room 1 temperature: " + houseRef.getRooms().get(0).getCurrentTemperature());
        logs.addMsg("Room 2 temperature: " + houseRef.getRooms().get(1).getCurrentTemperature());
        logs.addMsg(houseRef.getRoomByID("1").isHVACOn() ? "Room 1 - HVAC On" : "Room 1 - HVAC Off");
        logs.addMsg(houseRef.getDoorByName("Room 1 Door").isOpen() ? "Door 1 Open" : "Door 1 Closed");
        logs.addMsg(houseRef.getDoorByName("Room 2 Door").isOpen() ? "Door 2 Open" : "Door 2 Closed");
        return new ResponseEntity<Logs>(logs,
                HttpStatus.OK);
    }
}
