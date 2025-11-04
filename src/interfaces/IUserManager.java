package interfaces;

import models.Product;

public interface IUserManager {

    void getAllUsers();
    void searchUserById(int id);
    void addProductToCart(int userId, Product product, int quantity);
    void getProductsToMeCart(int userId);
    void deleteProductToCart(int userId, int i, int quantity);

}
