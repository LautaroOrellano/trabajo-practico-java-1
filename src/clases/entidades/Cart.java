package clases.entidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> products;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void clear() {
        products.clear();
    }

    public double getTotalPrice() {
        return products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    /*public static Cart fromJson(JSONObject json) {
        Cart cart = new Cart();
        JSONArray array = json.getJSONArray("products");
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject productJson = array.getJSONObject(i);
                Product p = new Product(
                        productJson.getString("name"),
                        productJson.getString("description"),
                        productJson.getDouble("price"),
                        productJson.getInt("stock")
                );
                cart.addProduct(p);
            }
        } catch (JSONException e) {
            System.out.println("Problema al convertir el json");
        }


        return cart;
    }*/

    public boolean isEmpty() {
        return products.isEmpty();
    }
}
