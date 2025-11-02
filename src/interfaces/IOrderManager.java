package interfaces;


import clases.entidades.Order;
import clases.entidades.users.User;

import java.util.List;

public interface IOrderManager {

    void generateOrderFromCart(User user);
    void getAllOrder();
    void getMeOrder();
    void searchOrder();
    void removeOrder();
}
