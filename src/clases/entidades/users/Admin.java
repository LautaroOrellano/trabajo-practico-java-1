package clases.entidades.users;

import enums.Rol;
import org.json.JSONException;
import org.json.JSONObject;

public class Admin extends User {

    public Admin(String name, String lastName, String email, String password) {
        super(name, lastName, email, password, Rol.ADMIN);
    }

    // baja o actualizacion como polimorfismo
    @Override
    public void darseDeBaja() {

    }

    @Override
    public String toString() {
        return "Admin{} " + super.toString();
    }
}
