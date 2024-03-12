package com.raygak.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
// Imports
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.raygak.server.services.HouseService;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/house")
public class HouseController {
    private final HouseService houseService;

    @Autowired
    public HouseController(HouseService houseService) {
        userService =
    }
}
