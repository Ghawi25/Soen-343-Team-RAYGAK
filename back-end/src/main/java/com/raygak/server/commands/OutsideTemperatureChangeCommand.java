package com.raygak.server.commands;

import com.raygak.server.smarthome.House;
import lombok.Getter;

@Getter
public class OutsideTemperatureChangeCommand extends Command {
    private House house;
    private double newOutsideTemperature;

    public OutsideTemperatureChangeCommand(House house, double newOutsideTemperature) {
        this.house = house;
        this.newOutsideTemperature = newOutsideTemperature;
    }

    public void setHouse(House house) {
            this.house = house;
    }

    public void setNewOutsideTemperature(double newOutsideTemperature) {
            this.newOutsideTemperature = newOutsideTemperature;
    }

    @Override
    public void execute() {
        this.house.setOutdoorTemperature(this.newOutsideTemperature);
    }
}
