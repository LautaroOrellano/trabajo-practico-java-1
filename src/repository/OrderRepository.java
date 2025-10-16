package repository;

import clases.entidades.Order;
import interfaces.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements IRepository<Order> {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public void add(Order item) {
        orders.add(item);
    }

    @Override
    public Optional<Order> findById(int id) {
        return orders.stream()
                    .filter(o -> o.getId() == id)
                    .findFirst();
    }

    @Override
    public Optional<Order> findByName(String numOrder) {
        return Optional.empty();
    }

    @Override
    public List<Order> getAll() {
        return orders;
    }

    @Override
    public boolean removeById(int id) {
        return orders.removeIf(o -> o.getId() == id);
    }
}
// que cada usuario que compra tenga su lista de ordenes