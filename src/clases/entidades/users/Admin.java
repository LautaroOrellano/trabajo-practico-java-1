package clases.entidades.users;

import enums.Rol;

public class Admin extends User {

    public Admin(String name, String lastName, String email, String password) {
        super(name, lastName, email, password, Rol.ADMIN);
    }

    // baja o actualizacion como polimorfismo
    @Override
    public String toString() {
        return "Admin{} " + super.toString();
    }
}
