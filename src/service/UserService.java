package service;

import enums.Rol;
import models.Cart;
import models.CartItem;
import models.Product;
import exceptions.ItemOutOfStockException;
import interfaces.IUserManager;
import models.users.Admin;
import models.users.Customer;
import models.users.User;
import repository.ProductRepository;
import repository.UserRepository;

import java.util.List;

public class UserService implements IUserManager {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public UserService(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public void createUser(String name, String lastName, String email, String password, String rolInput) {
        if (name == null || name.isEmpty() || lastName == null || lastName.isEmpty() ||
                email == null || email.isEmpty() || password == null || password.isEmpty() ||
            rolInput == null || rolInput.isEmpty()) {
            System.out.println("Datos incorrectos para crear el producto. ");
            return;
        }

        Rol rol;
        try {
            // Convertimos el string a mayúsculas para que no importe cómo lo escriben
            rol = Rol.valueOf(rolInput.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Rol inválido. Debe ser 'ADMIN' o 'CUSTOMER'.");
            return;
        }

        User user;
        if (rol == Rol.ADMIN) {
            user = new Admin(name, lastName, email, password);
        } else {
            user = new Customer(name, lastName, email, password);
        }

        userRepository.add(user);
        System.out.println("Producto " + name + " creado con exito");
    }

    public void getAllUsers() {
        List<User> users = userRepository.getAll();

        if (users.isEmpty()) {
            System.out.println("No hay usuarios para mostrar.");
            return;
        }

        users.forEach(u -> {
            System.out.println("======Usuario " + u.getName() + "======");
            System.out.println("Id: " + u.getId());
            System.out.println("Nombre: " + u.getName());
            System.out.println("Apellido: " + u.getLastName());
            System.out.println("Email: " + u.getEmail());
            System.out.println("Password: " + u.getPassword());
            System.out.println("========================");
        });
    }

    public void searchUserById(int id) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        user -> System.out.println(user),
                        ()-> System.out.println("Usuario con id " + id + " no encontrado")
                );
    }

    public void updateUser(int id, String name, String lastName, String email, String password) {
        User found = userRepository.getAll()
                .stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);

        if (found != null) {
            found.setName(name);
            found.setLastName(lastName);
            found.setEmail(email);
            found.setPassword(password);
            System.out.println("Usuario actualizado con exito!");
            userRepository.update(found);
        } else {
            System.out.println("Usuario con ID: " + id + " no encontrado");
        }
    }

    public void deleteUserById(int id) {
        userRepository.removeById(id);
    }

    // ----------- Metodos logica de carrito -----------------
    public void addProductToCart(int userId, Product product, int quantity) {
        userRepository.findById(userId)
                .ifPresentOrElse(user -> {
                    if (product.getStock() >= quantity) {
                        user.addProductToCart(product, quantity);
                         userRepository.update(user);
                    } else {
                        throw new ItemOutOfStockException("Stock insuficiente");
                    }
                },
                    () -> {
                        System.out.println("Usuario con ID: " + userId + " no encontrado");
                });
    }

    public void getProductsToMeCart(int userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(user -> {
                    Cart cart = user.getCart();
                    if (cart == null || cart.isEmpty()) {
                        System.out.println("El carrito está vacío");
                        return;
                    }

                    List<CartItem> items = cart.getItems();
                    System.out.println("---------- PRODUCTOS EN CARRITO ----------");
                    double totalCarrito = 0.0;

                    for (int i = 0; i < items.size(); i++) {
                        CartItem item = items.get(i);

                        Double precioUnitarioObj = item.getProduct().getPrice();
                        double precioUnitario = precioUnitarioObj != null ? precioUnitarioObj : 0.0;

                        Double subtotalObj = item.getTotalPrice();
                        double subtotal = subtotalObj != null ? subtotalObj : 0.0;

                        totalCarrito += subtotal;

                        System.out.println((i + 1) + ". " + item.getProduct().getName() +
                                " | Cantidad: " + item.getQuantity() +
                                " | Precio unitario: $" + String.format("%.2f", precioUnitario) +
                                " | Subtotal: $" + String.format("%.2f", subtotal));
                    }

                            System.out.println("-------------------------------------------");
                            System.out.println("Total carrito: $" + String.format("%.2f", totalCarrito));
                },
                    ()-> {
                        System.out.println("Carrito no encontrado");
                    });
    }

    public void deleteProductToCart(int userId, int i, int quantity) {
        userRepository.findById(userId)
                .ifPresentOrElse( user -> {
                    Cart cart = user.getCart();
                    if (cart == null || cart.isEmpty()) {
                        System.out.println("El carrito esta vacío.");
                    }

                    List<CartItem> items = cart.getItems();

                    if (i < 0 || i >= items.size()) {
                        System.out.println("Opción inválida");
                        return;
                    }

                    CartItem item = items.get(i);
                    Product product = item.getProduct();

                    // Si la cantidad a eliminar >= cantidad en el carrito, eliminar todo
                    if (quantity >= item.getQuantity()) {
                        items.remove(i);
                        System.out.println("Se eliminó todo el producto '" + product.getName() + "' del carrito.");
                    } else {
                        // Si es menor, solo restar la cantidad
                        item.setQuantity(item.getQuantity() - quantity);
                        System.out.println("Se eliminaron " + quantity + " unidades de '" + product.getName() +
                                "' del carrito correctamente.");
                    }

                    userRepository.update(user);
                }, () -> System.out.println("Usuario ID: " + userId + " no encontrad."));
    }

    public void clearMeCart(int userId) {
        userRepository.findById(userId)
                .ifPresentOrElse( user -> {
                        user.clearCart();

                        userRepository.update(user);
                        System.out.println("Carrito vaciado con éxito");
                    },
                    () -> System.out.println("Usuario con ID " + userId + "no encontrado.")
                );
    }

    // ----------------- MÉTODOS FX -----------------

    // Devuelve lista de items del carrito del usuario
    public List<CartItem> getCartListFX(int userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Cart cart = user.getCart();
                    if (cart != null) return cart.getItems();
                    else return List.<CartItem>of();
                })
                .orElse(List.of());
    }

}




