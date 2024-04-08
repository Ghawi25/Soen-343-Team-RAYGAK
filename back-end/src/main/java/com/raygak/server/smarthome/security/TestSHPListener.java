package com.raygak.server.smarthome.security;

public class TestSHPListener extends SHPListener {
    @Override
    public void update(SHPEventType eventType) {
        if (eventType.equals(SHPEventType.HOUSE_TEMP_AT_135_DEGREES)) {
            System.out.println("[SHP] WARNING: TEMPERATURE OF HOUSE HAS REACHED 135 DEGREES!");
        }
        if (eventType.equals(SHPEventType.ROOM_TEMP_UP_15_IN_1_MINUTE)) {
            System.out.println("[SHP] DANGER: TEMPERATURE INCREASE OF 15 DEGREES IN 1 MINUTE");
        }
    }

    @Override
    public void update(SHPEventType eventType, String identifier) {
        if (eventType.equals(SHPEventType.ROOM_TEMP_AT_135_DEGREES)) {
            System.out.println("[SHP] WARNING: TEMPERATURE OF ROOM " + identifier + " HAS REACHED 135 DEGREES!");
        }
        if (eventType.equals(SHPEventType.HOUSE_TEMP_AT_135_DEGREES)) {
            System.out.println("[SHP] WARNING: TEMPERATURE OF HOUSE HAS REACHED 135 DEGREES!");
        }
        if (eventType.equals(SHPEventType.AUTHORITIES_ALERTED)) {
            System.out.println("[SHP] WARNING: INTRUDER WITH EMAIL " + identifier + " IS STILL IN THE HOUSE! ALERTING AUTHORITIES!");
        }
        if (eventType.equals(SHPEventType.MOTION_DETECTED)) {
            System.out.println("[SHP] WARNING: INTRUDER WITH EMAIL " + identifier + " IS MOVING AROUND IN THE HOUSE!");
        }
        if (eventType.equals(SHPEventType.DOOR_OPENED)){
            System.out.println("[SHP] WARNING: DOOR " + identifier + " IS OPEN!");
        }
        if (eventType.equals(SHPEventType.WINDOW_OPENED)){
            System.out.println("[SHP] WARNING: WINDOW " + identifier + " IS OPEN!");
        }
    }
}
