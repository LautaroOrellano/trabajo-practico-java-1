package utils.cart;

import models.Cart;
import models.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CartJson {

    // Mapeo de Carrito a Json
    public static JSONObject toJson(Cart cart) throws JSONException {
        JSONObject obj = new JSONObject();
        JSONArray productsArray = new JSONArray();

        if (cart != null && cart.getProducts() != null) {
            for (Product p : cart.getProducts()) {
                JSONObject prodObj = new JSONObject();
                prodObj.put("name", p.getName());
                prodObj.put("description", p.getDescription());
                prodObj.put("price", p.getPrice());
                prodObj.put("stock", p.getStock());
                productsArray.put(prodObj);
            }
        }
        obj.put("products", productsArray);
        return obj;
    }

    // Mapeo de Json a Carrito
    public static Cart fromJson(JSONObject obj) throws JSONException{
        Cart cart = new Cart();
        JSONArray productsArray = obj.getJSONArray("products");

        for (int i = 0; i < productsArray.length(); i++) {
            JSONObject pObj = productsArray.getJSONObject(i);
            Product p = new Product(
                    pObj.getString("name"),
                    pObj.getString("description"),
                    pObj.getDouble("price"),
                    pObj.getInt("stock")
            );
            cart.addProduct(p);
        }
        return cart;
    }
}
