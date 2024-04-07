package com.raygak.server.timers;

import com.raygak.server.smarthome.Room;
import com.raygak.server.smarthome.security.SHP;
import com.raygak.server.smarthome.security.SHPEventType;
import lombok.Getter;

@Getter
public class SHP15DegreeTimer extends Thread {

    private double startTemperature;
    private double currentTemperature;
    private long startTime;
    private int numSeconds = 0;
    private boolean running;
    private SHP shp;
        private Room associatedRoom;

    public SHP15DegreeTimer(SHP shp)
    {
        this.shp = shp;
    }

        public SHP15DegreeTimer(Room associatedRoom)
        {
            this.associatedRoom = associatedRoom;
            this.shp = associatedRoom.getAssociatedHouse().getShp();
            this.startTemperature = associatedRoom.getCurrentTemperature();
            this.currentTemperature = associatedRoom.getCurrentTemperature();
        }

        public void setStartTemperature(double newStartTemperature) { this.startTemperature = newStartTemperature; }

        public void setCurrentTemperature(double newCurrentTemperature) { this.currentTemperature = newCurrentTemperature; }

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            running = true;
            while (running) {
                if (System.currentTimeMillis() == startTime + 1000) {
                    startTime = System.currentTimeMillis();
                    this.currentTemperature = this.shp.getHouse().getRoomByID(this.associatedRoom.getRoomID()).getCurrentTemperature();
                    System.out.println("Current temperature in room " + this.associatedRoom.getRoomID() + ": " + this.currentTemperature);
                    System.out.println("Starting temperature in room " + this.associatedRoom.getRoomID() + ": " + this.startTemperature);
                    numSeconds++;
                    if (numSeconds == 60) {
                        startTemperature = currentTemperature;
                        numSeconds = 0;
                    }
                    if (currentTemperature - startTemperature >= 15) {
                        this.shp.notify(SHPEventType.ROOM_TEMP_UP_15_IN_1_MINUTE);
                        stopTimer();
                    }
                }
            }
        }

        public void stopTimer() {
            running = false;
            Thread.currentThread().interrupt();
//            System.out.println("Number of seconds: " + numSeconds);
        }
    }
