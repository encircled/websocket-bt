package cz.encircled.test.model;

import java.math.BigDecimal;

/**
 * @author Vlad on 19-Mar-17.
 */
public class BidRequest {

    private Long auctionId;

    private BigDecimal amount;

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
