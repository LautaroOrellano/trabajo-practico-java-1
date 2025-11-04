package interfaces;


import models.users.User;

public interface IOrderManager {

    void generateOrderFromCart(User user);
    void getAllOrder();
    void getMeOrder();
    void searchOrder();
    void removeOrder();
}
