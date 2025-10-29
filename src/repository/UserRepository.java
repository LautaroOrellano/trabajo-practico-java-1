package repository;

import clases.entidades.Cart;
import clases.entidades.users.Admin;
import clases.entidades.users.Customer;
import clases.entidades.users.User;
import enums.Rol;
import interfaces.IRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.JsonUtiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IRepository<User> {
    private final List<User> users = new ArrayList<>();
    private final String archivo = "users.json";

    public UserRepository() {
        try {
            String jsonData = JsonUtiles.leer(archivo);

            if (jsonData.isEmpty() || jsonData.equals("[]")) {
                System.out.println("No hay usuarios guardados, creando archivo vacío...");
                JsonUtiles.grabar(new JSONArray(), archivo);
            } else {
                JSONArray array = new JSONArray(jsonData);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String type = obj.optString("type", "Customer"); // Valor por defecto

                    User u;

                    if (type.equals("Admin")) {
                        u = new Admin(
                                obj.getString("name"),
                                obj.getString("lastName"),
                                obj.getString("email"),
                                obj.getString("password")
                        );
                    } else {
                        Cart cart = null;

                        if (obj.has("cart") && !obj.isNull("cart")) {
                            cart = Cart.fromJson(obj.getJSONObject("cart"));
                        }

                        u = new Customer(
                                obj.getString("name"),
                                obj.getString("lastName"),
                                obj.getString("email"),
                                obj.getString("password")
                        );

                        if (cart != null) {
                            u.setCart(cart);
                        }

                    }
                    users.add(u);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error al leer productos desde json");
        }
    }

    private void saveToJson() {
        try {
            JSONArray array = new JSONArray();
            for (User u : users) {
                JSONObject obj = new JSONObject();
                obj.put("type", u.getRol().toString());
                obj.put("name", u.getName());
                obj.put("lastName", u.getLastName());
                obj.put("email", u.getEmail());
                obj.put("password", u.getPassword());

                if (u instanceof Customer) {
                    obj.put("cart", Cart.toJson(u.getCart()));
                }

                array.put(obj);
            }
            JsonUtiles.grabar(array, archivo);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error al guardar usuarios en JSON");
        }
    }

    @Override
    public void add(User user) {
        users.add(user);
        saveToJson();
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public boolean removeById(int id) {
        return false;
    }
}
