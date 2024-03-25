package com.raygak.server.smarthome;

import com.raygak.server.commands.Command;
import lombok.Getter;

public class HouseControl {
    @Getter
    private Command command;

    public void setCommand(Command newCommand) {
        this.command = newCommand;
    }

    public void execute() {
        command.execute();
    }
}
