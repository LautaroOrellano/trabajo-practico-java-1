package clases.entidades;

import java.util.Objects;

public class Product implements Comparable<Product> {
    ///  Attributes
    private static int INCREMENT = 0;
    private int id;
    private String name;
    private String description;
    private int stock;
    private double price;

    ///  Methods
    // Equals
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    // CompareTo
    @Override
    public int compareTo(Product o) {
        return Double.compare(this.price, o.price);
    }

    ///  Getters and Setters
    public static int getINCREMENT() {
        return INCREMENT;
    }

    public static void setINCREMENT(int INCREMENT) {
        Product.INCREMENT = INCREMENT;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
