import clases.entidades.users.Admin;
import clases.entidades.users.Customer;
import clases.entidades.users.User;
import clases.gestoras.AuthManager;
import clases.gestoras.MenuManager;
import enums.Rol;
import repository.UserRepository;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EcommerceApp {

    private UserRepository userRepository = new UserRepository();
    private AuthManager authManager = new AuthManager(userRepository);
    private MenuManager menuManager = new MenuManager();
    private Scanner scanner = new Scanner(System.in);

    public EcommerceApp() {
        authManager.register(new Admin("Lautaro", "Orellano", "lautaro@gmail.com", "1234"));
        authManager.register(new Customer("Juan", "Perez", "juan@mail.com", "abcd",
                12345678L, 123456789L, "Calle Falsa 123", 25));
    }

    public void run() {
        User user = null;
        int option = 0;

        do {
            // Menu bienvenida
            menuManager.showWelcomeMenu();

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    // Seccion registro
                    user = menuManager.showMenuRegister(scanner);

                    // Registro exitoso
                    if (user != null) {
                        authManager.register(user);
                        System.out.println("\nRegistro exitoso!!. Ya puede iniciar sesion");
                    }
                    user = null;
                }
                case 2 -> {
                    // Sección Login
                    user = menuManager.showMenuLogin(scanner, authManager);

                    if (user == null) {
                        System.out.println("Credenciales incorrectas. Intente nuevamente.");
                    } else {
                        System.out.println("\nLogin exitoso. Bienvenido " + user.getName());
                    }
                }
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opcion inválida.\n");
            }
        } while (user== null && option != 0 );

        // Entrada al menu dependiendo de rol
        if (user != null) {
            int optionMenu = 0;

            do {
                menuManager.showMenu(user);
                System.out.println("Elige una opcion correcta");
                optionMenu = scanner.nextInt();
                scanner.nextLine();

                menuManager.processOption(user, optionMenu, scanner);

            } while (optionMenu != 0);

            System.out.println("\nSesión cerrada.");
        }
    }
}
