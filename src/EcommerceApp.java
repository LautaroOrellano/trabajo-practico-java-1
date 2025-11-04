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
    }

    public void run() {
        User user = null;
        int option = 0;

        do {
            while (true) {
                // Menu bienvenida
                menuManager.showWelcomeMenu();

                System.out.print("> ");

                String input = scanner.nextLine().trim();

                try {
                    option = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Debes ingresar un número válido.\n");
                }
            }

            switch (option) {
                case 1 -> {
                    // Seccion registro
                    user = menuManager.showMenuRegister(scanner);

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
                try {
                    menuManager.showMenu(user);
                    System.out.println("Elige una opcion correcta");

                    while (!scanner.hasNextInt()) {
                        System.out.println("Debes ingresar un número válido");
                        scanner.nextLine();
                    }

                    optionMenu = scanner.nextInt();
                    scanner.nextLine();

                    if(optionMenu != 0){
                        menuManager.processOption(user, optionMenu, scanner);
                    }

                } catch (Exception e) {
                    System.out.println("Ocurrio un error: " + e.getMessage());
                }
            } while (optionMenu != 0);
            System.out.println("\nSesión cerrada.");
        }
    }
}
