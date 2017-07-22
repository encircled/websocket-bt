package cz.encircled.test.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlad on 19-Feb-17.
 */
public class Customer {

    private String firstName;

    private String lastName;

    private List<Order> orders = new ArrayList<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
