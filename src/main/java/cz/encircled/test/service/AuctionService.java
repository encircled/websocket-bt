package cz.encircled.test.service;

import cz.encircled.test.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vlad on 04-Mar-17.
 */
@Service
public class AuctionService {

    @Autowired
    private BidService bidService;

    private List<AuctionItem> auctions;
    private List<AuctionItem> defaultItems;

    @PostConstruct
    public void init() {
        auctions = new ArrayList<>();
        // Prepare mock data
        long counter = 0L;
        AuctionItem audi = buildAuction("Audi Sport", counter++, "audi-1-min.jpg", "audi-2-min.png");
        setInitialPrice(audi, new BigDecimal(10000));
        audi.setDescription("Very nice audi sport car!");

        AuctionItem camera = buildAuction("Camera Samsung", counter++, "cam-1-min.jpg", "cam-2-min.jpg", "cam-3-min.jpg");
        setInitialPrice(camera, new BigDecimal(2500));
        camera.setDescription("Cool Samsung camera!");

        AuctionItem nikon = buildAuction("Camera Nikon", counter++, "cam-nikon-min.jpg");
        setInitialPrice(nikon, new BigDecimal(22700));
        nikon.setDescription("Cool Nikon camera!");

        AuctionItem iphone4 = buildAuction("IPhone 4", counter++, "iphone-4-1-min.jpg", "iphone-4-1-min.jpg", "iphone-4-3-min.jpg");
        setInitialPrice(iphone4, new BigDecimal(4500));
        iphone4.setDescription("Almost new iphone 4!");

        AuctionItem iphone6 = buildAuction("IPhone 6", counter++, "iphone-6-1-min.jpg", "iphone-6-1-min.jpg", "iphone-6-3-min.jpg");
        setInitialPrice(iphone6, new BigDecimal(15400));
        iphone6.setDescription("New iphone 6!");

        AuctionItem mac = buildAuction("Mac Pro", counter++, "mac-1-min.jpg", "mac-2-min.jpg");
        setInitialPrice(mac, new BigDecimal(26000));
        mac.setDescription("Extra expansive Mac!");

        AuctionItem mb = buildAuction("Mercedes-benz", counter++, "mb-1-min.jpg", "mb-2-min.png", "mb-3-min.jpg");
        setInitialPrice(mb, new BigDecimal(26000));
        mb.setDescription("Get your own mercedes-benz!");

        AuctionItem xbox1 = buildAuction("XBOX ONE", counter, "xbox-1-1-min.jpg", "xbox-1-2-min.jpg", "xbox-1-3-min.jpg");
        setInitialPrice(xbox1, new BigDecimal(9500));
        xbox1.setDescription("Enjoy XBox One!");

        auctions = Arrays.asList(camera, nikon, mb, audi, iphone4, iphone6, mac, xbox1);
        defaultItems = Arrays.asList(audi, mb);
    }

    private Date getFinishDate(long counter) {
        return new Date(new Date().getTime() + (counter * 1000 * 60 * 10));
    }

    private void setInitialPrice(AuctionItem item, BigDecimal buyNowPrice) {
        item.setBuyNowPrice(buyNowPrice);
        Bid bid = new Bid();
        bid.setBidDate(new Date());
        bid.setCustomerName(CustomerService.AI_CUSTOMER);
        bid.setAmount(buyNowPrice.divide(BigDecimal.valueOf(10), 0, 0));
        item.getBids().add(bid);
    }

    private AuctionItem buildAuction(String name, long counter, String... imageNames) {
        AuctionItem item = new AuctionItem();
        item.setId(counter++);
        item.setFinishDate(getFinishDate(counter));
        item.setName(name);
        item.setImageNames(imageNames);
        return item;
    }

    public List<AuctionItem> dashboard(Principal principal) {
        if (principal == null) {
            return defaultItems;
        }
        return auctions.stream()
                .filter(AuctionItem::isNotSold)
                .collect(Collectors.toList());
    }

    private String getUserCode(Principal principal) {
        return getUserCode(principal.getName());
    }

    private String getUserCode(String name) {
        return name.substring(0, 4);
    }

    public List<AuctionItem> search(SearchRequest request, Principal principal) {
        String needle = request.getNeedle().trim().toLowerCase();

        return dashboard(principal).stream()
                .filter(i -> i.getName().toLowerCase().contains(needle) || i.getDescription().toLowerCase().contains(needle))
                .collect(Collectors.toList());
    }

    public AuctionItem detail(Long id, Principal principal) {
        return auctions.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst().orElse(null);
    }

    public Set<AuctionItem> getAllAuctions() {
        return new HashSet<>(auctions);
    }

    public void placeBid(BidRequest request, Customer customer) {
        AuctionItem item = auctions.stream()
                .filter(i -> i.isNotSold() && i.getId().equals(request.getAuctionId()))
                .findFirst().orElseThrow(IllegalStateException::new);

        if (!item.getBids().isEmpty()) {
            if (request.getAmount().compareTo(item.getBids().last().getAmount()) < 1) {
                throw new IllegalStateException("Bid must be greater then current max bid!");
            }
        }
        Bid bid = new Bid();
        bid.setAmount(request.getAmount());
        bid.setCustomerName(customer.getName());
        bid.setBidDate(new Date());
        item.getBids().add(bid);

        bidService.sendBids(item);
    }

}
