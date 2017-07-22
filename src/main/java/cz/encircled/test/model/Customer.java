package cz.encircled.test.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlad on 19-Feb-17.
 */
public class Customer {

    private String name;

    private List<Order> orders = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
