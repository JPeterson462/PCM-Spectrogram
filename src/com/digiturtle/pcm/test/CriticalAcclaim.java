package com.digiturtle.pcm.test;

import com.digiturtle.pcm.SpectrogramGraph;

import javafx.application.Application;

public class CriticalAcclaim {

	public static void main(String[] args) {
		System.setProperty("javafx.animation.framerate", "10");
		System.setProperty("quantum.multithreaded", "false");
		Application.launch(SpectrogramGraph.class, args);
		System.exit(0);
	}
	
}
