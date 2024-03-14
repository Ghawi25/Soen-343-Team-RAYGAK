package com.raygak.server.smarthome;

// Imports

import lombok.Getter;
import org.json.simple.JSONObject;
import com.raygak.server.utils.LayoutReader;

// Use case 1 - Read and load house layout
public class Layout {
    @Getter
    JSONObject layoutJSON;

    public Layout() {
        LayoutReader lr = new LayoutReader();
        layoutJSON = lr.readFile();
    }

    ;


}
