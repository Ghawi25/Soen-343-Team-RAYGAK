package com.raygak.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.raygak.server.services.HouseService;
import com.raygak.server.smarthome.heating.House;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/house")
public class HouseController {
    private final HouseService houseService;

    @Autowired
    public HouseController(HouseService hs) {
        houseService = hs;
    }

    // Get House
    @GetMapping
    public House getHouse() {
        return houseService.getHouse();
    }

}
