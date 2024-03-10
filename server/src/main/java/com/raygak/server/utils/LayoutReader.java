package com.raygak.server.utils;

// Imports
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import java.io.FileReader;

/**
 * LayoutReader
 */
public class LayoutReader {

    private JSONObject parsedLayout;

    public LayoutReader() {
        parsedLayout = null;
    }

    public JSONObject getParsedLayout() {
        return parsedLayout;
    }

    public void readFile() {
        JSONParser parser = new JSONParser();
        try {
            Object parsedFile = parser.parse(new FileReader("src/main/resources/jsonTest.json"));
            parsedLayout = (JSONObject) parsedFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}