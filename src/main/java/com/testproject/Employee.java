package com.testproject;

public class Employee {
    /**
     * Employee number
     */
    public int emp_no;

    /**
     * Employee's first name
     */
    public String first_name;

    /**
     * Employee's last name
     */
    public String last_name;

    /**
     * Employee's job title
     */
    public String title;

    /**
     * Employee's salary
     */
    public int salary;

    /**
     * Employee's current department
     */
    public String dept_name;

    /**
     * Employee's manager
     */
    public String manager;

    public Employee(){

    }

    public Employee(int emp_no, String first_name, String last_name, String title, int salary, String dept_name, String manager) {
        this.emp_no = emp_no;
        this.first_name = first_name;
        this.last_name = last_name;
        this.title = title;
        this.salary = salary;
        this.dept_name = dept_name;
        this.manager = manager;
    }

    public int getEmp_no() {
        return emp_no;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getTitle() {
        return title;
    }

    public int getSalary() {
        return salary;
    }

    public String getDept_name() {
        return dept_name;
    }

    public String getManager() {
        return manager;
    }
}

