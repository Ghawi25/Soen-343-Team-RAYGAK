package com.raygak.server.smarthome;

import lombok.Getter;

@Getter
public class Window {
	private String windowID;
	private boolean isOpen;
	private boolean isObstructed;
	
	public Window(String idInput, boolean isOpen, boolean isObstructed) {
		this.windowID = idInput;
		this.isOpen = isOpen;
		this.isObstructed = isObstructed;
	}
	
	public void open() {
		if (this.isObstructed) {
			System.out.println("Error: Cannot open window " + this.windowID + " as it is obstructed.");
			return;
		}
		if (this.isOpen) {
			System.out.println("Error: Cannot open window " + this.windowID + " as it is already open.");
			return;
		}
		this.isOpen = true;
	}
	
	public void close() {
		if (this.isObstructed) {
			System.out.println("Error: Cannot close window " + this.windowID + " as it is obstructed.");
			return;
		}
		if (!(this.isOpen)) {
			System.out.println("Error: Cannot close window " + this.windowID + " as it is already closed.");
			return;
		}
		this.isOpen = false;
	}
	
	// Use 15 - Obstruct window
	public void obstruct() {
		if (this.isObstructed) {
			System.out.println("Error: Cannot obstruct window " + this.windowID + " as it is already obstructed.");
			return;
		}
		this.isObstructed = true;
	}
	
	public void unobstruct() {
		if (!(this.isObstructed)) {
			System.out.println("Error: Cannot un-obstruct window " + this.windowID + " as it is already un-obstructed.");
			return;
		}
		this.isObstructed = false;
	}
}
