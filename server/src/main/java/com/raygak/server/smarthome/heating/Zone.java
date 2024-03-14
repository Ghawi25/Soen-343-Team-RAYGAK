package com.raygak.server.smarthome.heating;

import com.raygak.server.smarthome.Room;

import java.util.ArrayList;

public class Zone {
	private String zoneID;
	private ZoneType type;
	private ArrayList<TemperatureSetting> settingList = new ArrayList<TemperatureSetting>();
	private ArrayList<Room> roomList;
	
	public Zone(String idInput, ZoneType typeInput) {
		this.zoneID = idInput;
		this.type = typeInput;
	}
	
	public Zone(String idInput, ZoneType typeInput, ArrayList<TemperatureSetting> settingListInput, ArrayList<Room> roomListInput) {
		this.zoneID = idInput;
		this.type = typeInput;
		this.settingList = settingListInput;
		this.roomList = roomListInput;
		for (Room r : this.roomList) {
			System.out.println("ADDING ROOM " + r.getRoomID() + " TO ZONE " + this.zoneID);
			r.setZone(this);
		}
	}
	
	public String getZoneID() {
		return this.zoneID;
	}
	
	public ZoneType getType() {
		return this.type;
	}
	
	public ArrayList<TemperatureSetting> getSettingList() {
		return this.settingList;
	}
	
	public double checkZoneThermostat() {
		double totalTemperature = 0;
		for (Room r : this.roomList) {
			System.out.println("THERMOSTAT CHECK (Room " + r.getRoomID() + "):");
			totalTemperature += r.getCurrentTemperature();
		}
		return (double)(totalTemperature / this.roomList.size());
	}
	
	public ArrayList<Room> getRoomList() {
		return this.roomList;
	}
	
	public void setType(ZoneType newType) {
		this.type = newType;
	}
	
	public void addSetting(TemperatureSetting newSetting) {
		if (this.settingList.size() == 3) {
			System.out.println("Error (Zone " + this.zoneID + "): The temperature settings list cannot contain more than 3 settings.");
			return;
		}
		this.settingList.add(newSetting);
	}
	
	public void removeSetting(String settingID) {
		if (this.settingList.size() == 1) {
			System.out.println("Error (Zone " + this.zoneID + "): The temperature settings list must contain at least 1 setting.");
			return;
		}
		for (int i = 0;i < this.settingList.size();i++) {
			if (this.settingList.get(i).getSettingID().equals(settingID)) {
				this.settingList.remove(i);
				return;
			}
		}
		throw new IllegalArgumentException("Error: The thermostat with the provided ID does not exist.");
	}
	
	public void addRoom(Room newRoom) {
		newRoom.setZone(this);
		this.roomList.add(newRoom);
	}
	
	public void removeRoom(String roomID) {
		for (int i = 0;i < this.roomList.size();i++) {
			if (this.roomList.get(i).getRoomID().equals(roomID)) {
				this.roomList.remove(i);
				return;
			}
		}
		throw new IllegalArgumentException("Error: The room with the provided ID does not exist.");
	}
	
	public void setSettingList(ArrayList<TemperatureSetting> newSettingList) {
		this.settingList = newSettingList;
	}
	
	public void setRoomList(ArrayList<Room> newRoomList) {
		this.roomList = newRoomList;
		for (Room r : this.roomList) {
			r.setZone(this);
		}
	}
	
	//For when the temperature setting of the zone can be modified using individual parameters only
	public void setSetting(String settingID, double newTemp, int newStartHours, int newStartMinutes, int newEndHours, int newEndMinutes) {
		for (int i = 0;i < this.settingList.size();i++) {
			if (this.settingList.get(i).getSettingID().equals(settingID)) {
				this.settingList.set(i, new TemperatureSetting(settingID, newTemp, newStartHours, newStartMinutes, newEndHours, newEndMinutes));
			}
		}
	}
}