package com.orasi.bluesource.projectsPage;
/**
 *
 * @author justin.phlegar@orasi.com
 * 
 * @doc.description Test data should reference these enumeration names instead of literal columns names.
 * In the case the column titles are modified, the scripts only need to update the string references
 * in this class and the test data requires no change.
 */
public enum ProjectsTableColumns {
    PROJECTNAME("Project Name"),
    CLIENTPARTNER("Client Partner"),
    TEAMLEADS("Team Leads"),
    STATUS("Status");
    
    
    private String text = "";
    private ProjectsTableColumns(String text){this.text = text;}
    
    @Override
    public String toString(){return text;}
}
