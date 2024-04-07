package com.raygak.server.smarthome;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;

/*
Timestamp: 2024-03-11 08:00:00
Device ID: Thermostat-001
Event Type: Temperature Change
Event Description: Room temperature increased
Details:
- Previous Temperature: 70°F
- New Temperature: 72°F
- Heating Mode: Active
- Reason: Morning schedule activated
- User: Automated schedule

*/
public class LogFileMaker {
    public void makeTextFile() {
        try {
            File logFile = new File("logFile.txt");
            logFile.createNewFile();
            FileWriter writer = new FileWriter("logFile.txt", true);
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            writer.write("Timestamp: " + today.getYear() + "-" + (today.getMonthValue() < 10 ? "0" + today.getMonthValue() : today.getMonthValue()) + "-" + (today.getDayOfMonth() < 10 ? "0" + today.getDayOfMonth() : today.getDayOfMonth()) + " " +
                    (now.getHour() < 10 ? "0" + now.getHour() : now.getHour())  + ":" + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute()) + ":" + (now.getSecond() < 10 ? "0" + now.getSecond() : now.getSecond())+ "\n");
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void temperatureUpdateLog(String roomID, double oldTemp, double newTemp, boolean isSHHOn, String reason, String user) {
        try {
            File logFile = new File("logFile.txt");
            logFile.createNewFile();
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            FileWriter writer = new FileWriter("logFile.txt", true);
            writer.write("Timestamp: " + today.getYear() + "-" + (today.getMonthValue() < 10 ? "0" + today.getMonthValue() : today.getMonthValue()) + "-" + (today.getDayOfMonth() < 10 ? "0" + today.getDayOfMonth() : today.getDayOfMonth()) + " " +
                    (now.getHour() < 10 ? "0" + now.getHour() : now.getHour())  + ":" + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute()) + ":" + (now.getSecond() < 10 ? "0" + now.getSecond() : now.getSecond()) + '\n');
            writer.write("Device Name: Room " + roomID + " Thermostat" + '\n');
            writer.write("Event Type: Temperature Change " + '\n');
            writer.write("Event Description: Room temperature " + (oldTemp < newTemp ? "decreased." : "increased.") + '\n');
            writer.write("Details: " + '\n');
            writer.write("- Previous Temperature: " + oldTemp + '\n');
            writer.write("- New Temperature: " + newTemp + '\n');
            writer.write("- Heating Mode: " + (isSHHOn ? "Active" : "Inactive") + '\n');
            writer.write("- Reason: " + reason + '\n');
            if (reason.equals("A new person has entered a room when it was originally uninhabited.")) {
                writer.write("- User (person who entered room): " + user + '\n');
            }
            if (reason.equals("A person has exited a room, leaving it uninhabited.")) {
                writer.write("- User (person who left room): " + user + '\n');
            }
            if (reason.equals("A person has manually changed the temperature of a room from within it.")) {
                writer.write("- User (person who changed temperature locally): " + user + '\n');
            }
            if (reason.equals("A person has manually changed the temperature of a room in remote fashion.")) {
                writer.write("- User (person who changed temperature remotely): " + user + '\n');
            }
            writer.write('\n');
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void houseTemperatureChangeLog(House houseID, double oldTemp, double newTemp, boolean isSHHOn, String reason, String user) {
        try {
            File logFile = new File("logFile.txt");
            logFile.createNewFile();
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            FileWriter writer = new FileWriter("logFile.txt", true);
            writer.write("Timestamp: " + today.getYear() + "-" + (today.getMonthValue() < 10 ? "0" + today.getMonthValue() : today.getMonthValue()) + "-" + (today.getDayOfMonth() < 10 ? "0" + today.getDayOfMonth() : today.getDayOfMonth()) + " " +
                    (now.getHour() < 10 ? "0" + now.getHour() : now.getHour())  + ":" + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute()) + ":" + (now.getSecond() < 10 ? "0" + now.getSecond() : now.getSecond()) + '\n');
            writer.write("Device Name: House " + houseID + " Home-Wide Thermostat" + '\n');
            writer.write("Event Type: Temperature Change " + '\n');
            writer.write("Event Description: House temperature " + (oldTemp < newTemp ? "decreased." : "increased.") + '\n');
            writer.write("Details: " + '\n');
            writer.write("- Previous Temperature: " + oldTemp + '\n');
            writer.write("- New Temperature: " + newTemp + '\n');
            writer.write("- Heating Mode: " + (isSHHOn ? "Active" : "Inactive") + '\n');
            writer.write("- Reason: " + reason + '\n');
            if (reason.equals("A new person has entered a room when it was originally uninhabited.")) {
                writer.write("- User (person who entered room): " + user + '\n');
            }
            if (reason.equals("A person has exited a room, leaving it uninhabited.")) {
                writer.write("- User (person who left room): " + user + '\n');
            }
            if (reason.equals("A person has manually changed the temperature of a room from within it.")) {
                writer.write("- User (person who changed temperature locally): " + user + '\n');
            }
            if (reason.equals("A person has manually changed the temperature of a room in remote fashion.")) {
                writer.write("- User (person who changed temperature remotely): " + user + '\n');
            }
            writer.write('\n');
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void protocolLog(boolean isSHHOn, String eventCause) {
        try {
            File logFile = new File("logFile.txt");
            logFile.createNewFile();
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            FileWriter writer = new FileWriter("logFile.txt", true);
            writer.write("Timestamp: " + today.getYear() + "-" + (today.getMonthValue() < 10 ? "0" + today.getMonthValue() : today.getMonthValue()) + "-" + (today.getDayOfMonth() < 10 ? "0" + today.getDayOfMonth() : today.getDayOfMonth()) + " " +
                    (now.getHour() < 10 ? "0" + now.getHour() : now.getHour())  + ":" + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute()) + ":" + (now.getSecond() < 10 ? "0" + now.getSecond() : now.getSecond()) + '\n');
            writer.write("Device Name: House-Wide Thermostat" + '\n');
            writer.write("Event Type: Protocol Change " + '\n');
            if (eventCause.equals("The house has become uninhabited during the winter season.")) {
                writer.write("Event Description: All relevant zone temperature settings within the house have been set to 17 degrees Celsius." + '\n');
            }
            if (eventCause.equals("The house has become inhabited once again during the winter season.")) {
                writer.write("Event Description: All relevant zone temperature settings within the house have been set back to their original temperature values." + '\n');
            }
            if (eventCause.equals("The outside temperature of the house has become lower than the inside temperature and at least one person is home during the summer season.")) {
                writer.write("Event Description: All rooms in the house have had their HVAC turned off and their windows opened." + '\n');
            }
            if (eventCause.equals("The outside temperature of the house has become equal to or greater than the inside temperature, or nobody is home, during the summer season.")) {
                writer.write("Event Description: All rooms in the house have had their HVAC turned on and their windows closed." + '\n');
            }
            writer.write("Details: " + '\n');
            writer.write("- Heating Mode: " + (isSHHOn ? "Active" : "Inactive") + '\n');
            writer.write("- Cause of Event: " + eventCause + '\n');
            writer.write('\n');
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
