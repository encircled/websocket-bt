package cz.encircled.test.service;

import cz.encircled.test.model.AuctionItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

/**
 * @author Vlad on 04-Mar-17.
 */
@Service
public class AuctionService {

    private Map<Long, AuctionItem> idToAuction = new HashMap<>();
    private Map<String, List<AuctionItem>> userToAuction = new HashMap<>();
    private List<AuctionItem> defaultItems;

    {
        // Prepare mock data
        long counter = 0L;
        AuctionItem audi = buildAuction("Audi Sport", "audi-1-min.jpg", "audi-2-min.png");
        audi.setBuyNowPrice(new BigDecimal(10000));
        audi.setId(counter++);

        AuctionItem camera = buildAuction("Camera Samsung", "cam-1-min.jpg", "cam-2-min.jpg", "cam-3-min.jpg");
        camera.setBuyNowPrice(new BigDecimal(2500));
        camera.setId(counter++);

        AuctionItem nikon = buildAuction("Camera Nikon", "cam-nikon-min.jpg");
        nikon.setBuyNowPrice(new BigDecimal(22700));
        nikon.setId(counter++);

        AuctionItem iphone4 = buildAuction("IPhone 4", "iphone-4-1-min.jpg", "iphone-4-1-min.jpg", "iphone-4-3-min.jpg");
        iphone4.setBuyNowPrice(new BigDecimal(4500));
        iphone4.setId(counter++);

        AuctionItem iphone6 = buildAuction("IPhone 6", "iphone-6-1-min.jpg", "iphone-6-1-min.jpg", "iphone-6-3-min.jpg");
        iphone6.setBuyNowPrice(new BigDecimal(15400));
        iphone6.setId(counter++);

        AuctionItem mac = buildAuction("Mac Pro", "mac-1-min.jpg", "mac-2-min.jpg");
        mac.setBuyNowPrice(new BigDecimal(26000));
        mac.setId(counter++);

        AuctionItem mb = buildAuction("Mercedes-benz", "mb-1-min.jpg", "mb-2-min.png", "mb-3-min.jpg");
        mb.setBuyNowPrice(new BigDecimal(26000));
        mb.setId(counter++);

        AuctionItem xbox1 = buildAuction("XBOX ONE", "xbox-1-1-min.jpg", "xbox-1-2-min.jpg", "xbox-1-3-min.jpg");
        xbox1.setBuyNowPrice(new BigDecimal(9500));
        xbox1.setId(counter);

        List<AuctionItem> all = Arrays.asList(camera, nikon, mb, audi, iphone4, iphone6, mac, xbox1);

        userToAuction.put("user", Arrays.asList(camera, nikon, mb, audi));
        userToAuction.put("admin", all);

        all.forEach(i -> idToAuction.put(i.getId(), i));

        defaultItems = Arrays.asList(audi, mb);
    }

    private AuctionItem buildAuction(String name, String... imageNames) {
        AuctionItem item = new AuctionItem();
        item.setId(1L);
        item.setName(name);
        item.setImageNames(imageNames);
        return item;
    }

    public List<AuctionItem> dashboard(Principal principal) {
        if(principal == null) {
            return defaultItems;
        }
        return userToAuction.getOrDefault(principal.getName(), defaultItems);
    }

}
