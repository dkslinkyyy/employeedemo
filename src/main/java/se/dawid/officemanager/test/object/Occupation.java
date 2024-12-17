package se.dawid.officemanager.test.object;

public class Occupation {

    private Integer occupationID;

    private String title;
    private String description;

    private Integer salary;

    public Occupation(Integer occupationID, String title, String description, Integer salary) {
        this.occupationID = occupationID;
        this.title = title;
        this.description = description;
        this.salary = salary;
    }

    public Occupation(String title, String description, Integer salary) {
        this.title = title;
        this.description = description;
        this.salary = salary;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return occupationID;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
