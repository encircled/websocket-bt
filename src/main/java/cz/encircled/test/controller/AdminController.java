package cz.encircled.test.controller;

import cz.encircled.test.model.AuctionItem;
import cz.encircled.test.service.AuctionService;
import cz.encircled.test.service.BidService;
import cz.encircled.test.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * @author Vlad on 19-Mar-17.
 */
@Controller
public class AdminController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private BidService bidService;

    @MessageMapping("/admin/togglebids")
    public void toggleAutoBids() {
        bidService.setAutoBidsEnabled(!bidService.getAutoBidsEnabled());
    }

    @MessageMapping("/admin/reset")
    public void reset() {
        auctionService.init();
        customerService.init();
    }

    @MessageMapping("/admin/decrement")
    public void decrementCountdown() {
        auctionService.getAllAuctions().stream().filter(AuctionItem::isNotSold).forEach(a -> {
            a.setFinishDate(new Date(a.getFinishDate().getTime() - (1000 * 60 * 5)));
        });
    }

}
