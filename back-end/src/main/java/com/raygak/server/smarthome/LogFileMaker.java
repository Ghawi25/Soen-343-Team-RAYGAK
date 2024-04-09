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
            String cause = "";
            if (reason.equals("A new person has entered the room when it was originally uninhabited.")) {
                writer.write("- User (person who entered room): " + user + '\n');
                cause = "SHH Module.";
            }
            if (reason.equals("A person has exited the room, leaving it uninhabited.")) {
                writer.write("- User (person who left room): " + user + '\n');
                cause = "SHH Module.";
            }
            if (reason.equals("A person has manually changed the temperature of a room from within it.")) {
                writer.write("- User (person who changed temperature locally): " + user + '\n');
                cause = "User with email address " + user;
            }
            if (reason.equals("A person has manually changed the temperature of a room in remote fashion.")) {
                writer.write("- User (person who changed temperature remotely): " + user + '\n');
                cause = "User with email address " + user;
            }
            if (reason.equals("The outside temperature has been changed by a simulator user.")) {
                writer.write("- User (person who changed temperature remotely): current simulator user" + '\n');
                cause = "Current Simulator User";
            }
            writer.write("- Caused By: " + cause + '\n');
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
            String cause = "";
            if (reason.equals("A new person has entered a room when it was originally uninhabited.")) {
                writer.write("- User (person who entered room): " + user + '\n');
                cause = "SHH Module.";
            }
            if (reason.equals("A person has exited a room, leaving it uninhabited.")) {
                writer.write("- User (person who left room): " + user + '\n');
                cause = "SHH Module.";
            }
            if (reason.equals("A person has manually changed the temperature of a room from within it.")) {
                writer.write("- User (person who changed temperature locally): " + user + '\n');
                cause = "User with email address " + user;
            }
            if (reason.equals("A person has manually changed the temperature of a room in remote fashion.")) {
                writer.write("- User (person who changed temperature remotely): " + user + '\n');
                cause = "User with email address " + user;
            }
            writer.write("- Caused By: " + cause + '\n');
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
            writer.write("Device Name: SHH Module." + '\n');
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
            writer.write("- Caused By: SHH Module." + '\n');
            writer.write('\n');
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void openCloseLog(String objectType, String objectID, String statusAfter, String eventCause) {
        try {
            File logFile = new File("logFile.txt");
            logFile.createNewFile();
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            FileWriter writer = new FileWriter("logFile.txt", true);
            writer.write("Timestamp: " + today.getYear() + "-" + (today.getMonthValue() < 10 ? "0" + today.getMonthValue() : today.getMonthValue()) + "-" + (today.getDayOfMonth() < 10 ? "0" + today.getDayOfMonth() : today.getDayOfMonth()) + " " +
                    (now.getHour() < 10 ? "0" + now.getHour() : now.getHour())  + ":" + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute()) + ":" + (now.getSecond() < 10 ? "0" + now.getSecond() : now.getSecond()) + '\n');
            writer.write("Device Name: SHH Module" + '\n');
            writer.write("Event Description: " + objectType + " " + objectID + " " + (statusAfter.equals("Closed") ? "Closed" : "Open") + '\n');
            writer.write("Details: " + '\n');
            writer.write("- Cause of Event: " + eventCause + '\n');
            writer.write('\n');
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void unableToOpenOrCloseLog(String objectType, String objectID, String status, String reason) {
        try {
            File logFile = new File("logFile.txt");
            logFile.createNewFile();
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            FileWriter writer = new FileWriter("logFile.txt", true);
            writer.write("Timestamp: " + today.getYear() + "-" + (today.getMonthValue() < 10 ? "0" + today.getMonthValue() : today.getMonthValue()) + "-" + (today.getDayOfMonth() < 10 ? "0" + today.getDayOfMonth() : today.getDayOfMonth()) + " " +
                    (now.getHour() < 10 ? "0" + now.getHour() : now.getHour())  + ":" + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute()) + ":" + (now.getSecond() < 10 ? "0" + now.getSecond() : now.getSecond()) + '\n');
            writer.write("Device Name: SHH Module" + '\n');
            writer.write("Event Description: " + objectType + " " + objectID + " is unable to transition from being " + status.toLowerCase() + '\n');
            writer.write("Details: " + '\n');
            if (reason.equals("Obstruction")) {
                writer.write("- Cause of Event: " + objectType + " '" + objectID + "' is obstructed and thus cannot be opened/closed." + '\n');
            }
            if (reason.equals("Away Mode")) {
                writer.write("- Cause of Event: The house is in away mode, thus disallowing " + objectType + " '" + objectID + "' from being opened/closed." + '\n');
            }
            writer.write("- Notified Person: System User." + '\n');
            writer.write('\n');
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void roomTemperature135DegreesLog(String eventCause) {
        try {
            File logFile = new File("logFile.txt");
            logFile.createNewFile();
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            FileWriter writer = new FileWriter("logFile.txt", true);
            writer.write("Timestamp: " + today.getYear() + "-" + (today.getMonthValue() < 10 ? "0" + today.getMonthValue() : today.getMonthValue()) + "-" + (today.getDayOfMonth() < 10 ? "0" + today.getDayOfMonth() : today.getDayOfMonth()) + " " +
                    (now.getHour() < 10 ? "0" + now.getHour() : now.getHour())  + ":" + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute()) + ":" + (now.getSecond() < 10 ? "0" + now.getSecond() : now.getSecond()) + '\n');
            writer.write("Device Name: SHP Module" + '\n');
            writer.write("Event Type: Turning Off 'Away Mode' " + '\n');
            writer.write("Event Description: 'Away Mode' is turning off due to the temperature of a room in the house reaching 135 degrees." + '\n');
            writer.write("Details: " + '\n');
            writer.write("- Cause of Event: " + eventCause + '\n');
            writer.write("- Notified Persons: Homeowners." + '\n');
            writer.write('\n');
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void motionDetectedLog(String roomIdentifier, String userIdentifier) {
        try {
            File logFile = new File("logFile.txt");
            logFile.createNewFile();
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            FileWriter writer = new FileWriter("logFile.txt", true);
            writer.write("Timestamp: " + today.getYear() + "-" + (today.getMonthValue() < 10 ? "0" + today.getMonthValue() : today.getMonthValue()) + "-" + (today.getDayOfMonth() < 10 ? "0" + today.getDayOfMonth() : today.getDayOfMonth()) + " " +
                    (now.getHour() < 10 ? "0" + now.getHour() : now.getHour())  + ":" + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute()) + ":" + (now.getSecond() < 10 ? "0" + now.getSecond() : now.getSecond()) + '\n');
            writer.write("Device Name: SHP Module" + '\n');
            writer.write("Event Type: Motion Detected during 'Away Mode.'" + '\n');
            writer.write("Event Description: Motion within a room has been detected while the house is in 'Away Mode'." + '\n');
            writer.write("Details: " + '\n');
            writer.write("- Cause of Event: Motion was detected in room " + roomIdentifier + " by someone with email " + userIdentifier + '\n');
            writer.write("- Notified Persons: Homeowners." + '\n');
            writer.write('\n');
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void authoritiesAlertedLog(String userIdentifier) {
        try {
            File logFile = new File("logFile.txt");
            logFile.createNewFile();
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            FileWriter writer = new FileWriter("logFile.txt", true);
            writer.write("Timestamp: " + today.getYear() + "-" + (today.getMonthValue() < 10 ? "0" + today.getMonthValue() : today.getMonthValue()) + "-" + (today.getDayOfMonth() < 10 ? "0" + today.getDayOfMonth() : today.getDayOfMonth()) + " " +
                    (now.getHour() < 10 ? "0" + now.getHour() : now.getHour())  + ":" + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute()) + ":" + (now.getSecond() < 10 ? "0" + now.getSecond() : now.getSecond()) + '\n');
            writer.write("Device Name: SHP Module" + '\n');
            writer.write("Event Type: Authorities Alerted of Intruder." + '\n');
            writer.write("Event Description: The authorities have been alerted of an intruder being present in the home for the user-specified number of seconds during 'Away Mode'." + '\n');
            writer.write("Details: " + '\n');
            writer.write("- Cause of Event: Someone with email " + userIdentifier + " is still present in the house." + '\n');
            writer.write("- Notified Persons: Authorities, Homeowners." + '\n');
            writer.write('\n');
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
