package repository;

import clases.entidades.Product;
import interfaces.IRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.JsonUtiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements IRepository<Product> {
    private final List<Product> products = new ArrayList<>();
    private final String archivo = "produc.json";

    /*public ProductRepository() {
        JSONArray array = JsonUtiles.leer(archivo);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Product p = new Product(
                    obj.getString("name"),
                    obj.getString("description"),
                    obj.getInt("stock"),
                    obj.getPrice("price")
            );

    }*/
    
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
    public Optional<Product> findByName(String name) {
        return products.stream()
                       .filter(p -> p.getName().equalsIgnoreCase(name))
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
