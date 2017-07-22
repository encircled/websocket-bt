package cz.encircled.test.controller;

import cz.encircled.test.model.Customer;
import cz.encircled.test.model.Order;
import cz.encircled.test.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

/**
 * @author Vlad on 25-Feb-17.
 */
@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @MessageMapping("/orders")
    @SendToUser("/queue/orders")
    public List<Order> getAuctions(Principal principal) throws Exception {
        return customerService.getCustomer(principal.getName()).getOrders();
    }

    @RequestMapping("current")
    @ResponseBody
    public Customer getCurrentCustomer(Principal principal) {
        Customer customer = new Customer();
        customer.setName(principal.getName());
        return customer;
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
