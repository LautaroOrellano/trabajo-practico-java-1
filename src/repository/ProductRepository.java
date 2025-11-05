package repository;

import models.Product;
import interfaces.IRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.JsonUtiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements IRepository<Product> {
    private final List<Product> products = new ArrayList<>();
    private final String archivo = "json/product.json";


    public ProductRepository() {
        loadFromJson();
    }

    // CRUD GENERICO
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
                       .filter(p -> p.getName().equals(name))
                       .findFirst();
    }

    @Override
    public List<Product> getAll() {
        return products;
    }

    @Override
    public void getAllCustom() {
        products.forEach(product -> {
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Nombre: " + product.getName());
            System.out.println("Descripcion: " + product.getDescription());
            System.out.println("Precio: " + product.getPrice());
            System.out.println("Stock: " + product.getStock());
            System.out.println("-------------------------------------------------------------------------------------");
        });
    }

    @Override
    public void showAllWithIndex() {
        if (products.isEmpty()) {
            System.out.println("No hay productos disponibles.");
            return;
        }

        System.out.println("---------- PRODUCTOS DISPONIBLES ----------");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.println((i + 1) + ". " + p.getName() + " - $" + p.getPrice() + " - Stock: " + p.getStock());
        }
        System.out.println("-------------------------------------------");
    }

    @Override
    public void update(Product updateProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updateProduct.getId()) {
                products.set(i, updateProduct);
                saveToJson();
                System.out.println("Producto actualizado.");
                return;
            }
        }
        System.out.println("No se encontró el producto con ID " + updateProduct.getId());
    }

    @Override
    public boolean removeById(int id) {
        boolean removed = products.removeIf(p -> p.getId() == id);
        if (removed) {
            saveToJson();
        }
        return removed;
    }

    private void loadFromJson() {
        try {
            // Verificar si el archivo existe antes de leerlo
            File file = new File(archivo);
            if (!file.exists()) {
                System.out.println("Archivo de órdenes no encontrado. Creando uno nuevo...");
                file.getParentFile().mkdirs(); // Crea la carpeta json/ si no existe
                JsonUtiles.grabar(new JSONArray(), archivo);
            }

            String jsonData = JsonUtiles.leer(archivo);

            if (jsonData.isEmpty() || jsonData.equals("[]")) {
                System.out.println("No hay productos guardados, creando archivo vacío...");
                JsonUtiles.grabar(new JSONArray(), archivo);
            } else {
                JSONArray array = new JSONArray(jsonData);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Product p = fromJson(obj);
                    products.add(p);
                }
            }
        } catch (JSONException e) {
            System.out.println("Error al leer productos desde json");
        }
    }

    public void saveToJson()  {
        JSONArray array = new JSONArray();
        for (Product p : products) {
            array.put(toJson(p));
        }

        try {
            // Crear un backup antes de sobrescribir
            JsonUtiles.grabar(array, "json-backups/products_backup.json");

            // Grabar el archivo principal
            JsonUtiles.grabar(array, archivo);

        } catch (Exception e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Back-up 'products.json' creado correctamente.");
        }
    }

    public static Product fromJson(JSONObject json) throws JSONException {
        int AUTO_INCREMENT = 1;

        Product p = new Product(
                json.getString("name"),
                json.getString("description"),
                json.getDouble("price"),
                json.getInt("stock")
        );
        p.setId(json.getInt("id"));

        if (p.getId() >= AUTO_INCREMENT) {
            AUTO_INCREMENT = p.getId() + 1;
        }
        return p;
    }

    public static JSONObject toJson(Product p) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", p.getId());
            obj.put("name", p.getName());
            obj.put("description", p.getDescription());
            obj.put("price", p.getPrice());
            obj.put("stock", p.getStock());
        } catch (JSONException e) {
            System.out.println("No se pudo convertir Product a Json");
        }
        return obj;
    }
}
