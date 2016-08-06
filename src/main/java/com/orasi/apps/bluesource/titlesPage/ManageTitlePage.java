package com.orasi.apps.bluesource.titlesPage;

import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;

public class ManageTitlePage {
    	private OrasiDriver driver = null;

	//All the page elements
	@FindBy(id = "title_name") private Textbox txtTitle;	
	@FindBy(name = "commit") private Button btnCreateTitle;
	
	// *********************
	// ** Build page area **
	// *********************
	public ManageTitlePage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	public boolean pageLoaded(){
	    return driver.page().pageLoaded(this.getClass(), txtTitle);
	}

	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	
	//method to create a new title
	@Step("When I create the new title \"{0}\"")
	public void createNewTitle(@Parameter String newTitle){
	    enterTitleName(newTitle);
	}

	@Step("When I modify the title \"{0}\"")
	public void modifyTitle(@Parameter String title){
	    enterTitleName(title);
	}
	
	private void enterTitleName(String title){
	    txtTitle.set(title);
	    btnCreateTitle.submit();
	}
}
