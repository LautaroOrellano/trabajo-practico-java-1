package clases.gestoras;

import clases.entidades.Product;
import clases.entidades.users.User;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import exceptions.ItemOutOfStockException;
import interfaces.IUserManager;
import repository.ProductRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserManager implements IUserManager {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public UserManager(UserRepository userRepository, ProductRepository productRepository) {
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
    public void addProductToCart(int userId, Product product) {
        userRepository.findById(userId)
                .ifPresentOrElse(user -> {
                    if (product.getStock() > 0) {
                        user.addProductToCart(product);
                         product.setStock(product.getStock() - 1 );

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
                    List<Product> cart = user.getCart().getProducts();

                    if (user.getCart().isEmpty()) {
                        System.out.println("El carrito esta vacío");
                    } else {
                        System.out.println("---------- PRODUCTOS EN CARRITO ----------");
                        for (int i = 0; i < cart.size(); i++) {
                            System.out.println((i + 1) + ". " + cart.get(i).getName());
                        }
                        System.out.println("-------------------------------------------");
                    }
                },
                    ()-> {
                        System.out.println("Carrito no encontrado");
                    });
    }

    public void deleteProductToCart(int userId, int i) {
        userRepository.findById(userId)
                .ifPresentOrElse( user -> {
                    List<Product> cart = user.getCart().getProducts();

                    if (cart.isEmpty()) {
                        System.out.println("El carrito esta vacío.");
                    }

                    if (i < 0 || i >= cart.size()) {
                        System.out.println("Opción inválida");
                        return;
                    }

                    Product product = cart.get(i);
                    user.removeProductFromCart(product);
                    product.setStock(product.getStock() + 1 );

                    userRepository.update(user);
                    System.out.println("El producto '" + product.getName() + "' fue eliminado correctamente.");
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




