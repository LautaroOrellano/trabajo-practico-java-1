package models.users;

import models.Cart;
import models.CartItem;
import models.Product;
import enums.Rol;

public abstract class User {

    private static int AUTO_INCREMENT = 1;
    private int id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private Rol rol;
    private Cart cart;

    public User(){}

    public User(String name, String lastName, String email, String password, Rol rol) {
        this.id = AUTO_INCREMENT++;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.cart = new Cart();
    }

    public abstract void darseDeBaja();

    // Getters & Setters
    public static int getAutoIncrement() {
        return AUTO_INCREMENT;
    }

    public static void setAutoIncrement(int autoIncrement) {
        AUTO_INCREMENT = autoIncrement;
    }

    public int getId() {
        return id;
    }

     public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    // Manejo de carrito en usuario
    public void addProductToCart(Product p, int quantity) {
        if (cart == null) {
            cart = new Cart();
        }
        cart.addProduct(p, quantity);
    }

    public void clearCart() {
        cart.clear();
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rol=" + rol +
                '}';
    }
}
