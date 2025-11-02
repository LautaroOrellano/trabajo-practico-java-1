package repository;

import clases.entidades.Order;
import clases.entidades.Product;
import interfaces.IRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.JsonUtiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements IRepository<Order> {
    private final List<Order> orders = new ArrayList<>();
    private final String archivo = "json/orders.json";

    public OrderRepository() {
        loadFromJson();
    }

    // CRUD GENERICO
    @Override
    public void add(Order item) {
        orders.add(item);
        saveToJson();
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
    public void getAllCustom() {

    }

    @Override
    public void update(Order entity) {

    }

    @Override
    public boolean removeById(int id) {
        return orders.removeIf(o -> o.getId() == id);
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
                System.out.println("No hay órdenes guardadas, archivo vacío inicializado...");
                JsonUtiles.grabar(new JSONArray(), archivo);
                return;
            }

            JSONArray array = new JSONArray(jsonData);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Order order = Order.fromJson(obj);
                orders.add(order);
            }

            System.out.println("Órdenes cargadas correctamente desde JSON.");
        } catch (Exception e) {
            System.out.println("Error al leer órdenes: " + e.getMessage());
        }
    }

    private void saveToJson() {
        JSONArray array = new JSONArray();
        for (Order o : orders) {
            array.put(o.toJson());
        }

        try {
            // Crear un backup antes de sobrescribir
            JsonUtiles.grabar(array, "json-backups/orders_backup.json");

            // Grabar el archivo principal
            JsonUtiles.grabar(array, archivo);

        } catch (Exception e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Operación de guardado finalizada.");
        }
    }
}
