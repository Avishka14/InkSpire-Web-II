package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
import hibernate.Listing;
import hibernate.Product;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "AddToCart", value = "/AddToCart")
public class AddToCart extends HttpServlet {

    // ✅ Inner cart item class
    public static class cartItem {

        private int productId;
        private int listingId;
        private String title;
        private String image;
        private double price;
        private int userId;

        public cartItem(int productId , int listingId, String title, String image, double price, int userId) {
            this.productId = productId;
            this.title = title;
            this.image = image;
            this.price = price;
            this.userId = userId;
            this.listingId = listingId;
        }

        public int getProductId() {
            return productId;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }

        public double getPrice() {
            return price;
        }

        public int getUserId() {
            return userId;
        }

        public int getListingId() {
            return listingId;
        }

        public void setListingId(int listingId) {
            this.listingId = listingId;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        Gson gson = new Gson();

        JsonObject requestBody = gson.fromJson(request.getReader(), JsonObject.class);

        String userIdString = requestBody.get("userId").getAsString();

        System.out.println(userIdString);

        if (userIdString == null) {

            responseObject.addProperty("message", "User not logged in!");
            response.getWriter().write(responseObject.toString());
            return;
        }

        int userId = Integer.parseInt(userIdString);
        String productIdBody = requestBody.get("productId").getAsString();
        String listingIdBody = requestBody.get("listingId").getAsString();

        if (productIdBody == null || listingIdBody == null) {

            responseObject.addProperty("message", "Product ID and Listing ID are required!");
            response.getWriter().write(responseObject.toString());
            return;
        }

        int productId = Integer.parseInt(productIdBody);
        int listingId = Integer.parseInt(listingIdBody);

        Session session = HibernateUtill.getSessionFactory().openSession();

        try {

            Criteria productCriteria = session.createCriteria(Listing.class, "l")
                    .createAlias("l.product", "p")
                    .add(Restrictions.eq("p.id", productId))
                    .add(Restrictions.eq("l.id", listingId));

            Listing listing = (Listing) productCriteria.uniqueResult();

            if (listing == null) {

                responseObject.addProperty("message", "Product/Listing not found!");
                response.getWriter().write(responseObject.toString());
                return;
            }

            Product product = listing.getProduct();

            String baseUrl = request.getScheme() + "://"
                    + request.getServerName() + ":"
                    + request.getServerPort()
                    + request.getContextPath();

            String imagePath = "/ProductImageServlet/" + product.getId() + "/image1.png";
            String imageUrl = baseUrl + imagePath;

            HttpSession httpSession = request.getSession();
            List<cartItem> cart = (List<cartItem>) httpSession.getAttribute("cart");

            if (cart == null) {
                cart = new ArrayList<>();
            }

            boolean alreadyExists = cart.stream()
                    .anyMatch(item -> item.getProductId() == product.getId());

            if (alreadyExists) {
                responseObject.addProperty("status", false);
                responseObject.addProperty("message", "Product already in cart!");
            } else {
                cartItem item = new cartItem(
                        product.getId(),
                        listing.getId(),
                        product.getTitle(),
                        imageUrl,
                        listing.getPrice(),
                        userId
                );
                cart.add(item);
                httpSession.setAttribute("cart", cart);

                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Product added to cart successfully!");
            }

            JsonArray cartArray = new JsonArray();
            for (cartItem c : cart) {
                JsonObject cartItemJson = new JsonObject();
                cartItemJson.addProperty("productId", c.getProductId());
                cartItemJson.addProperty("listingId", c.getListingId());
                cartItemJson.addProperty("title", c.getTitle());
                cartItemJson.addProperty("imageUrl", c.getImage());
                cartItemJson.addProperty("price", c.getPrice());
                cartArray.add(cartItemJson);
            }
            responseObject.add("cart", cartArray);

        } catch (Exception e) {
            e.printStackTrace();
            responseObject.addProperty("message", "Error: " + e.getMessage());
        } finally {
            session.close();
        }

        response.setContentType("application/json");
        response.getWriter().write(responseObject.toString());
    }
}
