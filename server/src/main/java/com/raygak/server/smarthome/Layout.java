package com.raygak.server.smarthome;

// Imports
import org.json.simple.JSONObject;
import com.raygak.server.utils.LayoutReader;

public class Layout {
    private JSONObject layoutJSON;

    public Layout() {
        LayoutReader lr = new LayoutReader();
        layoutJSON = lr.readFile();
        System.out.println(layoutJSON);
    };


}
