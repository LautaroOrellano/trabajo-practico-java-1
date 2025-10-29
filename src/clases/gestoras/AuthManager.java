package clases.gestoras;

import clases.entidades.users.User;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class AuthManager {
    private final UserRepository userRepository;

    public AuthManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String email, String password) {
        return userRepository.getAll().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email)
                        && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public void register(User user) {
        userRepository.add(user);
    }
}
