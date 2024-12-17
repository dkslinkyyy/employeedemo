package se.dawid.officemanager.test.object;

public class Employee {

    private int employeeID;
    private String firstName;
    private String lastName;

    private Occupation occupation;



    public Employee(int employeeID, String firstName, String lastName, Occupation occupation) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
    }


    public Employee(String firstName, String lastName, Occupation occupation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Occupation getOccupation() {return occupation;}

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }
}
