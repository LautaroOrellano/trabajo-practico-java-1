package clases.entidades;

import clases.entidades.users.Customer;
import enums.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private static int AUTO_INCREMENT = 1;
    private final int id;
    private String numOrder;
    private int customerId;
    private List<Product> productsList;
    private LocalDateTime localDateTime;
    private Status status;

    public Order(int customerId, List<Product> productList) {
        this.id = AUTO_INCREMENT++;
        this.numOrder = String.format("#%06d", id); //mismo id con 6 ceros adelante
        this.customerId = customerId;
        this.productsList = productList;
        this.localDateTime = localDateTime.now();
        this.status = Status.PENDING;
    }


    public static int getAutoIncrement() {
        return AUTO_INCREMENT;
    }

    public static void setAutoIncrement(int autoIncrement) {
        AUTO_INCREMENT = autoIncrement;
    }

    public String getNumOrder() {
        return numOrder;
    }

    public void setNumOrder(String numOrder) {
        this.numOrder = numOrder;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public JSONObject toJson()  {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
            obj.put("numOrder", numOrder);
            obj.put("customerId", customerId);

            // Product maneja su propio toJson()
            JSONArray productsArray = new JSONArray();
            for (Product p : productsList) {
                productsArray.put(p.toJson());
            }
            obj.put("productsList", productsArray);

            obj.put("localDateTime", localDateTime.toString());
            obj.put("status", status.toString());
        } catch (JSONException e) {
            System.out.println("No se pudo convertir Order a Json");
        }
        return obj;
    }

    public static Order fromJson(JSONObject json) throws JSONException{
        int customerId = json.getInt("customerId");

        // Reconstruir lista de productos desde JSON
        List<Product> productsList = new ArrayList<>();
        JSONArray productsArray = json.getJSONArray("productsList");
        for (int i = 0; i < productsArray.length(); i++) {
            productsList.add(Product.fromJson(productsArray.getJSONObject(i)));
        }

        Order order = new Order(customerId, productsList);
        order.numOrder = json.getString("numOrder");
        order.setStatus(Status.valueOf(json.getString("status")));
        order.setLocalDateTime(LocalDateTime.parse(json.getString("localDateTime")));

        // Opcional: mantener el ID original
        try {
            Field idField = Order.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(order, json.getInt("id"));
        } catch (Exception ignored) {}

        return order;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", numOrder='" + numOrder + '\'' +
                ", customerId=" + customerId +
                ", products=" + productsList +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
