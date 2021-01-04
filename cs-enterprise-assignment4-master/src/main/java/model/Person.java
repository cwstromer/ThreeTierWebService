package model;

// Person properties
public class Person {
    //public static final int NEW_PERSON = 0;

    private int id;
    private String dateOfBirth= " ";
    private String lastName = " ";
    private String firstName = " ";

    public Person(int id, String firstName, String lastName, String DOB) {
        this.id = id;
        //id = NEW_PERSON;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfBirth = DOB;
    }

    @Override
    public String toString() {
        return "Person: id " + id + " , "+ " name: " + firstName + " " + lastName +  "  DOB: " + dateOfBirth + "\n";
    }


    // Encapsulation
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }

    public  void setDateOfBirth (String dob) {
        this.dateOfBirth = dob;
    }
    public  String getDateOfBirth () {
        return this.dateOfBirth;
    }
    public  void setLastName (String lastName) {
        this.lastName = lastName;
    }
    public  String getLastName () {
        return this.lastName;
    }
    public void setFirstName(String firstName) { this.firstName = firstName; }
}
