package repository;

import clases.entidades.Order;
import clases.entidades.Product;
import enums.Status;
import interfaces.IRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.JsonUtiles;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
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
    public void showAllWithIndex(){

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
                Order order = fromJson(obj);
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
            array.put(toJson(o));
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
            System.out.println("Back-up 'orders.json' creado correctamente.");
        }
    }

    // Conversion de Order -> JsonObject
    private JSONObject toJson(Order o)  {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", o.getId());
            obj.put("numOrder", o.getNumOrder());
            obj.put("customerId", o.getCustomerId());

            // Product maneja su propio toJson()
            JSONArray productsArray = new JSONArray();
            for (Product p : o.getProductsList()) {
                productsArray.put(ProductRepository.toJson(p));
            }
            obj.put("productsList", productsArray);

            obj.put("localDateTime", o.getLocalDateTime().toString());
            obj.put("status", o.getStatus().toString());
            obj.put("totalPrice", o.getTotalPrice());
        } catch (JSONException e) {
            System.out.println("No se pudo convertir Order a Json");
        }
        return obj;
    }

    // Conversión de JSONObject → Order
    private Order fromJson(JSONObject json) throws JSONException{
        int customerId = json.getInt("customerId");

        // Reconstruir lista de productos desde JSON
        List<Product> productsList = new ArrayList<>();
        JSONArray productsArray = json.getJSONArray("productsList");
        for (int i = 0; i < productsArray.length(); i++) {
            productsList.add(ProductRepository.fromJson(productsArray.getJSONObject(i)));
        }

        Order order = new Order(customerId, productsList);
        order.setNumOrder(json.getString("numOrder"));
        order.setStatus(Status.valueOf(json.getString("status")));
        order.setLocalDateTime(LocalDateTime.parse(json.getString("localDateTime")));

        // Opcional: mantener el ID original
        try {
            Field idField = Order.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(order, json.getInt("id"));
        } catch (Exception ignored) {}

        order.setTotalPrice(json.getDouble("totalPrice"));
        return order;
    }
}
