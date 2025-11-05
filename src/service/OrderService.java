package service;

import enums.Status;
import exceptions.UserNotFoundException;
import models.Cart;
import models.CartItem;
import models.Order;
import models.Product;
import models.users.User;
import exceptions.ItemOutOfStockException;
import interfaces.IOrderManager;
import repository.OrderRepository;
import repository.ProductRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


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
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        Cart cart = repoUser.getCart();
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío, no se puede generar la orden.");
        }

        // Validamos stock antes de confirmar
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            if (product.getStock() < quantity) {
                throw new ItemOutOfStockException("Sin stock para " + product.getName());
            }
            product.setStock(product.getStock() - quantity);
        }

        // Creamos la orden con los mismos productos del carrito
        Order order = new Order(repoUser.getId(), new ArrayList<>(cart.getItems()));

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

    public void getAllOrder(User user) {
        User reposUser = userRepository.findById(user.getId())
                .orElseThrow(()-> new UserNotFoundException("Usuario no encontrado"));

        orderRepository.getAll().stream()
                // Filtra solo las órdenes del usuario actual
                .filter(o -> o.getCustomerId() == reposUser.getId())
                // Recorre cada orden y la imprime con formato
                .forEach(o -> {
                    System.out.println("---- ORDEN DE COMPRA N° " + o.getNumOrder() + " ----");
                    System.out.println("Fecha: " + o.getLocalDateTime());
                    System.out.println("Productos: ");

                    for (CartItem item : o.getProductsList()) {
                        Product p = item.getProduct();
                        System.out.printf("  • %-20s x%d  → $%.2f%n",
                                p.getName(), item.getQuantity(), item.getTotalPrice());
                    }

                    System.out.printf("Total a pagar: $%.2f%n", o.getTotalPrice());
                    System.out.println("Estado de compra: " + o.getStatus());
                    System.out.println("----------------------------------------");
                });
    }

    public void getMeOrder(User user) {
        User reposUser = userRepository.findById(user.getId())
                .orElseThrow(()-> new UserNotFoundException("Usuario no encontrado"));

        List<Order> orders = orderRepository.getAll();

        if (orders.isEmpty()) {
            System.out.println("No hay ordenes para mostrar");
            return;
        }

        boolean hasOrders = false;

        for (Order o : orders) {
            if (user.getId() == o.getCustomerId()) {
                hasOrders = true;
                System.out.println("---- ORDEN DE COMPRA N° " + o.getNumOrder() + " ----");
                System.out.println("Fecha: " + o.getLocalDateTime());
                System.out.println("Productos: " );
                for (CartItem item : o.getProductsList()) {
                    Product p = item.getProduct();
                    System.out.printf("  • %-20s x%d  → $%.2f%n", p.getName(), item.getQuantity(), item.getTotalPrice());
                }
                System.out.println("Total a pagar: " + o.getTotalPrice());
                System.out.println("Estado de compra: " + Status.PAID);
                System.out.println("----------------------------------------");
            }
        }
    }

    public void searchOrder() {}

    public void removeOrder() {}
}
