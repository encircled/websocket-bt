package cz.encircled.test.controller;

import cz.encircled.test.model.Order;
import cz.encircled.test.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

/**
 * @author Vlad on 25-Feb-17.
 */
@Controller
public class UserController {

    @Autowired
    private CustomerService customerService;

    @MessageMapping("/orders")
    @SendToUser("/queue/orders")
    public List<Order> getAuctions(Principal principal) throws Exception {
        return customerService.getCustomer(principal).getOrders();
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
