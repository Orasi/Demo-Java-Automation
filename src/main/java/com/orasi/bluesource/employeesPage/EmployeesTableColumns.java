package com.orasi.bluesource.employeesPage;
/**
 *
 * @author justin.phlegar@orasi.com
 * 
 * @doc.description Test data should reference these enumeration names instead of literal columns names.
 * In the case the column titles are modified, the scripts only need to update the string references
 * in this class and the test data requires no change.
 */
public enum EmployeesTableColumns {
    FIRSTNAME("First Name"),
    LASTNAME("Last Name"),
    TITLE("Title"),
    SUPERVISOR("Supervisor"),
    PROJECT("Project"),
    LOCATION("Location"),
    VACATION_DAYS_LEFT("Vacation Left"),
    SICK_DAYS_LEFT("Sick\\Personal Left"),
    FLOATING_DAYS_LEFT("Floating Left"),
    CELL_PHONE("Cell Phone");
    
    private String text = "";
    private EmployeesTableColumns(String text){this.text = text;}
    
    @Override
    public String toString(){return text;}
}
