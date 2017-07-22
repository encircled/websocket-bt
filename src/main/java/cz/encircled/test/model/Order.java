package cz.encircled.test.model;

import java.util.Date;

/**
 * @author Vlad on 26-Feb-17.
 */
public class Order {

    private AuctionItem auctionItem;

    private Date orderDate;

    public AuctionItem getAuctionItem() {
        return auctionItem;
    }

    public void setAuctionItem(AuctionItem auctionItem) {
        this.auctionItem = auctionItem;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

}
