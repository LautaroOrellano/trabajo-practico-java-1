package repository;

import models.users.Admin;
import models.users.Customer;
import models.users.User;
import interfaces.IRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.JsonUtiles;
import utils.cart.CartJson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IRepository<User> {
    private final ProductRepository productRepository;
    private final List<User> users;
    private final String archivo = "json/users.json";

    public UserRepository(ProductRepository productRepository) {
        this.users = new ArrayList<>();
        this.productRepository = productRepository;
        loadFromJson();
    }

    // CRUD GENERICO
    @Override
    public void add(User user) {
        users.add(user);
        saveToJson();
    }

    @Override
    public Optional<User> findById(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }

    @Override
    public Optional<User> findByName(String name) {
        return users.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public void getAllCustom() {}

    @Override
    public void showAllWithIndex(){}

    public void update(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                saveToJson();
                System.out.println("Usuario actualizado.");
                return;
            }
        }
        System.out.println("No se encontró el usuario con ID " + updatedUser.getId());
    }

    @Override
    public boolean removeById(int id) {
        boolean removed = users.removeIf(u -> u.getId() == id);
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
                System.out.println("No hay usuarios guardados, creando archivo vacío...");
                JsonUtiles.grabar(new JSONArray(), archivo);
            } else {
                JSONArray array = new JSONArray(jsonData);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    User u = fromJson(obj, productRepository);
                    users.add(u);
                }
            }

            // Ajusto AUTO_INCREMENT según los IDs del JSON
            if (!users.isEmpty()) {
                int maxId = users.stream()
                        .mapToInt(User::getId)
                        .max()
                        .orElse(0);
                User.setAutoIncrement(maxId + 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error al leer productos desde json");
        }
    }

    private void saveToJson() {
        JSONArray array = new JSONArray();
        for (User u : users) {
            array.put(toJson(u));
        }

        try {
            // Crear un backup antes de sobrescribir
            JsonUtiles.grabar(array, "json-backups/users_backup.json");

            // Grabar el archivo principal
            JsonUtiles.grabar(array, archivo);

        } catch (Exception e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Back-up 'users.json' creado correctamente.");
        }
    }

    // De Json a User
    public static User fromJson(JSONObject obj, ProductRepository productRepository) throws JSONException {
        String type = obj.optString("type", "Customer");
        if (type.equalsIgnoreCase("Admin")) {
            Admin admin = new Admin(
                    obj.getString("name"),
                    obj.getString("lastName"),
                    obj.getString("email"),
                    obj.getString("password")
            );
            admin.setId(obj.getInt("id")); // Seteo id fuera de constructor por que no se los paso a las subclses
            return admin;
        } else {
            Customer customer = new Customer(
                    obj.getString("name"),
                    obj.getString("lastName"),
                    obj.getString("email"),
                    obj.getString("password")
            );
            customer.setId(obj.getInt("id"));
            if (obj.has("cart") && !obj.isNull("cart")) {
                customer.setCart(CartJson.fromJson(obj.getJSONObject("cart"), productRepository));
            }
            return customer;
        }
    }

    // Mapeador de User a JSON
    public JSONObject toJson(User u) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", u.getId());
            obj.put("type", u.getRol().toString());
            obj.put("name", u.getName());
            obj.put("lastName", u.getLastName());
            obj.put("email", u.getEmail());
            obj.put("password", u.getPassword());


            if (u.getCart() == null) {
                obj.put("cart", new JSONArray());
            } else {
                obj.put("cart", CartJson.toJson(u.getCart()));
            }

        } catch (JSONException e) {
            System.out.println("Error convirtiendo User a JSON");
        }
        return obj;
    }
}
