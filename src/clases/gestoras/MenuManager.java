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
                    productManager.getAllProducts();
                }
                case 3 -> productManager.updateProduct();
                case 4 -> orderManager.getOrder();
                case 5 -> orderManager.searchOrder();
                case 6 -> userManager.getAllUsers();
                case 7 -> userManager.searchUser();
                default -> System.out.println("Opcion incorrecta.");
            }
        } else if (user.getRol() == Rol.CUSTOMER) {
            switch (option) {
                case 1 -> productManager.getAllProducts();
                case 2 -> productManager.searchProduct(1);
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
