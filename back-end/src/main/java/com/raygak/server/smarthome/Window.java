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
		this.isOpen = true;
	}
	
	public void close() {
		this.isOpen = false;
	}
	
	public void obstruct() {
		this.isObstructed = true;
	}
	
	public void unobstruct() {
		this.isObstructed = false;
	}
}
