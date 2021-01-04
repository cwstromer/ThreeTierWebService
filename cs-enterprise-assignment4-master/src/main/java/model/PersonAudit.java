package model;

import javafx.beans.property.SimpleStringProperty;

public class PersonAudit {
    private int id = 0 ;
    private String change_msg ;
    private int change_by = 0;
    private int person_id = 0;
    private String when_occurred;
    private  String name;


    public PersonAudit ( ){
    }
    public PersonAudit (  int id ,  String change_msg, int change_by, int person_id, String when_occurred , String name) {
       setName(name);
       setMsg(change_msg);
       setWhen_occurred(when_occurred);
       setId (person_id);
    }
    public int getId ( ){ return this.id;}



    public final String getChange_msg ( ){ return this.change_msg;}

    public final String getWhen_Occured () { return when_occurred;}

    public final String  getName () {
        return name;
    }

    public int getChange_by ( ){ return this.person_id;}

    public int getPerson_id ( ){ return this.person_id;}

    public final void setName ( String name) {
        this.name = name;
    }

    public final void setMsg ( String msg) {
        this.change_msg = msg;
    }
    public final void setId ( int id) {
        this.person_id = id;
    }
    public void setWhen_occurred( String occur) {
       this.when_occurred = occur;
    }

}
