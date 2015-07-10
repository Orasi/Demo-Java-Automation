package com.orasi.bluesource.api;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orasi.api.restServices.blueSource.BlueSource;
import com.orasi.api.restServices.blueSource.employees.EmployeeDetails;

public class TestEmployee {
    BlueSource blueSource = new BlueSource("Company.admin");
    private EmployeeDetails employee = new EmployeeDetails();
    
    public TestEmployee(){
	employee.generateData();
    }
    
    @Test
    public void testGetNumberOfEmployees(){
	Assert.assertTrue(blueSource.employees().getNumberOfEmployees()> 0);
    }
    
    @Test(dependsOnMethods="testGetNumberOfEmployees")
    public void testCreateEmployee(){
	blueSource.employees().createEmployee(employee);
	Assert.assertTrue(employee.getId() > 0);
    }
    
    @Test(dependsOnMethods="testCreateEmployee")
    public void testEmployeeDetails(){
	Assert.assertTrue(blueSource.employees().getEmployeeDetails(employee.getId()).getUsername().equals(employee.getUsername()));
    }
    
    @Test(dependsOnMethods="testEmployeeDetails")
    public void testUpdateEmployeeDetails(){
 	employee.setFirst_name("blah");
 	blueSource.employees().updateEmployeeDetails(employee);
 	Assert.assertTrue(blueSource.employees().getEmployeeDetails(employee.getId()).getFirst_name().equals(employee.getFirst_name()));
     }
    
    @Test 
    public void testAllEmployees(){
	List<EmployeeDetails> allEmployees = blueSource.employees().getAllEmployees();
	Assert.assertFalse(allEmployees.isEmpty());
    }
    
}
