package com.raygak.server.timers;

import com.raygak.server.smarthome.House;
import com.raygak.server.smarthome.User;
import com.raygak.server.modules.SHP;
import com.raygak.server.smarthome.security.SHPEventType;
import lombok.Getter;

@Getter
public class IntruderAlertTimer extends Thread {
    private long startTime;
    private int numSeconds = 0;
    private boolean running = false;
    private String targetUser;
    private House associatedHouse;
    private SHP shp;

    public IntruderAlertTimer(SHP shp)
    {
        this.shp = shp;
    }

    public IntruderAlertTimer(String targetUser, House associatedHouse) {
        this.targetUser = targetUser;
        this.associatedHouse = associatedHouse;
        this.shp = associatedHouse.getShp();
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        int timeToAlert = this.shp.getTimeForAlert();
        running = true;
        while (running) {
            if (System.currentTimeMillis() == startTime + (timeToAlert * 1000)) {
                for (User u : this.associatedHouse.getInhabitants()) {
                    if (u.getUsername().equals(this.targetUser)) {
                        this.associatedHouse.getAssociatedSimulator().getLogger().authoritiesAlertedLog(u.getUsername());
                        this.shp.notify(SHPEventType.AUTHORITIES_ALERTED, this.targetUser);
                        this.associatedHouse.disableAwayMode();
                        stopTimer();
                    }
                }
            }
        }
    }

    public void stopTimer() {
        running = false;
    }
}
