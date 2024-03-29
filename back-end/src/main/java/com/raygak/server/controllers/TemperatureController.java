package com.raygak.server.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class TemperatureController {

    /**
     * Handles GET requests to fetch data using 'date' and 'time' query parameters.
     *
     * @param date The formatted date as a query parameter.
     * @param time The formatted time as a query parameter.
     * @return A response indicating the received query parameters.
     */
    @GetMapping("/temperature")
    public ResponseEntity<?> fetchDataUsingQueryParams(
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "time", required = false) String time) {

        String csvFilePath = "C:\\Users\\adamo\\Soen-343-Team-RAYGAK\\back-end\\src\\main\\resources\\temperature_data.csv";
        
        // Load the temperature data
        int temperature = loadTemperatureData(csvFilePath, date, time);
        
        Map<String, Object> response = new HashMap<>();
        response.put("temperature", temperature);
        return ResponseEntity.ok(response);
    }

    public int loadTemperatureData(String csvFilePath, String thisDate, String hour){
        Resource resource = new ClassPathResource("temperature_data.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
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
                    if (thisDate.equals(date) && hour.substring(0, 2).equals(time.substring(0, 2))) {
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
