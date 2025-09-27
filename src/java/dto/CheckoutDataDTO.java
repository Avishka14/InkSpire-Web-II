package dto;

import java.util.List;

public class CheckoutDataDTO {
    private String userId;
    private double subtotal;
    private double shipping;
    private double total;
    private List<CheckoutItemDTO> products;

    public CheckoutDataDTO(String userId, double subtotal, double shipping, double total, List<CheckoutItemDTO> products) {
        this.userId = userId;
        this.subtotal = subtotal;
        this.shipping = shipping;
        this.total = total;
        this.products = products;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<CheckoutItemDTO> getProducts() {
        return products;
    }

    public void setProducts(List<CheckoutItemDTO> products) {
        this.products = products;
    }


}
