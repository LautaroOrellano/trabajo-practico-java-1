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
        if (name == null || name.isEmpty() || description == null || description.isEmpty() || stock <= 0) {
            System.out.println("Datos incorrectos para crear el producto. ");
            return;
        }
        Product product = new Product(name, description, stock);
        repository.add(product);
        System.out.println("Producto " + name + " creado con exito");
    }

    @Override
    public void searchProduct(int id) {
        repository.findById(id)
                .ifPresentOrElse(
                        product -> System.out.println(product),
                        () -> System.out.println("Producto con ID " + id + " no encontrado.")
                );
    }

    @Override
    public void getAllProducts() {
        if(repository.getAll().isEmpty()) {
            System.out.println("No hay productos cargados actualmente.");
        }

        repository.getAll().forEach(System.out::println);
    }

    @Override
    public void updateProduct(int id, String name, String description, int stock) {
        Product found = repository.getAll()
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
       boolean removed = repository.removeById(id);
        System.out.println(removed ? "Producto eliminado con exito." : "Producto no encontrado.");
    }

}
