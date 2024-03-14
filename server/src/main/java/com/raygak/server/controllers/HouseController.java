package com.raygak.server.controllers;

import com.raygak.server.smarthome.HouseView;
import com.raygak.server.smarthome.heating.House;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/house")
public class HouseController {
    HouseView houseView = HouseView.getHome();

    @GetMapping
    public ResponseEntity<House> getHouse() {
        return new ResponseEntity<House>(houseView.house, HttpStatus.OK);
    }

}
