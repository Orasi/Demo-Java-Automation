package com.orasi.utils.database.databaseImpl;

import com.orasi.utils.database.Database;

public class OracleDatabase extends Database {
	private String dbGeneralUsername = "";
	private String dbGeneralPassword = "";
	
	public OracleDatabase(String environment, String tnsName){
		setDbDriver("oracle.jdbc.driver.OracleDriver");
		setDbConnectionString("jdbc:oracle:thin:@" + environment.toUpperCase() + "_" + tnsName.toUpperCase());
		
		switch(tnsName.toLowerCase()){
		case "dreams":
			setDbUserName(dbGeneralUsername);
			setDbPassword(dbGeneralPassword);
			break;
			
		case "sales":
			setDbUserName(dbGeneralUsername);
			setDbPassword(dbGeneralPassword);
			break;
		
		}
		
	}

	@Override
	protected void setDbDriver(String driver) {
		super.strDriver = driver;	
	}

	@Override
	protected void setDbConnectionString(String connection) {
		super.strConnectionString = connection;
	}

}
