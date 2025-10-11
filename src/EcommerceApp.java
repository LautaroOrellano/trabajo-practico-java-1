import clases.entidades.users.Admin;
import clases.entidades.users.Customer;
import clases.entidades.users.User;
import clases.gestoras.AuthManager;
import clases.gestoras.MenuManager;
import enums.Rol;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EcommerceApp {

    private List<User> users = new ArrayList<>();
    private AuthManager authManager = new AuthManager(users);
    private MenuManager menuManager = new MenuManager();
    private Scanner scanner = new Scanner(System.in);

    public EcommerceApp() {
        authManager.register(new Admin("Lautaro", "Orellano", "lautaro@gmail.com", "1234"));
        authManager.register(new Customer("Juan", "Perez", "juan@mail.com", "abcd",
                12345678L, 123456789L, "Calle Falsa 123", 25));
    }

    public void run() {
        User user = null;

        do {
            System.out.println("=== Bienvenido a ?? Ecommerce ===");

            System.out.println("Email: ");
            String email = scanner.nextLine();

            System.out.println("Password");
            String password = scanner.nextLine();

            user = authManager.login(email, password);

            if (user == null) {
                System.out.println("Credenciales incorrectas. Intente nuevamente.");
            }

        } while (user == null);

        System.out.println("Login exitoso. Bienvenido " + user.getName());

        int option = 0;

        do {
            user.getMenu();
            System.out.println("Elige una opcion correcta.");
            option  = scanner.nextInt();
            scanner.nextLine();

            menuManager.processOption(user, option);

        } while (option != 0);

        System.out.println("\nSesión cerrada.");

    }
}
