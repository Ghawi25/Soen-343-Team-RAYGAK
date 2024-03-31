package com.raygak.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomPOJO {
    private String roomID;
    private String name;
    private int width;
    private int height;
    private double currentTemperature;

}
