package com.orasi.api.restServices.blueSource.employees;

public class EmployeeSummary {

    /*
     * Sample
      {
      "id": 656,
      "status": "Permanent",
      "first_name": "Ted",
      "last_name": "Smith",
      "display_name": "Ted Smith",
      "location": "Greensboro",
      "vacation_time_left": null,
      "sick_time_left": null,
      "floating_time_left": null,
      "title": "Software Consultant",
      "manager":       {
         "id": 2,
         "first_name": "Adam",
         "last_name": "Thomas",
         "display_name": "Adam Thomas"
      },
      "project":       {
         "name": "Not billable",
         "id": null
      }
     */
    private int id;
    private String first_name;
    private String last_name;
    private String status;
    private String location;
    private String title;

    private int vacation_time_left;
    private int floating_time_left;
    private int sick_time_left;
    
    public int getId() {
        return id;
    }
    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }
   
    public String getStatus() {
        return status;
    }
    public String getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }
    public int getVacation_time_left() {
        return vacation_time_left;
    }
    public int getFloating_time_left() {
        return floating_time_left;
    }
    public int getSick_time_left() {
        return sick_time_left;
    }
    
    public void setId(int id) {
        this.id = id;
    }
   
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
   public void setLocation(String location) {
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setVacation_time_left(int vacation_time_left) {
        this.vacation_time_left = vacation_time_left;
    }
    public void setFloating_time_left(int floating_time_left) {
        this.floating_time_left = floating_time_left;
    }
    public void setSick_time_left(int sick_time_left) {
        this.sick_time_left = sick_time_left;
    }

    
}
