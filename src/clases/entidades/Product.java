package clases.entidades;

import exceptions.ItemOutOfStockException;
import exceptions.NegativeStockException;
import org.json.JSONException;
import org.json.JSONObject;

public class Product {

    private static int AUTO_INCREMENT = 1;
    private int id;
    private String name;
    private String description;
    private Double price;
    private int stock;

    public Product() {
    }

    public Product(String name, String description, Double price, int stock) {
        this.id = AUTO_INCREMENT++;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public void deceaseStock(int quantity) {
        if (quantity > stock) {
            throw new ItemOutOfStockException(
                    "Stock insuficiente. Disponible: " + stock + ", solicitado: " + quantity
            );
        }
    }

    public static int getAutoIncrement() {
        return AUTO_INCREMENT;
    }

    public static void setAutoIncrement(int autoIncrement) {
        AUTO_INCREMENT = autoIncrement;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new NegativeStockException("El stock no puede ser negativo");
        }
        this.stock = stock;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
            obj.put("name", name);
            obj.put("description", description);
            obj.put("price", price);
            obj.put("stock", stock);
        } catch (JSONException e) {
            System.out.println("No se pudo convertir Product a Json");
        }
        return obj;
    }

    public static Product fromJson(JSONObject json) throws JSONException {
        Product p = new Product(
                json.getString("name"),
                json.getString("description"),
                json.getDouble("price"),
                json.getInt("stock")
        );
        p.id = json.getInt("id");
        if (p.id >= AUTO_INCREMENT) {
            AUTO_INCREMENT = p.id + 1;
        }
        return p;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
