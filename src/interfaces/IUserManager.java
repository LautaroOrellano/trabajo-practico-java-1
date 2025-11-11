package interfaces;

import models.CartItem;
import models.Product;

import java.util.List;

public interface IUserManager {

    void createUser(String name, String lastName, String email, String password, String rol);
    void getAllUsers();
    void searchUserById(int id);
    void updateUser(int id, String name, String lastName, String email, String password);
    void deleteUserById(int id);
    void addProductToCart(int userId, Product product, int quantity);
    void getProductsToMeCart(int userId);
    void deleteProductToCart(int userId, int i, int quantity);
    void clearMeCart(int id);
    //---FX---
    List<CartItem> getCartListFX(int userId);

}
