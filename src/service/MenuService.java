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
        System.out.println("[10] Buscar usuario por id   |");
        System.out.println("[11] Ver todo los usuarios   |");
        System.out.println("[12] Modificar un usuario    |");
        System.out.println("[13] Eliminar un usuario     |");
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
                System.out.println("=== Crear un producto ===");
                System.out.println("(Escriba 'exit' en cualquier momento para cancelar)");

                // Nombre
                String name;
                while (true) {
                    System.out.print("Nombre del producto: ");
                    name = scanner.nextLine().trim();
                    if (name.equalsIgnoreCase("exit")) {
                        System.out.println("Creación de producto cancelada.");
                        return;
                    }
                    if (!name.isBlank()) break;
                    System.out.println("El nombre no puede estar vacío.");
                }

                // Descripción
                String description;
                while (true) {
                    System.out.print("Descripción del producto: ");
                    description = scanner.nextLine().trim();
                    if (description.equalsIgnoreCase("exit")) {
                        System.out.println("Creación de producto cancelada.");
                        return;
                    }
                    if (!description.isBlank()) break;
                    System.out.println("La descripción no puede estar vacía.");
                }

                // Precio
                double price = 0;
                while (true) {
                    System.out.print("Precio del producto (usa coma, no punto): ");
                    String input = scanner.nextLine().replace(",", ".");
                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Creación de producto cancelada.");
                        return;
                    }
                    try {
                        price = Double.parseDouble(input);
                        if (price > 0) break;
                        System.out.println("El precio debe ser mayor a 0.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese un número válido para el precio.");
                    }
                }

                // Stock
                int stock = 0;
                while (true) {
                    System.out.print("Stock del producto: ");
                    String inputStock = scanner.nextLine();
                    if (inputStock.equalsIgnoreCase("exit")) {
                        System.out.println("Creación de producto cancelada.");
                        return;
                    }
                    try {
                        stock = Integer.parseInt(inputStock);
                        if (stock >= 0) break;
                        System.out.println("El stock no puede ser negativo.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese un número entero válido para el stock.");
                    }
                }

                productManager.createProduct(name, description, price, stock);
            }
            case 2 -> {
                // Buscar un producto por id
                System.out.println("=== Buscar un producto ===");
                System.out.println("(Escriba 'exit' en cualquier momento para cancelar)");

                int id;
                while (true) {
                    System.out.print("Ingrese el ID del producto a buscar: ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        id = Integer.parseInt(input);
                        if (id > 0) break;
                        System.out.println("El ID debe ser un número mayor que 0.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese solo números enteros para el ID.");
                    }
                }
                productManager.searchProductById(id);
            }
            case 3 -> {
                // Listar todos los productos
                System.out.println("=== Ver todos los productos ===");
                productManager.getAllProducts();
            }
            case 4 -> {
                // Modificar un producto por id
                System.out.println("=== Actualizar un producto ===");
                System.out.println("(Escriba 'exit' en cualquier momento para cancelar)");

                int id;
                while (true) {
                    System.out.print("Ingrese el ID del producto a actualizar: ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        id = Integer.parseInt(input);
                        if (id > 0) break;
                        System.out.println("El ID debe ser mayor que 0.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese solo números enteros para el ID.");
                    }
                }

                String name;
                while (true) {
                    System.out.print("Nuevo nombre: ");
                    name = scanner.nextLine().trim();

                    if (name.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    if (!name.isEmpty()) break;
                    System.out.println("El nombre no puede estar vacío.");
                }

                String description;
                while (true) {
                    System.out.print("Nueva descripción: ");
                    description = scanner.nextLine().trim();

                    if (description.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    if (!description.isEmpty()) break;
                    System.out.println("La descripción no puede estar vacía.");
                }

                double price;
                while (true) {
                    System.out.print("Nuevo precio: ");
                    String inputPrice = scanner.nextLine().trim().replace(",", ".");

                    if (inputPrice.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        price = Double.parseDouble(inputPrice);
                        if (price > 0) break;
                        System.out.println("El precio debe ser mayor que 0.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese un valor numérico válido para el precio.");
                    }
                }

                int stock;
                while (true) {
                    System.out.print("Nuevo stock: ");
                    String inputStock = scanner.nextLine().trim();

                    if (inputStock.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        stock = Integer.parseInt(inputStock);
                        if (stock >= 0) break;
                        System.out.println("El stock no puede ser negativo.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese solo números enteros para el stock.");
                    }
                }

                productManager.updateProduct(id, name, description, price, stock);
            }
            case 5 -> {
                // Eliminar un producto
                System.out.println("=== Eliminar un producto ===");
                System.out.println("(Escriba 'exit' en cualquier momento para cancelar)");

                int id;
                while (true) {
                    System.out.print("Ingrese el ID del producto a eliminar: ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        id = Integer.parseInt(input);
                        if (id > 0) break;
                        System.out.println("El ID debe ser mayor que 0.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese solo números enteros válidos para el ID.");
                    }
                }

                productManager.deleteProduct(id);
            }
            case 6 -> {
                // Traer todas las ordenes
                System.out.println("=== Ver todas las ordenes ===");
                orderManager.getAllOrder();
            }
            case 7 -> {
                // Traer orden por id
                System.out.println("=== Buscar una orden ===");
                System.out.println("(Escriba 'exit' en cualquier momento para cancelar)");

                int id;
                while (true) {
                    System.out.print("Ingrese el ID de la orden a buscar: ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        id = Integer.parseInt(input);
                        if (id > 0) break;
                        System.out.println("El ID debe ser un número mayor que 0.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese solo números enteros válidos para el ID.");
                    }
                }
                orderManager.getOrderByOrderId(id);
            }
            case 8 -> {
                // Eliminar orden por id
                System.out.println("=== Eliminar una orden ===");
                System.out.println("(Escriba 'exit' en cualquier momento para cancelar)");

                int id = -1;
                while (true) {
                    System.out.print("Ingrese el ID de la orden a eliminar: ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        id = Integer.parseInt(input);
                        if (id > 0) break;
                        System.out.println("El ID debe ser un número mayor que 0.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese solo números enteros válidos para el ID.");
                    }
                }
                orderManager.removeOrder(id);
            }
            case 9 -> {
                // Crear Usuario
                System.out.println("=== Crear un usuario ===");
                System.out.println("(Escriba 'exit' en cualquier momento para cancelar)");

                // Nombre
                String name;
                while (true) {
                    System.out.print("Nombre del usuario: ");
                    name = scanner.nextLine().trim();

                    if (name.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    if (!name.isBlank()) break;
                    System.out.println("El nombre no puede estar vacío.");
                }

                // Apellido
                String lastName;
                while (true) {
                    System.out.print("Apellido del usuario: ");
                    lastName = scanner.nextLine().trim();

                    if (lastName.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    if (!lastName.isBlank()) break;
                    System.out.println("La descripción no puede estar vacía.");
                }

                // Email
                String email;
                while (true) {
                    System.out.print("Email del usuairo: ");
                    email = scanner.nextLine().trim();

                    if (email.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    if (!email.isBlank()) break;
                    System.out.println("El email no puede estar vacío.");
                }

                // Contraseña
                String password;
                while (true) {
                    System.out.print("Contraseña del usuario: ");
                    password = scanner.nextLine().trim();

                    if (password.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    if (!password.isBlank()) break;
                    System.out.println("La contraseña no puede estar vacía.");
                }

                // Rol
                String rol;
                while (true) {
                    System.out.print("Rol del usuario: (Admin o Customer)");
                    rol = scanner.nextLine().trim();

                    if (rol.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    if (!rol.isBlank()) break;
                    System.out.println("El rol no puede estar vacío.");
                }

                userManager.createUser(name, lastName, email, password, rol);
            }
            case 10 -> {
                // Buscar usuario por id
                System.out.println("=== Buscar un usuario ===");
                System.out.println("(Escriba 'exit' en cualquier momento para cancelar)");

                int id;
                while (true) {
                    System.out.print("Ingrese el ID del usuario: ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        id = Integer.parseInt(input);
                        if (id > 0) break;
                        System.out.println("El ID debe ser mayor que 0.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese solo números enteros válidos para el ID.");
                    }
                }
                userManager.searchUserById(1);
            }
            case 11 -> {
                System.out.println("=== Ver todos los usuarios ===");
                userManager.getAllUsers();
            }
            case 12 -> {
                // Modificar un usuario por id
                System.out.println("=== Actualizar un usuario ===");
                System.out.println("(Escriba 'exit' en cualquier momento para cancelar)");

                int id;
                while (true) {
                    System.out.print("Ingrese el ID del usuario a actualizar: ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        id = Integer.parseInt(input);
                        if (id > 0) break;
                        System.out.println("El ID debe ser mayor que 0.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese solo números enteros para el ID.");
                    }
                }

                String name;
                while (true) {
                    System.out.print("Nuevo nombre: ");
                    name = scanner.nextLine().trim();

                    if (name.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    if (!name.isEmpty()) break;
                    System.out.println("El nombre no puede estar vacío.");
                }

                String lastName;
                while (true) {
                    System.out.print("Nuevo apellido: ");
                    lastName = scanner.nextLine().trim();

                    if (lastName.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    if (!lastName.isEmpty()) break;
                    System.out.println("El apellido no puede estar vacío.");
                }

                String email;
                while (true) {
                    System.out.print("Nuevo email: ");
                    email = scanner.nextLine().trim();

                    if (email.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    if (!email.isEmpty()) break;
                    System.out.println("El email no puede estar vacío.");
                }

                String password;
                while (true) {
                    System.out.print("Nueva contraseña: ");
                    password = scanner.nextLine().trim();

                    if (password.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    if (!password.isEmpty()) break;
                    System.out.println("La contraseña no puede estar vacía.");
                }

               userManager.updateUser(id, name, lastName, email, password);
            }
            case 13 -> {
                // Eliminar un usuario
                System.out.println("=== Eliminar un usuario ===");
                System.out.println("(Escriba 'exit' en cualquier momento para cancelar)");

                int id = -1;
                while (true) {
                    System.out.print("Ingrese el ID del usuario a eliminar: ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }

                    try {
                        id = Integer.parseInt(input);
                        if (id > 0) break;
                        System.out.println("El ID debe ser un número mayor que 0.");
                    } catch (NumberFormatException e) {
                        System.out.println("Ingrese solo números enteros válidos para el ID.");
                    }
                }
                userManager.deleteUserById(id);
            }
            case 0 -> System.out.printf("Hasta pronto");
            default -> System.out.println("Opcion incorrecta.");
        }
    }

    private void processCustomerOptions(User user, int option, Scanner scanner){
        switch (option) {
            case 1 -> {
                // Obtener todos los productos
                System.out.println("=== Ver todos los productos ===");
                productManager.getAllProductsCustom();
            }
            case 2 -> {
                // Buscar producto por nombre
                System.out.println("=== Buscar producto por nombre ===");

                String name;
                while (true) {
                    System.out.print("Ingrese el nombre del producto (o 'exit' para cancelar): ");
                    name = scanner.nextLine().trim();

                    if (name.equalsIgnoreCase("exit")) {
                        System.out.println("Búsqueda cancelada.");
                        return; // Sale del case
                    }

                    if (name.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) { // solo letras y espacios
                        break;
                    } else {
                        System.out.println("El nombre solo puede contener letras y espacios. Intente nuevamente.");
                    }
                }

                productManager.searchProductByName(name);
            }
            case 3 -> {
                // Agregar producto al carrito
                System.out.println("=== Agregar producto al carrito ===");

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
            case 4 -> {
                // Mostrar productos en mi carrito
                System.out.println("=== Ver todos los productos en mi carrito ===");
                userManager.getProductsToMeCart(user.getId());
            }
            case 5 -> {
                // Eliminar producto de carrito
                System.out.println("=== Eliminar productos en mi carrito ===");
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
                System.out.println("=== Vaciar mi carrito ===");
                userManager.clearMeCart(user.getId());
            }
            case 7 -> {
                // Generar orden de compra
                System.out.println("=== Generar compra ===");
                orderManager.generateOrderFromCart(user);
            }
            case 8 -> {
                // Ver ultima orden de compra
                System.out.println("=== Ver ultima factura ===");
                orderManager.getMeOrder(user);
            }
            case 9 ->{
                // Ver todas mis ordenes de compras
                System.out.println("=== Ver todas las facturas ===");
                orderManager.getAllOrderByUser(user);
            }
            case 0 ->
                    // Salir del programa
                    System.out.printf("Hasta pronto");
            default -> System.out.println("Opcion incorrecta.");
        }
    }
}
