package cz.encircled.test.service;

import cz.encircled.test.model.Customer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vlad on 19-Mar-17.
 */
@Service
public class CustomerService {

    public static final String AI_CUSTOMER = "SYSTEM";

    private Map<String, Customer> customers;

    @PostConstruct
    public void init() {
        customers = new HashMap<>();

        Customer user = new Customer();
        user.setName("user");
        customers.put("user", user);

        Customer admin = new Customer();
        admin.setName("admin");
        customers.put("admin", admin);

        Customer ai = new Customer();
        ai.setName(AI_CUSTOMER);
        customers.put(AI_CUSTOMER, ai);
    }

    public Customer getCustomer(String name) {
        Customer customer = customers.get(name);
        if (customer != null) {
            return customer;
        }
        throw new IllegalStateException("Wrong user");
    }

}
