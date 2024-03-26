package com.raygak.server.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public class TemperatureController {

    /**
     * Handles GET requests to fetch data using 'date' and 'time' query parameters.
     *
     * @param date The formatted date as a query parameter.
     * @param time The formatted time as a query parameter.
     * @return A response indicating the received query parameters.
     */
    @GetMapping("/temperature")
    public String fetchDataUsingQueryParams(
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "time", required = false) String time) {
        // Implement your business logic here.
        // For demonstration, returning the received query parameters.
        return String.format("Received date: %s and time: %s", date, time);
    }

    public int loadTemperatureData(String jsonFilePath, String csvFilePath){
        Map<String, String> dateTimeMap = null;
        
        // Read the JSON file
        try {
            String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            Map<String, String> map = new HashMap<>();
            content = content.trim().replaceAll("[{}\"]", ""); // Remove JSON braces and quotes
            String[] keyValuePairs = content.split(",");
            for (String pair : keyValuePairs) {
                // Separate the data without messing up the Hour format
                int colonIndex = pair.indexOf(":");
                String key = pair.substring(0, colonIndex).trim();
                String value = pair.substring(colonIndex + 1).trim();
                map.put(key, value);
            }
            dateTimeMap = map;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        
        // Assign date and time values
        String thisDate = dateTimeMap.get("date");
        String hour = dateTimeMap.get("hour");
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip CSV header
                if (line.startsWith("Date")) {
                    continue;
                }
                // separate the information
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String date = parts[0].trim();
                    String time = parts[1].trim();
                    // find and return the correct temperature
                    if (thisDate.equals(date) && hour.equals(time)) {
                        return Integer.parseInt(parts[2].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // No temperature data found for the current hour
        return 0; 
    }
}
