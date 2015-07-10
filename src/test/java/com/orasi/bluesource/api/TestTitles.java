package com.orasi.bluesource.api;

import java.util.Iterator;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orasi.api.restServices.blueSource.BlueSource;
import com.orasi.api.restServices.blueSource.employees.EmployeeDetails;
import com.orasi.api.restServices.blueSource.titles.Title;

public class TestTitles {
    BlueSource blueSource = new BlueSource("Company.admin");
    private Title title = new Title();
    
    public TestTitles(){
	title.generateData();
    }
    
    @Test 
    public void testAllTitles(){
	List<Title> allTitles= blueSource.titles().getAllTitles();
	Assert.assertFalse(allTitles.isEmpty());
    }
    
   @Test
    public void testGetNumberOfTitles(){
       Assert.assertTrue(blueSource.titles().getNumberOfTitles()> 0);
    }
   
    @Test(dependsOnMethods="testGetNumberOfTitles")
    public void testCreateTitle(){
	blueSource.titles().createTitle(title);
	Assert.assertTrue(title.getId() > 0);
    }
    
    
    @Test(dependsOnMethods="testCreateTitle")
    public void testGetTitle(){
	Assert.assertTrue(blueSource.titles().getTitle(title.getId()).getName().equals(title.getName()));
    }
    
    @Test(dependsOnMethods="testGetTitle")
    public void testUpdateTitle(){
	title.setName("blah");
 	blueSource.titles().updateTitle(title);
 	Assert.assertTrue(blueSource.titles().getTitle(title.getId()).getName().equals(title.getName()));
     }
    
    @Test(dependsOnMethods="testUpdateTitle")
    public void testDeleteTitle(){
 	blueSource.titles().deleteTitle(title);
 	List<Title>  titles = blueSource.titles().getAllTitles();
	Title tempTitle = null;
	Iterator<Title> titleIterator = titles.iterator();
	while (titleIterator.hasNext()) {
	    tempTitle = titleIterator.next();
	    if(tempTitle.getName().equals(title.getName())) Assert.fail("Title name [ " + title.getName() + " ] was not deleted successfully.");
	 }
     }   
}
