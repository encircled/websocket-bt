package cz.encircled.test.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Vlad on 26-Feb-17.
 */
public class Order {

    private AuctionItem auctionItem;

    private Date orderDate;

    private String orderDateFormatted;

    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

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
        if(orderDate != null) {
            setOrderDateFormatted(new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(orderDate));
        }
        this.orderDate = orderDate;
    }

    public String getOrderDateFormatted() {
        return orderDateFormatted;
    }

    public void setOrderDateFormatted(String orderDateFormatted) {
        this.orderDateFormatted = orderDateFormatted;
    }
}
