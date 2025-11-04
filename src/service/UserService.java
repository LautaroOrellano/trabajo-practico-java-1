package service;

import models.Cart;
import models.CartItem;
import models.Product;
import exceptions.ItemOutOfStockException;
import interfaces.IUserManager;
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

    public void getAllUsers() {
        userRepository.getAll();
    }

    public void searchUserById(int id) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        user -> System.out.println(user),
                        ()-> System.out.println("Usuario con id " + id + " no encontrado")
                );
    }

    public void deleteUserById(int id) {
        userRepository.removeById(id);
    }

    // Metodos logica de carrito
    public void addProductToCart(int userId, Product product, int quantity) {
        userRepository.findById(userId)
                .ifPresentOrElse(user -> {
                    if (product.getStock() >= quantity) {
                        user.addProductToCart(product, quantity);
                         product.setStock(product.getStock() - quantity );

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
                    for (int i = 0; i < items.size(); i++) {
                        CartItem item = items.get(i);
                        System.out.println((i + 1) + ". " + item.getProduct().getName() +
                                          " | Cantidad: " + item.getQuantity() +
                                          " | Precio unitario: " + item.getProduct().getPrice() +
                                          " | Subtotal: " + item.getTotalPrice());
                    }
                    System.out.println("-------------------------------------------");
                    System.out.println("Total carrito: " + cart.getTotalPrice());
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
                        user.removeProductFromCart(item);
                        product.setStock(product.getStock() + item.getQuantity());
                        System.out.println("Se eliminó todo el producto '" + product.getName() + "' del carrito.");
                    } else {
                        // Si es menor, solo restar la cantidad
                        item.setQuantity(item.getQuantity() - quantity);
                        product.setStock(product.getStock() + quantity);
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


}




