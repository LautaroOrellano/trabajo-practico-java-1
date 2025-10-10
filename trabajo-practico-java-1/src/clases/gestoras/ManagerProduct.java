package clases.gestoras;

import clases.entidades.Product;
import exceptions.EmptyCatalogException;
import exceptions.ItemOutOfStockException;
import exceptions.ProductNotFoundException;
import interfaces.IStock;

import java.util.*;

public class ManagerProduct implements IStock {
    private Map<Integer, Product> productCatalog;

    ///  Constructors
    public ManagerProduct() {
        this.productCatalog = new HashMap<>();
    }

    public ManagerProduct(Map<Integer, Product> productCatalog) {
        this.productCatalog = productCatalog;
    }

    ///  Methods
    // Añadir un producto a la lista
    public void addProduct(Product p){
        Integer key = p.getId();
        this.productCatalog.put(key, p);
    }

    // Eliminar un producto de la lista
    public void removeProduct(int id){
        if (!this.productCatalog.containsKey(id)){
            throw new ProductNotFoundException("ERROR: Producto inexistente");
        }

        this.productCatalog.remove(id);
    }

    // Obtener todos los productos de la lista
    public Collection<Product> getAllProducts(){
        return this.productCatalog.values();
    }

    // Ver el stock de un producto
    public int checkStock(int id){
        Product p = this.productCatalog.get(id);

        if (p == null){
            throw new ProductNotFoundException("ERROR: Producto inexistente");
        }

        return p.getStock();
    }

    // Agregar stock a un producto
    public void addStock(int id, int quantity){
        Product p = this.productCatalog.get(id);

        if (p == null){
            throw new ProductNotFoundException("ERROR: Producto inexistente");
        }

        int newStock = p.getStock() + quantity;
        p.setStock(newStock);
    }

    // Remover stock de un producto
    public void removeStock(int id, int quantity){
        Product p = this.productCatalog.get(id);

        if (p == null){
            throw new ProductNotFoundException("ERROR: Producto inexistente");
        }

        else if (p.getStock() < quantity){
            throw new ItemOutOfStockException("ERROR: La cantidad existente es menor a la solicitada");
        }

        int newStock = p.getStock() - quantity;
        p.setStock(newStock);
    }

    // Generar lista ordenada por precios
    public List<Product> sortByPrice(){
        checkCatalogNotEmpty();
        Collection<Product> productList = this.productCatalog.values();
        List<Product> sortedList = new ArrayList<>(productList);
        Collections.sort(sortedList);

        return sortedList;
    }

    // Filtrar por rango de precios
    public List<Product> getListByPriceRange(double range1, double range2){
        checkCatalogNotEmpty();
        Collection<Product> productsList = this.productCatalog.values();
        List<Product> filteredList = new ArrayList<>();
        for (Product p : productsList){
            if (p.getPrice() >= range1 && p.getPrice() <= range2){
                filteredList.add(p);
            }
        }

        if (filteredList.isEmpty()){
            System.out.println("No se encontraron productos en ese rango de precio");
        }

        return filteredList;
    }

    // Ver si el catalogo esta vacio
    public void checkCatalogNotEmpty(){
        if(this.productCatalog.isEmpty()){
            throw new EmptyCatalogException("ERROR: No existen productos dentro del catalogo");
        }
    }
}
