package cz.encircled.test.model;

import java.math.BigDecimal;
import java.util.TreeSet;

/**
 * @author Vlad on 19-Feb-17.
 */
public class AuctionItem {

    private Long id;

    private BigDecimal minBid;

    private TreeSet<Bid> bids = new TreeSet<>();

    private String name;

    private Boolean buyNow = false;

    private BigDecimal buyNowPrice;

    private String[] imageNames;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String[] getImageNames() {
        return imageNames;
    }

    public void setImageNames(String[] imagesNames) {
        this.imageNames = imagesNames;
    }

    public TreeSet<Bid> getBids() {
        return bids;
    }

    public void setBids(TreeSet<Bid> bids) {
        this.bids = bids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBuyNow() {
        return buyNow;
    }

    public void setBuyNow(Boolean buyNow) {
        this.buyNow = buyNow;
    }

    public BigDecimal getBuyNowPrice() {
        return buyNowPrice;
    }

    public void setBuyNowPrice(BigDecimal buyNowPrice) {
        this.buyNow = true;
        this.buyNowPrice = buyNowPrice;
    }
}
