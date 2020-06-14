package Model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String lastName;
    private String number;
    private int id;

    public User(String name, String lastName, String number, int id) {
        this.name = name;
        this.lastName = lastName;
        this.number = number;
        this.id=id;
    }

    public User() {
        this(null,null,null,0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", number='" + number + '\'' +
                ", id=" + id +
                '}';
    }
}
