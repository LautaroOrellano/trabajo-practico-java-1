package clases.entidades.users;

import enums.Rol;

public class Admin extends User {

    public Admin(String name, String lastName, String email, String password) {
        super(name, lastName, email, password, Rol.ADMIN);
    }

    // baja o actualizacion como polimorfismo
    @Override
    public void getMenu() {
        System.out.println("Menu Administrador: ");
        System.out.println("[1] Crear producto");
        System.out.println("[2] Buscar producto por id");
        System.out.println("[3] Ver todos los productos");
        System.out.println("[4] Modificar producto");
        System.out.println("[5] Ver ventas");
        System.out.println("[6] Buscar venta");
        System.out.println("[7] Ver usuarios");
        System.out.println("[8] Buscar usuarios");
        System.out.println("[0] Exit");
    }

    @Override
    public String toString() {
        return "Admin{} " + super.toString();
    }
}
