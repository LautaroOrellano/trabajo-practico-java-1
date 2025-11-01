package clases.gestoras;

import clases.entidades.Product;
import clases.entidades.users.Admin;
import clases.entidades.users.Customer;
import clases.entidades.users.User;
import enums.Rol;
import exceptions.ProductNotFoundException;

import java.util.Scanner;

public class MenuManager {

    private ProductManager productManager = new ProductManager();
    private UserManager userManager = new UserManager();
    private OrderManager orderManager = new OrderManager();

    public void showWelcomeMenu() {
        System.out.println("------------------------------------");
        System.out.println("=== Bienvenido a UTN Ecommerce === |");
        System.out.println("------------------------------------");
        System.out.println("Elige una opcion para comenzar     |");
        System.out.println("1- Registrarse                     |");
        System.out.println("2- Iniciar Sesion                  |");
        System.out.println("0- Exit                            |");
        System.out.println("------------------------------------");
    }

    public User showMenuRegister(Scanner scanner) {
        System.out.println("--------------------------");
        System.out.println("| == Sección Registro == |");
        System.out.println("--------------------------");

        String name, lastName, email, password;

        do {
            System.out.println("Ingrese su nombre: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) System.out.println("El nombre no puede estar vacío.");
        } while (name.isEmpty());

        do {
            System.out.println("Ingrese su apellido: ");
            lastName = scanner.nextLine().trim();
            if (lastName.isEmpty()) System.out.println("El apellido no puede estar vacío.");
        } while (lastName.isEmpty());

        do {
            System.out.println("Ingrese su email: ");
            email = scanner.nextLine().trim();
            if (email.isEmpty()) System.out.println("El email no puede estar vacío.");
        } while (email.isEmpty());

        do {
            System.out.println("Ingrese su password: ");
            password = scanner.nextLine().trim();
            if (password.isEmpty()) System.out.println("La contraseña no puede estar vacía.");
        } while (password.isEmpty());

        return new Customer(name, lastName, email, password);
    }

    public User showMenuLogin(Scanner scanner, AuthManager authManager) {
        System.out.println("-----------------------");
        System.out.println("| == Sección Login == |");
        System.out.println("-----------------------");

        System.out.println("Email: ");
        String email = scanner.nextLine();

        System.out.println("Password");
        String password = scanner.nextLine();

        return authManager.login(email, password);


    }

    public void showMenu(User user) {
        if (user instanceof Admin) {
            showAdminMenu();
        } else if (user instanceof Customer) {
            showCustomerMenu();
        }
    }

    public void showAdminMenu() {
        System.out.println("------------------------------");
        System.out.println("|     == Menu Admin ==       |");
        System.out.println("------------------------------");
        System.out.println("[1] Crear producto           |");
        System.out.println("[2] Buscar producto por id   |");
        System.out.println("[3] Ver todos los productos  |");
        System.out.println("[4] Modificar producto       |");
        System.out.println("[5] Eliminar un producto       |");
        System.out.println("[6] Ver ventas               |");
        System.out.println("[7] Buscar venta             |");
        System.out.println("[8] Ver usuarios             |");
        System.out.println("[9] Buscar usuarios          |");
        System.out.println("[0] Exit                     |");
        System.out.println("------------------------------");
    }

    public void showCustomerMenu() {
        System.out.println("------------------------------------");
        System.out.println("|            == Menu ==            |");
        System.out.println("------------------------------------");
        System.out.println("[1] Ver lista de productos         |");
        System.out.println("[2] Buscar producto                |");
        System.out.println("[3] Agregar producto al carrito    |");
        System.out.println("[4] Ver mi carrito                 |");
        System.out.println("[5] Eliminar producto del carrito  |");
        System.out.println("[6] Generar orden                  |");
        System.out.println("[7] Ver mi orden                   |");
        System.out.println("[0] Salir del programa             |");
        System.out.println("------------------------------------");
    }

    public void processOption(User user, int option, Scanner scanner) {
        if (user.getRol() == Rol.ADMIN) {
            processAdminOption(option,  scanner);
        } else if (user.getRol() == Rol.CUSTOMER) {
            processCustomerOptions(user, option, scanner);
        }
    }

    private void processAdminOption(int option, Scanner scanner) {
        switch (option) {
            case 1 -> {
                // Crear un producto
                System.out.println("Nombre del producto: ");
                String name = scanner.nextLine();
                System.out.println("Descripcion del producto: ");
                String description = scanner.nextLine();
                System.out.println("Precio del producto: (--recuerda que el precio lleva coma no punto--)");
                double price = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Stock del producto");
                int stock = scanner.nextInt();
                scanner.nextLine();
                productManager.createProduct(name, description, price, stock);
            }
            case 2 -> {
                // Buscar un producto por id
                System.out.println("Id del producto a buscar");
                int id = scanner.nextInt();
                scanner.nextLine();
                productManager.searchProductById(id);
            }
            case 3 -> {
                // Listar todos los productos
                productManager.getAllProducts();
            }
            case 4 -> {
                // Actualizar un producto completo
                System.out.println("Ingrese el id del producto a actualizar");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Nuevo nombre");
                String name = scanner.nextLine();
                System.out.println("Nueva descripcion");
                String description = scanner.nextLine();
                System.out.println("Stock del producto");
                double price = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Nuevo stock");
                int stock = scanner.nextInt();
                scanner.nextLine();
                productManager.updateProduct(id, name, description, price, stock);
            }
            case 5 -> {
                // Eliminar un producto
                System.out.println("Id del producto a eliminar");
                int id = scanner.nextInt();
                scanner.nextLine();
                productManager.deleteProduct(id);
            }
            case 6 -> orderManager.getAllOrder();
            case 7 -> orderManager.searchOrder();
            case 8 -> orderManager.removeOrder();
            case 9 -> userManager.getAllUsers();
            case 10 -> userManager.searchUserbyId(1);
            case 0 -> System.out.printf("Hasta pronto");
            default -> System.out.println("Opcion incorrecta.");
        }
    }

    private void processCustomerOptions(User user, int option, Scanner scanner){
        switch (option) {
            case 1 -> {
                // Obtener todos los productos
                productManager.getAllProducts();
            }
            case 2 -> {
                // Buscar producto por name
                System.out.println("Nombre del producto a buscar");
                String name = scanner.nextLine();
                productManager.searchProductByName(name);
            }
            case 3 -> {
                // Agregar producto al carrito
                System.out.println("Que producto desea agregar al carrito?");
                String name = scanner.nextLine();
                try {
                    Product prod = productManager.productByNameAObject(name);
                    if (prod != null) {
                        userManager.addProductToCart(user.getId(), prod);
                    } else {
                        throw new ProductNotFoundException("Producto no encontrado");
                    }
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                }
            }

            case 4 ->
                // Mostrar productos en mi carrito
                userManager.getProductsToMeCart(user.getId());

            case 5 -> {
                // Eliminar producto de carrito
                System.out.println("Que producto desea eliminar del carrito?");
                userManager.getProductsToMeCart(user.getId());
                String name = scanner.nextLine();
                Product product = productManager.productByNameAObject(name);

                if (product != null ){
                    userManager.deleteProductToCart(user.getId(), product);
                } else {
                    throw new ProductNotFoundException("Producto no encontrado");
                }
            }

            case 6 -> {
                orderManager.generateOrderFromCart(user);
            }
            case 7 -> orderManager.getMeOrder();
            case 0 -> System.out.printf("Hasta pronto");
            default -> System.out.println("Opcion incorrecta.");
        }
    }

    public ProductManager getProductManager() {
        return productManager;
    }
}
