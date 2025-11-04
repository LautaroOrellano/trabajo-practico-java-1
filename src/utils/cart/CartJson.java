package utils.cart;

import models.Cart;
import models.CartItem;
import models.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CartJson {

    // Mapeo de Carrito a Json
    public static JSONObject toJson(Cart cart) throws JSONException {
        JSONObject obj = new JSONObject();
        JSONArray productsArray = new JSONArray();

        if (cart != null && cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                JSONObject itemObj = new JSONObject();
                Product p = item.getProduct();

                itemObj.put("id", p.getId());
                itemObj.put("name", p.getName());
                itemObj.put("description", p.getDescription());
                itemObj.put("price", p.getPrice());
                itemObj.put("stock", p.getStock());
                itemObj.put("quantity", item.getQuantity());
                productsArray.put(itemObj);
            }
        }
        obj.put("items", productsArray);
        return obj;
    }

    // Mapeo de Json a Carrito
    public static Cart fromJson(JSONObject obj) throws JSONException{
        Cart cart = new Cart();
        JSONArray itemsArray = obj.getJSONArray("items");

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemsObj = itemsArray.getJSONObject(i);
            Product p = new Product(
                    itemsObj.getString("name"),
                    itemsObj.getString("description"),
                    itemsObj.getDouble("price"),
                    itemsObj.getInt("stock")
            );
            p.setId(itemsObj.getInt("id"));
            int quantity = itemsObj.getInt("quantity");
            cart.addProduct(p, quantity);
        }
        return cart;
    }
}
