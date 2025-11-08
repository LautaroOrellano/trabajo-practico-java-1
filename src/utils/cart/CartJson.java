package utils.cart;

import models.Cart;
import models.CartItem;
import models.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import repository.ProductRepository;
import service.ProductService;

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
                itemObj.put("quantity", item.getQuantity());
                productsArray.put(itemObj);
            }
        }
        obj.put("items", productsArray);
        return obj;
    }

    // Mapeo de Json a Carrito
    public static Cart fromJson(JSONObject obj, ProductRepository productRepository) throws JSONException{
        Cart cart = new Cart();

        if (obj == null || !obj.has("items")) {
            return cart;
        }

        JSONArray itemsArray = obj.getJSONArray("items");

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemsObj = itemsArray.getJSONObject(i);

            int id = itemsObj.getInt("id");
            int quantity = itemsObj.getInt("quantity");

            Product p = productRepository.findById(id).orElse(null);

            if (p != null) {
                cart.addProduct(p, quantity);
            } else {
                System.out.println("⚠️ Producto con ID " + id + " no encontrado al cargar el carrito.");
            }
        }
        return cart;
    }
}
