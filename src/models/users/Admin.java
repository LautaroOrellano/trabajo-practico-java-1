package models.users;

import enums.Rol;

public class Admin extends User {

    public Admin(String name, String lastName, String email, String password) {
        super(name, lastName, email, password, Rol.ADMIN);
    }

    @Override
    public String toString() {
        return "Admin{} " + super.toString();
    }
}
