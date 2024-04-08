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
public class RoomWrapper {
    private ArrayList<String> roomIds = new ArrayList<>();
}
