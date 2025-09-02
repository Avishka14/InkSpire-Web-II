
package hibernate;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "offers")
public class Offers implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "offer_type_id")
    private OfferType offerType;

   @ManyToOne
   @JoinColumn(name = "listing_id")
   private Listing listingId;

    public Offers() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OfferType getOfferTypeId() {
        return offerType;
    }

    public void setOfferTypeId(OfferType offerType) {
        this.offerType= offerType;
    }

    public Listing getListingId() {
        return listingId;
    }

    public void setListingId(Listing listingId) {
        this.listingId = listingId;
    }
    
}
