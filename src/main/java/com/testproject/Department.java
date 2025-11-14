package com.testproject;


public class Department {

    public String dept_no;
    public String dept_name;
    public Employee manager;

    public Department(String deptNo, String deptName) {
        this.dept_no = deptNo;
        this.dept_name = deptName;
    }

    public String getDept_no() {
        return dept_no;
    }

    public void setDept_no(String dept_no) {
        this.dept_no = dept_no;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Department[dept_no=" + dept_no + ", dept_name=" + dept_name + "]";
    }
}
