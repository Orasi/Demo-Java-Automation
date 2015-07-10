package com.orasi.apps.bluesource.projectsPage;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.runners.model.EachTestNotifier;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Reporter;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.apps.bluesource.employeesPage.Employee;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.ElementImpl;
import com.orasi.core.interfaces.impl.ListboxImpl;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.TestEnvironment;

public class ManageProjectModal {
    private TestEnvironment te;
    
    @FindBy(id = "modal_label_1")
    private Label lblAddProjectPopup;
    
    @FindBy(id = "project_name")
    private Textbox txtName;
    
    @FindBy(id = "project_client_partner_id")
    private Listbox lstClientPartner;
    
    @FindBy(id = "add-team-lead")
    private Button btnAddTeamLead;
    
    @FindBy(id = "project_leads")
    private List<Listbox> lstTeamLeads;
	
    @FindBy(id = "project_status")
    private Listbox lstStatus;
	
    @FindBy(id = "project_start_date")
    private Textbox txtProjectStartDate;
    
    @FindBy(id = "project_end_date")
    private Textbox txtProjectEndDate;
    
    @FindBy(name = "commit")
    private Button btnSave;

    // *********************
    // ** Build page area **
    // *********************
    public ManageProjectModal(TestEnvironment te){
    	this.te = te;
    	ElementFactory.initElements(te.getDriver(), this);
    }
    	
    public boolean pageLoaded(){
    	return te.pageLoaded().isElementLoaded(this.getClass(), txtName); 		  
    }
    	
    // *****************************************
    // ***Page Interactions ***
    // *****************************************
    	
    //adds a new project on the projects page
    @Step("And I add a new Project")
    public void addProject(String projectName, String clientPartner, ArrayList<String> teamLeads, String status, String startDate, String endDate){
	lblAddProjectPopup.syncEnabled(te.getDriver());
    	//Fill in the details

	txtName.set(projectName);
    	lstClientPartner.select(clientPartner);
    	if (teamLeads.size() > 0){
    	    for(String teamLead : teamLeads){
    		btnAddTeamLead.click();
    		te.initializePage(this.getClass());
    		Listbox box = new ListboxImpl(lstTeamLeads.get(lstTeamLeads.size()-1));
    		box.select(teamLead);
    	    }
    	}
    	lstStatus.select(status);
    	txtProjectStartDate.safeSet(startDate);
    	txtProjectEndDate.safeSet(endDate);
    		 
    	//submit
    	btnSave.syncEnabled(te.getDriver());
    	btnSave.click();
    		    		  
    	  }
    	
    public void addProject(Project project){
	addProject(project.getProjectName(), project.getClientPartner(), project.getTeamLeads(), project.getStatus(), project.getStartDate(), project.getEndDate());   		   
    }
    
    @Step("And I modify the Project Details")
    public void modifyProject(String projectName, String clientPartner, ArrayList<String> teamLeads, ArrayList<String> teamLeadsToRemove, String status, String startDate, String endDate)  {
	lblAddProjectPopup.syncEnabled(te.getDriver());
    	if(!txtName.getText().equalsIgnoreCase(projectName)) txtName.set(clientPartner);			  
	if(!lstClientPartner.getFirstSelectedOption().getText().equalsIgnoreCase(clientPartner)) lstClientPartner.select(clientPartner);
	if (teamLeads.size() > 0){
	    boolean leadFound = false;
    	    for(String teamLead : teamLeads){
    		for(int x = 0 ; x < lstTeamLeads.size() ; x++){
    		    Listbox box = new ListboxImpl(lstTeamLeads.get(x));
    		    if(box.getFirstSelectedOption().getText().equals(teamLead)){
    			leadFound = true;
    			break;
    		    }
    		}
    		if(!leadFound){
        		btnAddTeamLead.click();
        		//te.initializePage(this.getClass());
        		Listbox box = new ListboxImpl(lstTeamLeads.get(lstTeamLeads.size()-1));
            		box.select(teamLead);
        		leadFound = false;
    		}
    	    }
    	}
	if(teamLeadsToRemove.size() > 0){
	    for(String teamLead : teamLeadsToRemove){
		for(int x = 0 ; x < lstTeamLeads.size() ; x++){
		    Listbox box = new ListboxImpl(lstTeamLeads.get(x));
    		    if(box.getFirstSelectedOption().getText().equals(teamLead)){
    			new ElementImpl(box.findElement(By.xpath("../span"))).click();
    		    }
    		}
	    }
	}
	if(!lstStatus.getFirstSelectedOption().getText().equalsIgnoreCase(status)) lstStatus.select(status);
    	if(!txtProjectStartDate.getText().equalsIgnoreCase(startDate)) txtProjectStartDate.set(startDate);
    	if(!txtProjectEndDate.getText().equalsIgnoreCase(endDate)) txtProjectEndDate.set(endDate);
        
        //submit
        btnSave.syncEnabled(te.getDriver());
        btnSave.click();
    	  }
    
    public void modifyProject(Project project){
	modifyProject(project.getProjectName(), project.getClientPartner(), project.getTeamLeads(), project.getTeamLeadsToRemove(),project.getStatus(), project.getStartDate(), project.getEndDate());   		   
	}
    
    @Step("Then the Client Partner \"{0}\" is availible for selection")
    public boolean validateClientPartnerIsAvailible(String name){
	return isOptionAvailible(lstClientPartner, name);
    }
    
    @Step("Then the Client Partner \"{0}\" is not availible for selection")
    public boolean validateClientPartnerIsNotAvailible(String name){
	return !isOptionAvailible(lstClientPartner, name);
    }
    
    @Step("Then the Team Lead \"{0}\" is availible for selection")
    public boolean validateTeamLeadIsAvailible(String name){
	btnAddTeamLead.click();
	Listbox box = new ListboxImpl(lstTeamLeads.get(0));
	return isOptionAvailible(box, name);
    }
    
    @Step("Then the Team Lead \"{0}\" is not availible for selection")
    public boolean validateTeamLeadIsNotAvailible(String name){
	btnAddTeamLead.click();
	Listbox box = new ListboxImpl(lstTeamLeads.get(0));
	return !isOptionAvailible(box, name);
    }
    
    private boolean isOptionAvailible(Listbox listbox, String name){
	List<WebElement> allOptions = listbox.getOptions();
	for(WebElement option : allOptions){
	    if(option.getText().equals(name)) return true;
	}
	return false;
    }
}
