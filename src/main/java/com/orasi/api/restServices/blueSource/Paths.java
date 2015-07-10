package com.orasi.api.restServices.blueSource;

public class Paths {
    /*
     * Base Info
     */
    public final static String baseURL = "http://bluesourcestaging.herokuapp.com";
    public final static String root = baseURL + "/" ;
    
    /*
     * API Area
     */
    public final static String api_subordinates = root + "api/subordinates" ;
    public final static String api_department = root + "api/department" ;
    public final static String api_department_list = root + "api/department_list" ;
    public final static String api_manager = root + "api/manager" ;
    
    /*
     * Admin
     */
    public final static String admin_index = root + "admin/index" ;
    public final static String admin_departments = root + "admin/departments" ;
    
    /*
     * Login
     */

    public final static String login = root + "login" ;
    public final static String logout = root + "logout" ;
    public final static String login_issue = root + "login_issue" ;
    public final static String saml_login = root + "auth/saml/callback" ;
    
    /*
     * Titles
     */
    public final static String titles = root + "admin/titles" ;
    public final static String title = root + "admin/titles/:id" ;
    
    /*
     * Departments
     */
    public final static String department_sub_departments = root + "departments/:department_id/sub_departments" ;
    public final static String department_employees = root + "departments/:department_id/employees" ;
    public final static String departments = root + "departments" ;
    public final static String new_department = root + "departments/new" ;
    public final static String edit_department = root + "departments/:id/edit" ;
    public final static String department = root + "departments/:id" ;
    
    /*
     * Employees
     */
    public final static String view_employee_vacations = root + "employees/:employee_id/vacations/view" ;
    public final static String requests_employee_vacations = root + "employees/:employee_id/vacations/requests" ;
    public final static String cancel_employee_vacation = root + "employees/:employee_id/vacations/:id/cancel" ;
    public final static String employee_vacations = root + "employees/:employee_id/vacations" ;
    public final static String employee_vacation = root + "employees/:employee_id/vacations/:id" ;
    public final static String employee_project_histories = root + "employees/:employee_id/projects" ;
    public final static String new_employee_project_history = root + "employees/:employee_id/projects/new" ;
    public final static String edit_employee_project_history = root + "employees/:employee_id/projects/:id/edit" ;
    public final static String employee_project_history = root + "employees/:employee_id/projects/:id" ;
    public final static String employee_preferences = root + "employees/:employee_id/preferences" ;
    public final static String employees = root + "employees" ;
    public final static String new_employee = root + "employees/new" ;
    public final static String edit_employee = root + "employees/:id/edit" ;
    public final static String employee = root + "employees/:id" ;
    
    /*
     * Projects
     */
    public final static String project_leads = root + "projects/:project_id/leads" ;
    public final static String projects = root + "projects" ;
    public final static String edit_project = root + "projects/:id/edit" ;
    public final static String project = root + "projects/:id" ;
    
    /*
     * General items
     */
    public final static String issue = root + "issue" ;
    public final static String search = root + "search" ;
    public final static String directory_employees = root + "directory/employees" ;
    public final static String directory = root + "directory" ;
    public final static String calendar = root + "calendar" ;
    public final static String report_calendar = root + "calendar/report" ;
}
