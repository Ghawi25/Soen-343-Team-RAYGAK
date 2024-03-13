package com.raygak.server.services;

// Imports
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.raygak.server.smarthome.HouseView;
import com.raygak.server.smarthome.heating.House;
import com.raygak.server.smarthome.heating.Room;
import com.raygak.server.smarthome.heating.Season;
import com.raygak.server.smarthome.heating.Zone;

@Service
public class HouseService {
    private HouseView houseView;
    private House house;

    public HouseService() {
        houseView = HouseView.getHome();
        house = houseView.house;
    }

    public double getIndoorTemperature() {
        return house.getIndoorTemperature();
    }

    public void setIndoorTemperature(double temp) {
        house.setIndoorTemperature(temp);
    }

    public double getOutdoorTemperature() {
        return house.getOutdoorTemperature();
    }

    public void setOutdoorTemperature(double temp) {
        house.setOutdoorTemperature(temp);
    }

    public Season getCurrentSeason() {
        return house.getCurrentSeason();
    }

    public void setCurrentSeason(Season s) {
        house.setCurrentSeason(s);
    }

    public ArrayList<Room> getRooms() {
        return house.getRooms();
    }

    public Room getRoomById(String id) {
        Room room = null;

        for (Room r : house.getRooms()) {
            if (r.getRoomID().equals(id)) {
                room = r;
                break;
            }
        }
        return room;
    }

    public ArrayList<Zone> getZones() {
        return house.getZones();
    }

    public Zone getZoneById(String id) {
        Zone zone = null;

        for (Zone z: house.getZones()){
            if(z.getZoneID().equals(id)) {
                zone = z;
                break;
            }
        }
        return zone;
    }

    public House getHouse() {
        return house;
    }

}
