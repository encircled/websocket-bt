package cz.encircled.test.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Vlad on 19-Feb-17.
 */
public class Bid implements Comparable<Bid> {

    private BigDecimal amount;

    private Customer customer;

    private Date bidDate;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getBidDate() {
        return bidDate;
    }

    public void setBidDate(Date bidDate) {
        this.bidDate = bidDate;
    }

    @Override
    public int compareTo(Bid o) {
        if(o == null || o.bidDate == null) {
            return 1;
        } else {
            return bidDate.compareTo(o.bidDate);
        }
    }
}
