package interfaces;


import models.users.User;

public interface IOrderManager {

    void generateOrderFromCart(User user);
    void getAllOrderByUser(User user);
    void getAllOrder();
    void getOrderByOrderId(int id);
    void getMeOrder(User user);
    void removeOrder(int id);
}
