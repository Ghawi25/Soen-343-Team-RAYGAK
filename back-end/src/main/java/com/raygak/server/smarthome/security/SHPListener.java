package com.raygak.server.smarthome.security;

public abstract class SHPListener {
    public abstract void update(SHPEventType eventType);
    public void update(SHPEventType eventType, String identifier) {

    }
}
