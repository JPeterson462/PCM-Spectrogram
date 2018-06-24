package com.digiturtle.pcm.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.digiturtle.pcm.SpectrogramGraph;

import javafx.application.Application;

public class CriticalAcclaim {

	public static void main(String[] args) throws FileNotFoundException {
		System.setErr(new PrintStream(new FileOutputStream("err.txt")));
		Application.launch(SpectrogramGraph.class, args);
		System.exit(0);
	}
	
}
