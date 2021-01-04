package model;

public class LogsView {

    private String name;
    private String allegiance;
    private String position;
    private double salary;

    public LogsView(){
    }

    public LogsView(String name, String allegiance, String position, double salary){
        setName(name);
        setAllegiance(allegiance);
        setPosition(position);
        setSalary(salary);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllegiance() {
        return allegiance;
    }

    public void setAllegiance (String allegiance){
        this.allegiance = allegiance;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
