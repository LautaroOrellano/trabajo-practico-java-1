package interfaces;

import clases.entidades.Product;

import java.util.List;

public interface IProducManager {

    void createProduct(String name, String description, double price,int stock);
    void getAllProducts();
    List<Product> getProducts();
    void searchProductById(int id);
    void searchProductByName(String name);
    void updateProduct(int id, String name, String description, double price, int stock);
    void deleteProduct(int id);
    Product searchProductFX(int id);
}

