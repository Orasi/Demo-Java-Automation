package com.orasi.bluesource.api;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;

import com.orasi.api.restServices.blueSource.BlueSource;
import com.orasi.api.restServices.blueSource.employees.EmployeeDetails;
import com.orasi.utils.TestReporter;

public class TestEmployee {
    BlueSource blueSource = new BlueSource("Company.admin");
    private EmployeeDetails employee = new EmployeeDetails();
    
    public TestEmployee(){
	employee.generateData();
    }
    
    @Features("REST Services Testing")
    @Test
    public void testGetNumberOfEmployees(){
    	TestReporter.logStep("Get total number of Employees");
    	Assert.assertTrue(blueSource.employees().getNumberOfEmployees()> 0, "Verify that the total number of employees is greater than 0");
    }
    
    @Features("REST Services Testing")
    @Test(dependsOnMethods="testGetNumberOfEmployees")
    public void testCreateEmployee(){
    	TestReporter.logStep("Create a new employee");
		blueSource.employees().createEmployee(employee);
		Assert.assertTrue(employee.getId() > 0, "Verify employee was successfully created");
    }
    
    @Features("REST Services Testing")
    @Test(dependsOnMethods="testCreateEmployee")
    public void testEmployeeDetails(){
    	TestReporter.logStep("Get the newly created employees username");
    	Assert.assertTrue(blueSource.employees().getEmployeeDetails(employee.getId()).getUsername().equals(employee.getUsername()), 
    			"Verify that the actual username created by API matches the expected username");
    }
    
    @Features("REST Services Testing")
    @Test(dependsOnMethods="testEmployeeDetails")
    public void testUpdateEmployeeDetails(){
    	TestReporter.logStep("Update the employees name");
    	employee.setFirst_name("blah");
    	blueSource.employees().updateEmployeeDetails(employee);
    	Assert.assertTrue(blueSource.employees().getEmployeeDetails(employee.getId()).getFirst_name().equals(employee.getFirst_name()),
    			"Verify the actual updated name matches the expected");
     }
    
    @Features("REST Services Testing")
    @Test 
    public void testAllEmployees(){
    	TestReporter.logStep("Get a list of all employees");
    	List<EmployeeDetails> allEmployees = blueSource.employees().getAllEmployees();
    	Assert.assertFalse(allEmployees.isEmpty(), "Verify the list is not empty");
    }
    
}
