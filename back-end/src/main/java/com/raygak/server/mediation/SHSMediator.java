package com.raygak.server.mediation;

import com.raygak.server.modules.Module;
import com.raygak.server.modules.SHH;
import com.raygak.server.modules.SHP;

public class SHSMediator implements Mediator {
    private SHH shh;
    private SHP shp;

    public void setSHH(SHH newSHH) {
        this.shh = newSHH;
    }

    public void setSHP(SHP newSHP) {
        this.shp = newSHP;
    }

    @Override
    public void notify(Module module, String event) {
        if (module instanceof SHH) {
            if (event.contains("TemperatureUpdate")) {
                this.shp.triggerEvent(event);
            }
        }
        if (module instanceof SHP) {
            if (event.equals("Away Mode On")) {
                this.shh.triggerEvent("Away Mode On");
            }
            if (event.equals("Away Mode Off")) {
                this.shh.triggerEvent("Away Mode Off");
            }
        }
    }
}
