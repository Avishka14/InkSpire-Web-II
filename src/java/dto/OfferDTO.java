
package dto;

import java.util.List;


public class OfferDTO {
     private int id;
    private String offerTypeName;
    private String createdDate;
    private List<ListingDTO> listings;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOfferTypeName() {
        return offerTypeName;
    }

    public void setOfferTypeName(String offerTypeName) {
        this.offerTypeName = offerTypeName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<ListingDTO> getListings() {
        return listings;
    }

    public void setListings(List<ListingDTO> listings) {
        this.listings = listings;
    }
}
