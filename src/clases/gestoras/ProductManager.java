package clases.gestoras;

import clases.entidades.Product;
import interfaces.IProducManager;
import interfaces.IRepository;
import repository.ProductRepository;

public class ProductManager implements IProducManager {
    private IRepository<Product> repository;

    public ProductManager () {
        this.repository = new ProductRepository();
    }

    public ProductManager(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createProduct(String name, String description, int stock) {

        Product product = new Product(name, description, stock);
        if (!name.isEmpty() && !description.isEmpty() && stock > 0) {
            repository.add(product);
            System.out.println("Producto " + name + " creado con exito");
        } else {
            System.out.println("Hubo un problema al crear el producto");
        }

    }

    @Override
    public void searchProduct(int id) {
    }

    @Override
    public void getAllProducts() {
        for (Product product : repository.getAll()) {
            System.out.println(product);
        }
    }

    @Override
    public void updateProduct() {

    }

    @Override
    public void deleteProduct(int id) {

    }

}
