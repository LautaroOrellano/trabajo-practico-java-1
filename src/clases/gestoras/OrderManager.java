package clases.gestoras;

import clases.entidades.Cart;
import clases.entidades.Order;
import clases.entidades.Product;
import clases.entidades.users.Customer;
import clases.entidades.users.User;
import exceptions.ItemOutOfStockException;
import interfaces.IOrderManager;
import interfaces.IRepository;
import repository.OrderRepository;
import repository.ProductRepository;

import java.util.ArrayList;


public class OrderManager implements IOrderManager {
    private final IRepository<Order> orderRepository;
    private final ProductRepository productRepository;

    public OrderManager () {
        this.orderRepository = new OrderRepository();
        this.productRepository = new ProductRepository();
    }

    public Order generateOrderFromCart(User user) {
        Cart cart = user.getCart();
        if (cart.getProducts().isEmpty()) {
            throw new RuntimeException("El carrito está vacío, no se puede generar la orden.");
        }

        // Validamos stock antes de confirmar
        for (Product product : cart.getProducts()) {
            if (product.getStock() <= 0) {
                throw new ItemOutOfStockException("Sin stock para " + product.getName());
            }
        }

        // Descontamos stock
        for (Product product : cart.getProducts()) {
            product.setStock(product.getStock() - 1);
        }

        // Creamos la orden
        Order order = new Order(user.getId(), new ArrayList<>(cart.getProducts()));
        orderRepository.add(order);

        // Limpiamos el carrito
        cart.clear();

        System.out.println("Orden creada correctamente. NumOrden: " + order.getNumOrder());
        return order;
    }

    public void getAllOrder() {}

    public void getMeOrder() {}

    public void searchOrder() {}

    public void removeOrder() {}
}
