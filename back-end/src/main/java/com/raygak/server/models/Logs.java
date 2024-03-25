package com.raygak.server.models;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public class Logs {
    @Id
    private ObjectId id;
    @Getter
    private ArrayList<String> logMsg = new ArrayList<>();

    public Logs(String msg) {
        this.logMsg.add(msg);
    }
}