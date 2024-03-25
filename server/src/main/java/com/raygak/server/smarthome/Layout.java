package com.raygak.server.smarthome;

// Imports

import lombok.Getter;
import org.json.simple.JSONObject;
import com.raygak.server.utils.LayoutReader;

public class Layout {
    @Getter
    JSONObject layoutJSON;

    public Layout() {
        LayoutReader lr = new LayoutReader();
        layoutJSON = lr.readFile();
    }

    ;


}
