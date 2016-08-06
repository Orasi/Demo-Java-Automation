package com.orasi.utils.debugging;

public enum Colors {
    NONE("none"),
    RED ("red"),
    YELLOW ("yellow"),
    PURPLE ("purple"),
    BLUE ("blue"),
    GREEN ("green"),
    BLACK ("black");
    
    @SuppressWarnings("unused")
	private String color;
     Colors(String color){
	this.color = color;
    }
     
 }
