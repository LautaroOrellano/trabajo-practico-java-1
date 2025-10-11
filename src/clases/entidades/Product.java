package clases.entidades;

import exceptions.ItemOutOfStockException;
import exceptions.NegativeStockException;

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
