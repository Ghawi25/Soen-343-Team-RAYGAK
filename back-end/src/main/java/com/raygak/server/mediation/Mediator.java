package com.raygak.server.mediation;

import com.raygak.server.modules.Module;

public interface Mediator {
    public void notify(Module module, String event);
}
