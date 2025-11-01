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
    UserRepository userRepository = new UserRepository();

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

    public void addProductToCart(int userId, Product product) {
        userRepository.findById(userId)
                .ifPresentOrElse(user -> {
                    if (product.getStock() > 0) {
                        user.addProductToCart(product);
                         product.setStock(product.getStock() - 1 );

                         userRepository.update(user);
                         System.out.println("Producto " + product.getName() + " agregado" );
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
                    if (user.getCart().isEmpty()) {
                        System.out.println("El carrito esta vacío");
                    } else {
                        System.out.println("Productos en el carrito: ");
                        user.getCart().getProducts().forEach(p -> {
                            System.out.println("Nombre: " + p.getName());
                            System.out.println("Descripcion: " + p.getDescription());
                            System.out.println("Precio: " + p.getPrice());
                            System.out.println("Stock: " + p.getStock());
                        });
                    }
                },
                    ()-> {
                        System.out.println("Carrito no encontrado");
                    });
    }

    public void deleteProductToCart(int userId, Product product) {
        userRepository.findById(userId)
                .ifPresentOrElse( user -> {
                            if (user.getCart().isEmpty()) {
                                System.out.println("El carrito esta vacío");
                            } else {
                                user.removeProductFromCart(product);
                                product.setStock(product.getStock() + 1);

                                userRepository.update(user);
                                System.out.println("El producto fue eliminado correctamente!");
                            }
                        },
                            () -> {
                                System.out.println("Usuario con ID: " + userId + " no encontrado.");
                            }
                );
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




