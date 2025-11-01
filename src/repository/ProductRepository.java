package repository;

import clases.entidades.Product;
import interfaces.IRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.JsonUtiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements IRepository<Product> {
    private final List<Product> products = new ArrayList<>();
    private final String archivo = "product.json";


    public ProductRepository() {
        try {
            String jsonData = JsonUtiles.leer(archivo);

            if (jsonData.isEmpty() || jsonData.equals("[]")) {
                System.out.println("No hay productos guardados, creando archivo vacío...");
                JsonUtiles.grabar(new JSONArray(), archivo);
            } else {
                JSONArray array = new JSONArray(jsonData);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Product p = Product.fromJson(obj);
                    products.add(p);
                }
            }
        } catch (JSONException e) {
            System.out.println("Error al leer productos desde json");
        }
    }

    private void saveToJson()  {
        JSONArray array = new JSONArray();
        for (Product p : products) {
            array.put(p.toJson());
        }
        JsonUtiles.grabar(array, archivo);
    }
    
    @Override
    public void add(Product item) {
        products.add(item);
        saveToJson();
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
        boolean removed = products.removeIf(p -> p.getId() == id);
        if (removed) {
            saveToJson();
        }
        return removed;
    }
}
