package com.testproject;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class App {
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location +
                        "/employees?useSSL=false&allowPublicKeyRetrieval=true", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public Employee getEmployees(int ID)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT emp_no, first_name, last_name "
                            + "FROM employees "
                            + "WHERE emp_no = " + ID;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                return emp;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public ArrayList<Employee> getAllSalaries()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC "
                            + "LIMIT 10";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    public void displayEmployee(Employee emp)
    {
        if (emp != null)
        {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept_name + "\n"
                            + "Manager: " + emp.manager + "\n");
        }
    }

    public void printSalaries(ArrayList<Employee> employees)
    {
        // Check employees is not null
        if (employees == null)
        {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.printf("%-10s %-15s %-20s %-8s%n", "Emp No", "First Name", "Last Name", "Salary");
        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    public Department getDepartment(String dept_name) {
        Department department = null;

        try {
            String query = "SELECT dept_no, dept_name FROM departments WHERE dept_name = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, dept_name);

            ResultSet rs = pstmt.executeQuery();

            // Debug: check if ResultSet has rows
            if (rs.next()) {
                String deptNo = rs.getString("dept_no");
                String deptName = rs.getString("dept_name");
                System.out.println("Found department: " + deptNo + " - " + deptName);
                department = new Department(deptNo, deptName);
            } else {
                System.out.println("No department found for dept_name = '" + dept_name + "'");
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            System.out.println("Error fetching department: " + e.getMessage());
            e.printStackTrace(); // more detailed error info
        }

        return department;
    }


    public ArrayList<Employee> getSalariesByDepartment(Department department) {
        ArrayList<Employee> employees = new ArrayList<>();

        try {
            String query =
                    "SELECT employees.emp_no AS emp_no, employees.first_name AS first_name, employees.last_name AS last_name, salaries.salary AS salary " +
                            "FROM employees, salaries, dept_emp, departments " +
                            "WHERE employees.emp_no = salaries.emp_no " +
                            "AND employees.emp_no = dept_emp.emp_no " +
                            "AND dept_emp.dept_no = departments.dept_no " +
                            "AND departments.dept_no = ? "+
                            "ORDER BY employees.emp_no ASC ";

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, department.dept_no);

            ResultSet rset = pstmt.executeQuery();

            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.salary = rset.getInt("salary");
                employees.add(emp);
            }

            rset.close();
            pstmt.close();

        } catch (Exception e) {
            System.out.println("Error in getSalariesByDepartment: " + e.getMessage());
        }

        return employees;
    }

    public void addEmployee(Employee emp)
    {
        try
        {
            Statement stmt = con.createStatement();
            String strUpdate =
                    "INSERT INTO employees (emp_no, first_name, last_name, birth_date, gender, hire_date) " +
                            "VALUES (" + emp.emp_no + ", '" + emp.first_name + "', '" + emp.last_name + "', " +
                            "'9999-01-01', 'M', '9999-01-01')";
            stmt.execute(strUpdate);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to add employee");
        }
    }

    /**
     * Outputs to Markdown
     *
     * @param employees
     */
    public void outputEmployees(ArrayList<Employee> employees, String filename) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees to output.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("| Emp No | First Name | Last Name | Salary |\r\n");
        sb.append("| --- | --- | --- | --- |\r\n");

        for (Employee emp : employees) {
            if (emp == null) continue;
            sb.append("| " + emp.emp_no + " | " +
                    emp.first_name + " | " + emp.last_name + " | " +
                    emp.salary + " |\r\n");
        }

        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        // Create new Application
        App a = new App();

        if (args.length < 1) {
            a.connect("localhost:33060", 3000);
        } else {
            a.connect(args[0], Integer.parseInt(args[1]));
        }

      // Extract employee salary information
      // ArrayList<Employee> employees = a.getAllSalaries();


        Department departments = a.getDepartment("Sales");
        System.out.println(departments);
        ArrayList<Employee> salaries = a.getSalariesByDepartment(departments);
        //a.outputEmployees(salaries, "DepartmentsSalaries.md");
        a.printSalaries(salaries);

        // Disconnect from database
        a.disconnect();
    }

}
