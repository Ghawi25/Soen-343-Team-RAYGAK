package com.raygak.server.controllers;

import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.HouseView;
import com.raygak.server.smarthome.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/api/SHP")
public class SHPController {
    @PostMapping(path = "/awayMode/{isAwayModeOn}")
    public ResponseEntity<Boolean> toggleHeatingSys(@PathVariable(value = "isAwayModeOn") Boolean isAwayModeOn) {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        if (isAwayModeOn) {
            houseRef.getShp().turnOnAwayMode();
        } else {
            houseRef.getShp().turnOffAwayMode();
        }
        return new ResponseEntity<>(houseRef.getShp().isInAwayMode(), HttpStatus.OK);
    }

    @PostMapping(path = "/closeDoorsWindows")
    public ResponseEntity<HttpStatus> closeDoorsWindows() {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        if (houseRef.getShp().isInAwayMode()) {
            houseRef.closeAllDoorsAndWindows();
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/notificationTimer/{timer}")
    public ResponseEntity<HttpStatus> closeDoorsWindows(@PathVariable(value = "timer") Integer time) {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        try {
            houseRef.getShp().setTimeForAlert(time);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(path = "/motionDetectors/{detectorId}")
    public ResponseEntity<HttpStatus> addNewDetector(@PathVariable(value = "detectorId") String id) {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        ArrayList<Room> roomsList = houseRef.getRooms();
        for (Room room : roomsList) {
            if (room.getRoomID().equals(id)) {
                room.setMotionDetectorInstalled(true);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/motionDetectors/{detectorId}")
    public ResponseEntity<HttpStatus> deleteDetector(@PathVariable(value = "detectorId") String id) {
        HouseView house = HouseView.getHome();
        House houseRef = house.getHouse();
        ArrayList<Room> roomsList = houseRef.getRooms();
        for (Room room : roomsList) {
            if (room.getRoomID().equals(id)) {
                room.setMotionDetectorInstalled(false);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
