package clases.gestoras;

import clases.entidades.Product;
import exceptions.ProductNotFoundException;
import interfaces.IProducManager;
import interfaces.IRepository;
import repository.ProductRepository;

import java.util.List;

public class ProductManager implements IProducManager {
    private IRepository<Product> productRepository;

    public ProductManager () {
        this.productRepository = new ProductRepository();
    }

    public ProductManager(ProductRepository repository) {
        this.productRepository = repository;
    }

    @Override
    public void createProduct(String name, String description, double price, int stock) {
        if (name == null || name.isEmpty() || description == null || description.isEmpty() || stock <= 0) {
            System.out.println("Datos incorrectos para crear el producto. ");
            return;
        }
        Product product = new Product(name, description, price, stock);
        productRepository.add(product);
        System.out.println("Producto " + name + " creado con exito");
    }

    @Override
    public void searchProductById(int id) {
        productRepository.findById(id)
                .ifPresentOrElse(
                        product -> System.out.println(product),
                        () -> System.out.println("Producto con ID " + id + " no encontrado")
                );
    }

    @Override
    public void searchProductByName(String name) {
        productRepository.findByName(name)
                .ifPresentOrElse(
                        product-> {
                            System.out.println(" == Producto encontrado: ==");
                                    System.out.println("Nombre: " + product.getName());
                                    System.out.println("Precio: $" + product.getPrice());
                                    System.out.println("Stock: " + product.getStock());
                        },
                        () -> System.out.println("Producto con nombre " + name + " no encontrado")
                );
    }

    public Product productByNameAObject(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));
    }

    @Override
    public void getAllProducts() {
        if(productRepository.getAll().isEmpty()) {
            System.out.println("No hay productos cargados actualmente.");
        }

        productRepository.getAll().forEach(System.out::println);
    }

    public List<Product> getProducts() {
        return productRepository.getAll();
    }

    @Override
    public void updateProduct(int id, String name, String description, double price, int stock) {
        Product found = productRepository.getAll()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        if (found != null) {
            found.setName(name);
            found.setDescription(description);
            found.setStock(stock);
            System.out.println("Producto actualizado con exito!");
        } else {
            System.out.println("Prodcuto con ID: " + id + " no encontrado");
        }
    }

    @Override
    public void deleteProduct(int id) {
       boolean removed = productRepository.removeById(id);
        System.out.println(removed ? "Producto eliminado con exito." : "Producto no encontrado.");
    }

    // Seccion UX
    @Override
    public Product searchProductFX(int id) {
        return productRepository.findById(id)
                .orElse(null);
    }

}
