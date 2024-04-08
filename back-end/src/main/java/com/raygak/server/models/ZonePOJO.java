package com.raygak.server.models;

import com.raygak.server.smarthome.Room;
import com.raygak.server.smarthome.heating.TemperatureSetting;
import com.raygak.server.smarthome.heating.ZoneType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class ZonePOJO {
    private String zoneID;
    private ZoneType type;
    private ArrayList<TemperatureSettingPOJO> settingList;
    private ArrayList<String> roomIds;
}
