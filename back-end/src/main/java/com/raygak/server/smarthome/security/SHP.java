package com.raygak.server.smarthome.security;

import com.raygak.server.smarthome.House;
import com.raygak.server.timers.SHP15DegreeTimer;
import com.raygak.server.timers.SmartHomeTimer;
import lombok.Getter;

import java.util.ArrayList;

public class SHP {
    @Getter
    private House house;
    private boolean isOn = false;
  
    private ArrayList<SHPListener> listeners = new ArrayList<SHPListener>();

    private boolean awayMode = false;

    //10 seconds until emergency services are alerted by default.
    private int timeForAlert = 10;

    private SmartHomeTimer SHPTimer = new SmartHomeTimer();

    public SHP(House house) {
        this.house = house;
    }

    public SHP(House house, int timeForAlert) {
        this.house = house;
        this.timeForAlert = timeForAlert;
    }

    public boolean getIsOn() {
        return this.isOn;
    }

    public boolean getAwayMode() {
        return this.awayMode;
    }

    public SmartHomeTimer getSHPTimer() {
        return this.SHPTimer;
    }

    public int getTimeForAlert() {
        return this.timeForAlert;
    }

    public void turnOn() {
        this.isOn = true;
    }

    public void turnOff() {
        this.isOn = false;
    }

    public void turnOnAwayMode() {
        this.awayMode = true;
        this.house.closeAllDoorsAndWindows();
        this.house.startAll15DegreeTimers();
    }

    public void turnOffAwayMode() {
        this.awayMode = false;
        this.house.stopAll15DegreeTimers();
    }

    public void setHouse(House newHouse) { this.house = newHouse; }

    public void setTimeForAlert(int newSeconds) {
        this.timeForAlert = newSeconds;
    }

    public void addListener(SHPListener newListener) {
        this.listeners.add(newListener);
    }

    public void removeListener(SHPListener existingListener) {
        this.listeners.remove(existingListener);
    }

    public void notify(SHPEventType eventType) {
        if (this.awayMode) {
            for (SHPListener listener : this.listeners) {
                listener.update(eventType);
            }
        }
    }

    public void notify(SHPEventType eventType, String identifier) {
        if (this.awayMode) {
            for (SHPListener listener : this.listeners) {
                listener.update(eventType, identifier);
            }
        }
    }
}
