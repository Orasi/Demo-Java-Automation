package com.orasi.utils;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

public class WindowHandler {
	private String currentWindow;	
	

	public void setCurrentWindow(WebDriver driver){
		currentWindow = driver.getWindowHandle(); // get the current window handle	
	}
	
		
	public void waitUntilWindowExists(WebDriver driver, String window){
		int time = 0 ;
		boolean found = false;
		while(!found)
			for (String winHandle : driver.getWindowHandles()) {
				try{
						driver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's your newly opened window)
						if (driver.getTitle().toUpperCase().contains(window.toUpperCase())){
							found = true;
							break;
						}
			       }catch(NoSuchWindowException | NullPointerException e){
			    	   
			       }
			}		
			time++;
			
			if (time == TestEnvironment.getDefaultTestTimeout()) found = true;
		
	}
	

	
	public WebDriver swapToParentWindow(WebDriver driver){
		driver.switchTo().window(currentWindow); // switch back to the original window
		   return driver;
	}
	
	
	
	public static void ieKiller() throws Exception
	{
	  final String KILL = "taskkill /IM ";
	  String processName = "IEDriverServer.exe"; //IE process
	  Runtime.getRuntime().exec(KILL + processName); 
	  Thread.sleep(3000); //Allow OS to kill the process
	} 	
	
	/**
	 * @summary Swaps to a window with a specified title after waiting a specified number of
	 * 			seconds for the window to display.  Pass in a number of seconds (1,2 etc), and
	 * 			the title of the window you wish to switch to.  Do not need to pass in the whole title,
	 * 			can be a part of it such as "Lilo".  Can also pass in a null value ("") if the window
	 * 			does not have a title.  Returns a true if the window was found & switched to and false if not
	 * @version Created 10/29/2014
	 * @author 	Jessica Marshall
	 * @param 	driver, title, numOfWaitSeconds
	 * @throws 	NA
	 * @return 	true/false
	 */
	public static boolean swapToWindowWithTimeout(WebDriver driver, String title, int numOfWaitSeconds){
		int count = 0;
		//wait for the window count to be greater than 1
		while (driver.getWindowHandles().size()==1){
			Sleeper.sleep(1000);
			
			if (count > numOfWaitSeconds)
				return false;
			count++;
		}
		
		for (String winHandle : driver.getWindowHandles()) {
			
			driver.switchTo().window(winHandle);
			//code to handle if the title of the window you expect to switch to is null
			if (title.equals("")){
				if (driver.getTitle().toUpperCase().equals(""))
					return true;
			}
			//code to handle a non null tile of the winodw you wish to switch to	
			if (driver.getTitle().toUpperCase().contains(title.toUpperCase())){
				return true;
			}
		}
		
		return false;
	}
}
