package Model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String lastName;
    private String number;

    public User(String name, String lastName, String number) {
        this.name = name;
        this.lastName = lastName;
        this.number = number;
    }

    public User() {
        this(null,null,null);
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

    @Override
    public String toString() {
        return "User{" +
                "Name='" + name + '\'' +
                ", Last Name='" + lastName + '\'' +
                ", Number=" + number +
                '}';
    }
}
