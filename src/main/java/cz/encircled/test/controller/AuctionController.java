package cz.encircled.test.controller;

import cz.encircled.test.model.AuctionItem;
import cz.encircled.test.model.SearchRequest;
import cz.encircled.test.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

/**
 * @author Vlad on 20-Feb-17.
 */
@Controller
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @SubscribeMapping("/dashboard")
    public List<AuctionItem> dashboard(Principal principal) {
        return auctionService.dashboard(principal);
    }


    @MessageMapping("/search")
    @SendTo("/queue/search")
    public List<AuctionItem> search(SearchRequest searchRequest, Principal principal) {
        return auctionService.dashboard(principal).subList(0, 1);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
