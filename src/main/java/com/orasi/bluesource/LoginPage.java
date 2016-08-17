package com.orasi.bluesource;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;

public class LoginPage {
	private OrasiDriver driver = null;
	//all the page elements
	@FindBy(id = "employee_username") private Textbox txtUsername;	
	@FindBy(id = "employee_password") private Textbox txtPassword;	
	@FindBy(name = "commit") private Button btnLogin;	
	@FindBy(className = "alert-danger") private Element eleAlert;	
	
	// *********************
	// ** Build page area **
	// *********************
	public LoginPage(OrasiDriver driver){
		this.driver = driver;		
		ElementFactory.initElements(driver, this);
	}
	
	public boolean pageLoaded(){
	    return driver.page().pageLoaded(this.getClass(), btnLogin);
	}
	
	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	@Step("Login with the role \"{0}\"")
	public void login(String role) {
		String username = "";
		String password = "";
		final ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

		if (!role.toUpperCase().equals("SKIP_USER")) username = userCredentialRepo.getString("BLUESOURCE_" + role.toUpperCase());
		if (!role.toUpperCase().equals("SKIP_PASSWORD")) password = userCredentialRepo.getString("BLUESOURCE_ENCODED_PASSWORD");			
		
		txtUsername.set(username);
		txtPassword.setSecure(password);
		btnLogin.submit();
	}
	
	@Step("User did not log in successfully")
	public boolean isNotLoggedIn(){
		return btnLogin.isDisplayed();
	}
}