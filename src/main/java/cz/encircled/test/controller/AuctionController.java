package cz.encircled.test.controller;

import cz.encircled.test.model.*;
import cz.encircled.test.service.AuctionService;
import cz.encircled.test.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
 * @author Vlad on 20-Feb-17.
 */
@Controller
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private CustomerService customerService;

    @MessageMapping("/dashboard")
    @SendToUser("/queue/dashboard")
    public List<AuctionItem> dashboard(Principal principal) {
        return auctionService.dashboard(principal);
    }

    @MessageMapping("/search")
    @SendToUser("/queue/search")
    public List<AuctionItem> search(SearchRequest searchRequest, Principal principal) {
        return auctionService.search(searchRequest, principal);
    }

    @ResponseBody
    @RequestMapping(value = "/searchAjax", method = RequestMethod.GET)
    public List<AuctionItem> searchAjax(@RequestParam String needle, Principal principal) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setNeedle(needle);
        return auctionService.search(searchRequest, principal);
    }

    @MessageMapping("/auction")
    @SendToUser("/queue/auction")
    public AuctionItem detail(Long id) {
        return auctionService.detail(id);
    }

    @MessageMapping("/auction/order")
    @SendToUser("/queue/auction/order")
    public Order placeOrder(Long id, Principal principal) {
        AuctionItem item = auctionService.detail(id);
        if (item.isNotSold()) {
            item.setSold(true);
            Order order = new Order();
            order.setAmount(item.getBuyNowPrice());
            order.setAuctionItem(item);
            order.setOrderDate(new Date());
            Customer customer = customerService.getCustomer(principal.getName());
            customer.getOrders().add(order);

            return order;
        } else {
            throw new IllegalStateException("This item is already sold...");
        }
    }

    @MessageMapping("/auction/bid")
    public void placeBid(BidRequest request, Principal principal) {
        auctionService.placeBid(request, customerService.getCustomer(principal.getName()));
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
