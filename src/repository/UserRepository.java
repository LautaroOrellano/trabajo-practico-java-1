package repository;

import clases.entidades.users.User;
import interfaces.IRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.JsonUtiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IRepository<User> {
    private final List<User> users;
    private final String archivo = "users.json";

    public UserRepository() {
        this.users = new ArrayList<>();
        try {
            String jsonData = JsonUtiles.leer(archivo);

            if (jsonData.isEmpty() || jsonData.equals("[]")) {
                System.out.println("No hay usuarios guardados, creando archivo vacío...");
                JsonUtiles.grabar(new JSONArray(), archivo);
            } else {
                JSONArray array = new JSONArray(jsonData);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    User u = User.fromJson(obj);
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
            array.put(u.toJson());
        }

        try {
            // Crear un backup antes de sobrescribir
            JsonUtiles.grabar(array, "users_backup.json");

            // Grabar el archivo principal
            JsonUtiles.grabar(array, archivo);

        } catch (Exception e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Operación de guardado finalizada.");
        }

    }

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
    public boolean removeById(int id) {
        boolean removed = users.removeIf(u -> u.getId() == id);
        if (removed) {
            saveToJson();
        }
        return removed;
    }
}
