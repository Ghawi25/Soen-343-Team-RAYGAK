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
        return new ResponseEntity<Logs>(new Logs("House temperature: " + houseRef.getIndoorTemperature()),
                HttpStatus.OK);
    }
}
