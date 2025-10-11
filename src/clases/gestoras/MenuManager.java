package clases.gestoras;

import clases.entidades.Product;
import clases.entidades.users.User;
import enums.Rol;

import java.util.Scanner;

public class MenuManager {

    private Scanner scanner = new Scanner(System.in);
    private ProductManager productManager = new ProductManager();
    private UserManager userManager = new UserManager();
    private OrderManager orderManager = new OrderManager();
    private ShopManager shopManager = new ShopManager();

    public void processOption(User user, int option) {
        if (user.getRol() == Rol.ADMIN) {
            switch (option) {
                case 1 -> {
                    // Crear un producto
                    System.out.println("Nombre del producto");
                    String name = scanner.nextLine();
                    System.out.println("Descripcion del producto");
                    String description = scanner.nextLine();
                    System.out.println("Stock del producto");
                    int stock = scanner.nextInt();
                    scanner.nextLine();
                    productManager.createProduct(name, description, stock);
                }
                case 2 -> {
                    // Buscar un producto por id
                    System.out.println("Id del producto a buscar");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    productManager.searchProduct(id);
                }
                case 3 -> {
                    // Listar todos los productos
                    productManager.getAllProducts();
                }
                case 4 -> {
                    // Actualizar un producto completo
                    System.out.println("Ingrese el id del producto a actualizar");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Nuevo nombre");
                    String name = scanner.nextLine();
                    System.out.println("Nueva descripcion");
                    String description = scanner.nextLine();
                    System.out.println("Nuevo stock");
                    int stock = scanner.nextInt();
                    scanner.nextLine();
                    productManager.updateProduct(id, name, description, stock);
                }
                case 5 -> {
                    // Eliminar un producto
                    System.out.println("Id del producto a eliminar");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    productManager.deleteProduct(id);
                }


                case 6 -> orderManager.getOrder();
                case 7 -> orderManager.searchOrder();
                case 8 -> userManager.getAllUsers();
                case 9 -> userManager.searchUser();
                case 0 -> System.out.printf("Hasta pronto");
                default -> System.out.println("Opcion incorrecta.");
            }
        } else if (user.getRol() == Rol.CUSTOMER) {
            switch (option) {
                case 1 -> productManager.getAllProducts();
                case 2 -> {
                    System.out.println("Id del producto a buscar");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    productManager.searchProduct(id);
                }
                case 3 -> orderManager.generateOrder();
                case 4 -> orderManager.getMeOrder();
                case 5 -> shopManager.getShop();
                case 6 -> userManager.getMeCart();
                case 7 -> userManager.addMeCart();
                default -> System.out.println("Opcion incorrecta.");
            }
        }
    }
}
