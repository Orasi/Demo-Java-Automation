package com.orasi.utils;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FrameHandler {
	public static void findAndSwitchToFrame(TestEnvironment te, String frame){
		te.getDriver().switchTo().defaultContent();
		WebDriverWait wait = new WebDriverWait(te.getDriver(), te.getDefaultTestTimeout());
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));		
	}
}
