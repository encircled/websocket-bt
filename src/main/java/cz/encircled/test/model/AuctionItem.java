package cz.encircled.test.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

/**
 * @author Vlad on 19-Feb-17.
 */
public class AuctionItem {

    private Date finishDate;

    private String finishDateFormatted;

    private Long id;

    private TreeSet<Bid> bids = new TreeSet<>();

    private String name;

    private String description;

    private Boolean buyNow = false;

    private BigDecimal buyNowPrice;

    private String[] imageNames;

    private volatile boolean isSold;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBuyNowPrice() {
        return buyNowPrice;
    }

    public void setBuyNowPrice(BigDecimal buyNowPrice) {
        this.buyNow = true;
        this.buyNowPrice = buyNowPrice;
    }

    public boolean isNotSold() {
        return !isSold;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        if(finishDate != null) {
            setFinishDateFormatted(new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(finishDate));
        }
        this.finishDate = finishDate;
    }

    public String getFinishDateFormatted() {
        return finishDateFormatted;
    }

    public void setFinishDateFormatted(String finishDateFormatted) {
        this.finishDateFormatted = finishDateFormatted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuctionItem that = (AuctionItem) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
