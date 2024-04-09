package com.raygak.server.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZoneWrapper {
    private String ZoneName;
    private String ZoneType;
    private ArrayList<String> roomIds = new ArrayList<>();
}
