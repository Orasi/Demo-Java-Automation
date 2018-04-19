package com.orasi.database.databaseImpl;

import com.orasi.database.Database;
import com.orasi.utils.Constants;

public class OracleDatabase extends Database {

    public OracleDatabase(String tnsName) {
        String tns = getClass()
                .getResource(Constants.TNSNAMES_PATH + "tnsnames.ora")
                .getPath().toString();
        tns = tns.substring(0, tns.lastIndexOf("/"));
        System.setProperty("oracle.net.tns_admin", tns);

        super.driver = "oracle.jdbc.driver.OracleDriver";
        super.connectionString = "jdbc:oracle:thin:@" + tnsName.toUpperCase();
    }

    public OracleDatabase(String host, String port, String sid) {
        super.driver = "oracle.jdbc.driver.OracleDriver";
        super.connectionString = "jdbc:oracle:thin:@" + host + ":" + port + "/" + sid;
    }
}
