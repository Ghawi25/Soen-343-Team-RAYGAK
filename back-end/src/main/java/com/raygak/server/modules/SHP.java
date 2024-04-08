package com.raygak.server.modules;

import com.raygak.server.mediation.Mediator;
import com.raygak.server.smarthome.House;
import com.raygak.server.modules.SHH;
import com.raygak.server.smarthome.Room;
import com.raygak.server.smarthome.security.SHPEventType;
import com.raygak.server.smarthome.security.SHPListener;
import com.raygak.server.timers.SmartHomeTimer;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class SHP extends Module {
    private House house;
    private boolean isOn = false;
    private ArrayList<SHPListener> listeners = new ArrayList<SHPListener>();
    private double houseIndoorTemperature;
    private boolean isInAwayMode = false;

    //10 seconds until emergency services are alerted by default.
    private int timeForAlert = 10;

    private SmartHomeTimer SHPTimer = new SmartHomeTimer();

    public SHP(Mediator mediator, House house) {
        super(mediator);
        this.house = house;
        this.houseIndoorTemperature = house.getIndoorTemperature();
    }

    public SHP(Mediator mediator, House house, int timeForAlert) {
        super(mediator);
        this.house = house;
        this.houseIndoorTemperature = house.getIndoorTemperature();
        this.timeForAlert = timeForAlert;
    }

    public SmartHomeTimer getSHPTimer() {
        return this.SHPTimer;
    }

    public void turnOn() {
        this.isOn = true;
    }

    public void turnOff() {
        this.isOn = false;
    }

    public void turnOnAwayMode() {
        this.isInAwayMode = true;
        this.house.startAll15DegreeTimers();
        this.mediator.notify(this, "Away Mode On");
    }

    public void turnOffAwayMode() {
        this.isInAwayMode = false;
        this.house.stopAll15DegreeTimers();
        this.house.stopAllIntruderAlertTimers();
        this.mediator.notify(this, "Away Mode Off");
    }

    public void setHouse(House newHouse) { this.house = newHouse; }

    public void setTimeForAlert(int newSeconds) {
        this.timeForAlert = newSeconds;
    }

    public void updateIndoorTemperature(double newIndoorTemperature) {
        this.houseIndoorTemperature = newIndoorTemperature;
        if (newIndoorTemperature >= 135.0 && this.isInAwayMode) {
            notify(SHPEventType.HOUSE_TEMP_AT_135_DEGREES);
            this.house.getAssociatedSimulator().getLogger().roomTemperature135DegreesLog("The temperature in the house has reached 135 degrees Celsius.");
        }
        if (isOn && isInAwayMode) {
            for (Room r : this.house.getRooms()) {
                if (r.getCurrentTemperature() >= 135) {
                    notify(SHPEventType.ROOM_TEMP_AT_135_DEGREES, r.getRoomID());
                    this.house.getAssociatedSimulator().getLogger().roomTemperature135DegreesLog("The temperature in room " + r.getRoomID() + " has reached 135 degrees Celsius.");
                    turnOffAwayMode();
                    return;
                }
            }
        }
    }

    public void addListener(SHPListener newListener) {
        this.listeners.add(newListener);
    }

    public void removeListener(SHPListener existingListener) {
        this.listeners.remove(existingListener);
    }

    public void notify(SHPEventType eventType) {
        if (this.isInAwayMode) {
            for (SHPListener listener : this.listeners) {
                listener.update(eventType);
            }
        }
    }

    public void notify(SHPEventType eventType, String identifier) {
        if (this.isInAwayMode) {
            for (SHPListener listener : this.listeners) {
                listener.update(eventType, identifier);
            }
        }
    }

    @Override
    public void triggerEvent(String event) {
        if (event.contains("TemperatureUpdate")) {
            String[] strComponents = event.split(" ");
            updateIndoorTemperature(Double.parseDouble(strComponents[1]));
        }
    }
}
