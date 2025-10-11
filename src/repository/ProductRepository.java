package repository;

import clases.entidades.Product;
import interfaces.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements IRepository<Product> {
    private final List<Product> products = new ArrayList<>();

    @Override
    public void add(Product item) {
        products.add(item);
    }

    @Override
    public Optional<Product> findById(int id) {
        return products.stream()
                       .filter(p -> p.getId() == id)
                       .findFirst();
    }

    @Override
    public List<Product> getAll() {
        return products;
    }

    @Override
    public boolean removeById(int id) {
        return products.removeIf(p -> p.getId() == id);
    }
}
