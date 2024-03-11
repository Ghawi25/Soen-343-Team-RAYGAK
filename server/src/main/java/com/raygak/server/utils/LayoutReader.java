package com.raygak.server.utils;

// Imports
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import java.io.FileReader;

/**
 * LayoutReader
 */
public class LayoutReader {

    public JSONObject readFile() {
        JSONParser parser = new JSONParser();
        JSONObject parsedLayout = new JSONObject();
        try {
            Object parsedFile = parser.parse(new FileReader("src/main/resources/jsonTest.json"));
            parsedLayout = (JSONObject) parsedFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedLayout;
    }
}