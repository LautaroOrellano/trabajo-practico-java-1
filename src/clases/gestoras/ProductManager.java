package clases.gestoras;

import clases.entidades.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {

    public List<Product> products = new ArrayList<>();

    public void createProduct(String name, String description, int stock) {
        Product product = new Product(name, description, stock);
        if (!name.isEmpty() && !description.isEmpty() && stock > 0) {
            products.add(product);
            System.out.println("Producto creado con exito");
        } else {
            System.out.println("Hubo un problema al crear el producto");
        }

    }

    public void getAllProducts() {
        for (Product product : products) {
            System.out.println(product);
        }
    }

    public void updateProduct() {

    }



    public void searchProduct() {

    }
}
