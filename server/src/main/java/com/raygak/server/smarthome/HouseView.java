package com.raygak.server.smarthome;

public final class HouseView {
    private static HouseView home;
    private HouseView() {
        // Init our home instance here
        // Calls file-reader method and initialize the house.
    }

    public static HouseView getHome() {
        if (home == null) {
            home = new HouseView();
        }
        return home;
    }
}
