package interfaces;

import clases.entidades.Product;

public interface IUserManager {

    void getAllUsers();
    void searchUserById(int id);
    void addProductToCart(int userId, Product product);
    void getProductsToMeCart(int userId);
    void deleteProductToCart(int userId, int i);

}
