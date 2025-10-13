package interfaces;

import clases.entidades.Product;

import java.util.List;

public interface IProducManager {

    void createProduct(String name, String description, double price,int stock);
    void getAllProducts();
    List<Product> getProducts();
    void searchProduct(int id);
    Product searchProductFX(int id);
    void updateProduct(int id, String name, String description, double price, int stock);
    void deleteProduct(int id);
}

