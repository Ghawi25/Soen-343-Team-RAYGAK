package com.raygak.server.smarthome.heating;

import java.time.LocalTime;

public class TemperatureSetting {
	//A setting ID is present for ease of access.
	private String settingID;
	private double desiredTemperature;
	private LocalTime start;
	private LocalTime end;
	
	public TemperatureSetting(String idInput, double tempInput, int hours1, int minutes1, int hours2, int minutes2) {
		//hours1, minutes1 -> start time.
		//hours2, minutes2 -> end time.
		//hours2 < hours1 -> end time precedes start time in terms of hours.
		//(hours2 == hours1 && minutes2 <= minutes1) -> end time precedes start time in terms of minutes.
		if ((hours2 < hours1) || (hours2 == hours1 && minutes2 <= minutes1)) {
			throw new IllegalArgumentException("Error: End time cannot precede or be the same as start time.");
		}
		this.settingID = idInput;
		this.desiredTemperature = tempInput;
		this.start = LocalTime.of(hours1, minutes1, 0);
		this.end = LocalTime.of(hours2, minutes2, 0);
	}
	
	public String getSettingID() {
		return this.settingID;
	}
	
	public double getDesiredTemperature() {
		return this.desiredTemperature;
	}
	
	public LocalTime getStart() {
		return this.start;
	}
	
	public LocalTime getEnd() {
		return this.end;
	}
	
	public void setDesiredTemperature(double newTemperature) {
		this.desiredTemperature = newTemperature;
	}
	
	public void setStart(int newStartHours, int newStartMinutes) {
		if ((newStartHours > this.end.getHour()) || (newStartHours == this.end.getHour() && newStartMinutes >= this.end.getMinute())) {
			throw new IllegalArgumentException("Error: Start time cannot exceed or be the same as end time.");
		}
		this.start = LocalTime.of(newStartHours, newStartMinutes, 0);
	}
	
	public void setEnd(int newEndHours, int newEndMinutes) {
		if ((newEndHours < this.start.getHour()) || (newEndHours == this.start.getHour() && newEndMinutes <= this.start.getMinute())) {
			throw new IllegalArgumentException("Error: End time cannot precede or be the same as start time.");
		}
		this.end = LocalTime.of(newEndHours, newEndMinutes, 0);
	}
}
