package service;

import models.Product;
import models.users.Admin;
import models.users.Customer;
import models.users.User;
import enums.Rol;
import repository.OrderRepository;
import repository.ProductRepository;
import repository.UserRepository;

import java.util.Scanner;

public class MenuService {

    // Repositorios compartidos
    private final ProductRepository productRepository = new ProductRepository();
    private final UserRepository userRepository = new UserRepository(productRepository);
    private final OrderRepository orderRepository = new OrderRepository();

    // Managers que usan los repositorios compartidos
    private final ProductService productManager;
    private final UserService userManager;
    private final OrderService orderManager;

    public MenuService() {
        this.productManager = new ProductService(productRepository);
        this.userManager = new UserService(userRepository, productRepository);
        this.orderManager = new OrderService(orderRepository, userRepository, productRepository);
    }

    //Getters
    public ProductService getProductManager() { return productManager; }
    public UserService getUserManager() { return userManager; }
    public OrderService getOrderManager() { return orderManager; }


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
        String nameRegex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"; // Solo letras y espacios

        do {
            System.out.println("Ingrese su nombre: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
            } else if (!name.matches(nameRegex)) {
                System.out.println("El nombre solo puede contener letras.");
                name = ""; // Forzar repetición del bucle
            }
        } while (name.isEmpty());

        do {
            System.out.println("Ingrese su apellido: ");
            lastName = scanner.nextLine().trim();
            if (lastName.isEmpty()) {
                System.out.println("El apellido no puede estar vacío.");
            } else if (!lastName.matches(nameRegex)) {
                System.out.println("El apellido solo puede contener letras.");
                lastName = "";
            }
        } while (lastName.isEmpty());

        do {
            System.out.println("Ingrese su email: ");
            email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("El email no puede estar vacío.");
            } else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                System.out.println("El formato del email no es válido.");
                email = "";
            }
        } while (email.isEmpty());

        do {
            System.out.println("Ingrese su password: ");
            password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("La contraseña no puede estar vacía.");
            } else if (password.length() < 6) {
                System.out.println("La contraseña debe tener al menos 6 caracteres.");
                password = "";
            }
        } while (password.isEmpty());

        return new Customer(name, lastName, email, password);

    }

    public User showMenuLogin(Scanner scanner, AuthService authManager) {
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
        System.out.println("[5] Eliminar un producto     |");
        System.out.println("[6] Ver ordenes              |");
        System.out.println("[7] Buscar orden por id      |");
        System.out.println("[8] Eliminar una orden       |");
        System.out.println("[9] Crear un usuario         |");
        System.out.println("[10] Buscar usuario por id    |");
        System.out.println("[11] Ver todo los usuarios    |");
        System.out.println("[12] Modificar un usuario     |");
        System.out.println("[13] Eliminar un usuario      |");
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
        System.out.println("[6] Vaciar carrito                 |");
        System.out.println("[7] Realizar compra                |");
        System.out.println("[8] Ver ultima compra              |");
        System.out.println("[9] Ver todas mis compras          |");
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
                productManager.getProducts();
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
            //case 6 -> userManager.clearMeCart();
            case 7 -> orderManager.searchOrder();
            case 8 -> orderManager.removeOrder();
            case 9 -> userManager.getAllUsers();
            case 10 -> userManager.searchUserById(1);
            case 11 -> userManager.searchUserById(1);
            case 12 -> userManager.searchUserById(1);
            case 13 -> userManager.searchUserById(1);
            case 0 -> System.out.printf("Hasta pronto");
            default -> System.out.println("Opcion incorrecta.");
        }
    }

    private void processCustomerOptions(User user, int option, Scanner scanner){
        switch (option) {
            case 1 -> {
                // Obtener todos los productos
                productManager.getAllProductsCustom();
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
                productManager.showAllProductsWithOption();

                int optionCart = -1;
                int quantity = 0;

                while(true) {
                    System.out.println("Elija el número del producto (o escribra 'exit' para cancelar): ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        optionCart = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese un número válido.");
                        continue;
                    }

                    if (optionCart <= 0) {
                        System.out.println("Opción fuera de rango");
                        continue;
                    }

                    Product selected = productManager.getProductByIndex(optionCart - 1);
                    if (selected == null) {
                        System.out.println("Opción fuera de rango");
                        continue;
                    }

                    while (true) {
                        System.out.println("Elija la cantidad a agregar: (o escriba 'exit' para cancelar.)");
                        String qtyInput = scanner.nextLine().trim();

                        if (qtyInput.equalsIgnoreCase("exit")) {
                            System.out.println("Operación cancelada.");
                            return;
                        }

                        try {
                            quantity = Integer.parseInt(qtyInput);
                        } catch (NumberFormatException e) {
                            System.out.println("Ingrese un número válido.");
                            continue;
                        }

                        if (quantity <= 0) {
                            System.out.println("La cantidad debe ser mayor a 0.");
                            continue;
                        }

                        if (quantity > selected.getStock()) {
                            System.out.println("Stock insuficiente. Disponible: " + selected.getStock());
                            continue;
                        }

                        break;
                    }
                    userManager.addProductToCart(user.getId(), selected, quantity);
                    System.out.println("✅ Producto agregado al carrito: " + selected.getName());
                    System.out.println("Cantidad: " + quantity);
                    break;

                }
            }
            case 4 ->
                // Mostrar productos en mi carrito
                userManager.getProductsToMeCart(user.getId());
            case 5 -> {
                // Eliminar producto de carrito
                userManager.getProductsToMeCart(user.getId());

                int optionDelete = -1;
                while (true) {
                    System.out.println("Ingrese el n° del producto que desea eliminar: (o escriba 'exit' para cancelar)");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        optionDelete = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.out.println("Debe ingresar un número válido.");
                        continue;
                    }

                    Product selected = productManager.getProductByIndex(optionDelete - 1);
                    if (selected == null) {
                        System.out.println("Opción fuera de rango");
                        continue;
                    }

                    if (optionDelete <= 0) {
                        System.out.println("El número debe ser mayor a cero.");
                        continue;
                    }
                    break;
                }

                int quantityDelete = -1;
                while (true) {
                    System.out.println("Ingrese la cantidad a eliminar (o escriba 'exit' para cancelar): ");
                    String qtyInput = scanner.nextLine().trim();

                    if (qtyInput.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        quantityDelete = Integer.parseInt(qtyInput);
                    } catch (NumberFormatException e) {
                        System.out.println("Debe ingresar un número válido.");
                        continue;
                    }

                    if (quantityDelete <= 0) {
                        System.out.println("La cantidad debe ser mayor a cero.");
                        continue;
                    }
                    break;
                }
                userManager.deleteProductToCart(user.getId(), optionDelete - 1, quantityDelete);
            }
            case 6 -> {
                // Vaciar carrito completo
                userManager.clearMeCart(user.getId());
            }
            case 7 -> {
                // Generar orden de compra
                orderManager.generateOrderFromCart(user);
            }
            case 8 ->
                    // Ver ultima orden de compra
                    orderManager.getMeOrder(user);
            case 9 ->
                    // Ver todas mis ordenes de compras
                    orderManager.getAllOrder(user);
            case 0 ->
                    // Salir del programa
                    System.out.printf("Hasta pronto");
            default -> System.out.println("Opcion incorrecta.");
        }
    }

}
