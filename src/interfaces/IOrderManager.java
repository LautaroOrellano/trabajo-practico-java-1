package interfaces;


import models.users.User;

public interface IOrderManager {

    void generateOrderFromCart(User user);
    void getAllOrder(User user);
    void getMeOrder(User user);
    void searchOrder();
    void removeOrder();
}
