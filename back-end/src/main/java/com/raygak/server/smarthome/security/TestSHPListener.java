package com.raygak.server.smarthome.security;

public class TestSHPListener extends SHPListener {
    public void update(SHPEventType eventType) {
        if (eventType.equals(SHPEventType.ROOM_TEMP_UP_15_IN_1_MINUTE)) {
            System.out.println("[SHP] DANGER: TEMPERATURE INCREASE OF 15 DEGREES IN 1 MINUTE");
        }
    }

    @Override
    public void update(SHPEventType eventType, String identifier) {
        if (eventType.equals(SHPEventType.ROOM_TEMP_AT_135_DEGREES)) {
            System.out.println("[SHP] WARNING: TEMPERATURE OF ROOM " + identifier + " HAS REACHED 135 DEGREES!");
        }
    }
}
