package se.dawid.officemanager.test.object;

public class Account {

    private Integer id;
    private String username;
    private String password;
    private boolean adminPrivilege;
    private Integer employeeId;

    private Employee employee;

    public Account(int id, String username, String password, boolean adminPrivilege, Integer employeeId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.adminPrivilege = adminPrivilege;
        this.employeeId = employeeId;
    }

    public Account(int id, String username, String password, boolean adminPrivilege, Employee employee) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.adminPrivilege = adminPrivilege;
        this.employee = employee;
        this.employeeId = employee.getEmployeeID();
    }

    public Account(String username, String password, boolean adminPrivilege, Integer employeeId) {
        this.username = username;
        this.password = password;
        this.adminPrivilege = adminPrivilege;
        this.employeeId = employeeId;
    }
    public Account(String username, String password, boolean adminPrivilege, Employee employee) {
        this.username = username;
        this.password = password;
        this.adminPrivilege = adminPrivilege;
        this.employee = employee;
    }


    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean hasAdminPrivilege() {
        return adminPrivilege;
    }

    public void setAdminPrivilege(boolean adminPrivilege) {
        this.adminPrivilege = adminPrivilege;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Employee getEmployee() {
        return employee;
    }
}
