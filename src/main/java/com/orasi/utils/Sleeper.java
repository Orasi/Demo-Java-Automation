package com.orasi.utils;


public class Sleeper {
	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			
		}
	}
}
