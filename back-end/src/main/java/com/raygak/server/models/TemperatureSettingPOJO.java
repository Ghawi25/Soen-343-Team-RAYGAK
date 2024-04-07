package com.raygak.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TemperatureSettingPOJO {
    private String settingID;
    private double desiredTemperature;
    private double originalDesiredTemperature;
    private LocalTime start;
    private LocalTime end;
}
