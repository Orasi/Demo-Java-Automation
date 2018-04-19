package com.orasi.database.databaseImpl;

import com.orasi.database.Database;

public class DB2Database extends Database {
    public DB2Database(String host, String port, String dbName) {
        super.driver = "COM.ibm.db2.jdbc.app.DB2Driver";
        super.connectionString = "jdbc:db2://" + host + ":" + port + "/" + dbName;
    }
}
