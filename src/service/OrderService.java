package service;

import models.Cart;
import models.Order;
import models.Product;
import models.users.User;
import exceptions.ItemOutOfStockException;
import interfaces.IOrderManager;
import repository.OrderRepository;
import repository.ProductRepository;
import repository.UserRepository;

import java.util.ArrayList;


public class OrderService implements IOrderManager {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public void generateOrderFromCart(User user) {
        User repoUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cart cart = repoUser.getCart();
        if (cart == null || cart.getProducts().isEmpty()) {
            throw new RuntimeException("El carrito está vacío, no se puede generar la orden.");
        }

        // Validamos stock antes de confirmar
        for (Product product : cart.getProducts()) {
            if (product.getStock() <= 0) {
                throw new ItemOutOfStockException("Sin stock para " + product.getName());
            }
            product.setStock(product.getStock() - 1);
        }

        // Creamos la orden con los mismos productos del carrito
        Order order = new Order(repoUser.getId(), new ArrayList<>(cart.getProducts()));

        // Total usando el método del carrito
        double total = cart.getTotalPrice();

        orderRepository.add(order);
        cart.clear();
        userRepository.update(repoUser);
        productRepository.saveToJson();

        System.out.println("✅ Orden creada correctamente.");
        System.out.println("Número de orden: " + order.getNumOrder());
        System.out.println("Total a pagar: $" + total);
    }

    public void getAllOrder() {}

    public void getMeOrder() {}

    public void searchOrder() {}

    public void removeOrder() {}
}
