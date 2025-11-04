package models;

import enums.Status;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private static int AUTO_INCREMENT = 1;
    private final int id;
    private String numOrder;
    private int customerId;
    private double totalPrice;
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
        this.totalPrice = productList.stream()
                .mapToDouble(Product::getPrice)
                .sum();
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", numOrder='" + numOrder + '\'' +
                ", customerId=" + customerId +
                ", totalPrice=" + totalPrice +
                ", productsList=" + productsList +
                ", localDateTime=" + localDateTime +
                ", status=" + status +
                '}';
    }
}
