package com.raygak.server.smarthome.heating;

public class Person {
	private String personID;
	private String name;
	private int age;
	
	public Person(String idInput, String nameInput, int ageInput) {
		this.personID = idInput;
		this.name = nameInput;
		this.age = ageInput;
	}
	
	public String getPersonID() {
		return this.personID;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public void enterRoom(Room room) {
		room.addInhabitant(this);
	}
	
	public void leaveRoom(Room room) {
		room.removeInhabitant(this.personID);
	}
}
