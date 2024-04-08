package com.raygak.server.modules;

import com.raygak.server.mediation.Mediator;

public abstract class Module {
    protected Mediator mediator;

    public Module(Mediator mediator) {
        this.mediator = mediator;
    }

    public abstract void triggerEvent(String event);
}
