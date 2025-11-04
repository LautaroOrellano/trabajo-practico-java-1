package service;

import models.users.User;
import repository.UserRepository;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
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
