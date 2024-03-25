package com.raygak.server;

import com.raygak.server.smarthome.HouseView;
import com.raygak.server.smarthome.heating.Season;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
//        // Example of how to access the singleton class
//        HouseView houseRef = HouseView.getHome();
//        HouseView houseRef2 = HouseView.getHome();
//
//        // Setting season using ref1
//        houseRef.house.setCurrentSeason(Season.AUTUMN);
//
//        // Ref2 should have the same season
//        System.out.println(houseRef2.house.getCurrentSeason());
    }

}
