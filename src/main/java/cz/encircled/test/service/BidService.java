package cz.encircled.test.service;

import cz.encircled.test.model.AuctionItem;
import cz.encircled.test.model.Bid;
import cz.encircled.test.model.Customer;
import cz.encircled.test.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Vlad on 19-Feb-17.
 */
@Service
public class BidService implements ApplicationListener<BrokerAvailabilityEvent> {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuctionService auctionService;

    private final SimpMessageSendingOperations messagingTemplate;

    private final AtomicBoolean brokerAvailable = new AtomicBoolean();

    private Boolean autoBidsEnabled = true;

    @Autowired
    public BidService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        this.brokerAvailable.set(event.isBrokerAvailable());
    }

    @Scheduled(fixedDelay = 1000)
    public void completeAuctions() {
        if (this.brokerAvailable.get()) {
            auctionService.getAllAuctions().stream().filter(AuctionItem::isNotSold).forEach(a -> {
                Date now = new Date();
                if (a.getFinishDate().before(now)) {
                    a.setSold(true);
                    Bid wonBid = a.getBids().last();
                    Customer customer = customerService.getCustomer(wonBid.getCustomerName());

                    Order order = new Order();
                    order.setOrderDate(now);
                    order.setAmount(wonBid.getAmount());
                    order.setAuctionItem(a);

                    customer.getOrders().add(order);
                }
            });
        }
    }

    @Scheduled(fixedDelay = 30 * 1000)
    public void autoBids() {
        if (this.brokerAvailable.get() && autoBidsEnabled) {
            auctionService.getAllAuctions().stream().filter(AuctionItem::isNotSold).forEach(a -> {
                Bid bid = new Bid();
                bid.setCustomerName(CustomerService.AI_CUSTOMER);
                bid.setBidDate(new Date());
                if (a.getBids().isEmpty()) {
                    bid.setAmount(BigDecimal.ONE);
                } else {
                    bid.setAmount(a.getBids().last().getAmount().multiply(BigDecimal.valueOf(1.02)).setScale(0, RoundingMode.UP));
                }
                a.getBids().add(bid);

                sendBids(a);
            });
        }
    }

    public Boolean getAutoBidsEnabled() {
        return autoBidsEnabled;
    }

    public void setAutoBidsEnabled(Boolean autoBidsEnabled) {
        this.autoBidsEnabled = autoBidsEnabled;
    }

    public void sendBids(AuctionItem a) {
        if (this.brokerAvailable.get()) {
            this.messagingTemplate.convertAndSend("/topic/auction/" + a.getId(), a.getBids());
        }
    }
}