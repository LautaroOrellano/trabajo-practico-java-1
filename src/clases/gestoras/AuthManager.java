package clases.gestoras;

import clases.entidades.users.User;

import java.util.ArrayList;
import java.util.List;

public class AuthManager {
    private List<User> users = new ArrayList<>();

    public AuthManager(List<User> users) {
        this.users = users;
    }

    public User login(String emial, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(emial) &&
                user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void register(User user) {
        users.add(user);
    }
}
