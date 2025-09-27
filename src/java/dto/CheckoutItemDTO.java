package dto;

public class CheckoutItemDTO {
    private int listingId;
    private int productId;
    private String title;
    private double price;
    private String image;

    public CheckoutItemDTO(int listingId, int productId, String title, double price, String image) {
        this.listingId = listingId;
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.image = image;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
