package repository;

import models.CartItem;
import models.Order;
import models.Product;
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
            JSONArray itemsArray = new JSONArray();
            for (CartItem item : o.getProductsList()) {
                JSONObject itemObj = new JSONObject();
                Product p = item.getProduct();

                itemObj.put("id", p.getId());
                itemObj.put("quantity", item.getQuantity());

                itemsArray.put(itemObj);
            }
            obj.put("productsList", itemsArray);

            obj.put("localDateTime", o.getLocalDateTime().toString());
            obj.put("status", o.getStatus().toString());
            obj.put("totalPrice", o.getTotalPrice());
        } catch (JSONException e) {
            System.out.println("No se pudo convertir Order a Json");
        }
        return obj;
    }

    // Conversión de JSONObject → Order
    private Order fromJson(JSONObject json) throws JSONException {
        int customerId = json.getInt("customerId");

        // Instancia del repositorio de productos para reconstruirlos
        ProductRepository productRepository = new ProductRepository();

        // Reconstruir lista de CartItem desde JSON
        List<CartItem> itemsList = new ArrayList<>();
        JSONArray itemsArray = json.getJSONArray("productsList");
        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemObj = itemsArray.getJSONObject(i);
            int productId = itemObj.getInt("id");
            int quantity = itemObj.getInt("quantity");

            // 🔹 Buscar el producto real por ID
            Product p = productRepository.findById(productId)
                    .orElseGet(() -> {
                        // Si no lo encuentra, crear un dummy para evitar nulls
                        Product dummy = new Product();
                        dummy.setId(productId);
                        dummy.setName("Producto desconocido");
                        dummy.setPrice(0.0);
                        return dummy;
                    });

            itemsList.add(new CartItem(p, quantity));
        }

        // Crear la orden con lista de CartItem
        Order order = new Order(customerId, itemsList);

        order.setNumOrder(json.getString("numOrder"));
        order.setStatus(Status.valueOf(json.getString("status")));
        order.setLocalDateTime(LocalDateTime.parse(json.getString("localDateTime")));
        order.setTotalPrice(json.getDouble("totalPrice"));

        // Mantener el ID original
        try {
            Field idField = Order.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(order, json.getInt("id"));
        } catch (Exception ignored) {}

        return order;
    }

}
