package clases.gestoras;

import clases.entidades.Cart;
import clases.entidades.Order;
import clases.entidades.Product;
import clases.entidades.users.User;
import exceptions.ItemOutOfStockException;
import interfaces.IOrderManager;
import repository.OrderRepository;
import repository.ProductRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


public class OrderManager implements IOrderManager {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository = new UserRepository();
    private final ProductRepository productRepository;

    public OrderManager () {
        this.orderRepository = new OrderRepository();
        this.productRepository = new ProductRepository();
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
