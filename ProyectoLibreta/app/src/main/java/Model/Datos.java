package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Datos implements Serializable {

    private List<User> usuarios;


    public Datos() {
        this.usuarios=new ArrayList<>();
        iniciarU();
    }

    private void iniciarU() {
        User k = new User("Melania","Benavides","88325476");
        this.usuarios.add(k);
        k = new User("Casa","Familia","22624812");
        this.usuarios.add(k);
        k = new User("Diego","Monterrey","87101280");
        this.usuarios.add(k);
        k = new User("Dj","Hernandez","88333015");
        this.usuarios.add(k);
    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }

}
