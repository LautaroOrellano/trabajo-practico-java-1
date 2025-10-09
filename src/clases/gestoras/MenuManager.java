package clases.gestoras;

import clases.entidades.Product;
import clases.entidades.users.User;
import enums.Rol;

public class MenuManager {

    private ProductManager productManager = new ProductManager();
    private UserManager userManager = new UserManager();
    private OrderManager orderManager = new OrderManager();
    private ShopManager shopManager = new ShopManager();

    public void processOption(User user, int option) {
        if (user.getRol() == Rol.ADMIN) {
            switch (option) {
                case 1 -> productManager.createProduct();
                case 2 -> productManager.updateProduct();
                case 3 -> orderManager.getOrder();
                case 4 -> orderManager.searchOrder();
                case 5 -> userManager.getAllUsers();
                case 6 -> userManager.searchUser();
                default -> System.out.println("Opcion incorrecta.");
            }
        } else if (user.getRol() == Rol.CUSTOMER) {
            switch (option) {
                case 1 -> productManager.getAllProducts();
                case 2 -> productManager.searchProduct();
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
