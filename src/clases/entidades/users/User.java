package clases.entidades.users;

import clases.entidades.Cart;
import clases.entidades.Product;
import enums.Rol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

     void setId(int id) {
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
    public void addProductToCart(Product p) {
        if (cart == null) {
            cart = new Cart();
        }
        cart.addProduct(p);
    }

    public void removeProductFromCart(Product p) {
        cart.removeProduct(p);
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

    // Mapeador de User a JSON
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
            obj.put("type", rol.toString());
            obj.put("name", name);
            obj.put("lastName", lastName);
            obj.put("email", email);
            obj.put("password", password);

            if (cart == null) {
                obj.put("cart", new JSONArray());
            } else {
                obj.put("cart", Cart.toJson(cart));
            }

        } catch (JSONException e) {
            System.out.println("Error convirtiendo User a JSON");
        }
        return obj;
    }

    // De Json a User
    public static User fromJson(JSONObject obj) throws JSONException {
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
                customer.setCart(Cart.fromJson(obj.getJSONObject("cart")));
            }
            return customer;
        }
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
