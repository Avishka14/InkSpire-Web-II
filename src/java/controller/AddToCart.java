package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
import hibernate.Listing;
import hibernate.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    public static class cartItem {

        private String title;
        private String image;
        private double price;

        public cartItem(String title, String image, double price) {
            this.title = title;
            this.image = image;
            this.price = price;
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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject requestBody = gson.fromJson(request.getReader(), JsonObject.class);

        Integer productId = requestBody.get("productId").getAsInt();
        Integer listingId = requestBody.get("listingId").getAsInt();
        Integer userId = requestBody.get("userId").getAsInt();
        

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (userId == null) {
            responseObject.addProperty("Please log in to your Account to use Cart!", false);
        } else {

            if (productId == null || listingId == null) {
                responseObject.addProperty("Prouct isn't Available!", false);
            } else {

                try {

                    Session session = HibernateUtill.getSessionFactory().openSession();
                    Transaction tx = session.beginTransaction();

                    Criteria criteria = session.createCriteria(Listing.class, "l")
                            .createAlias("l.product", "p")
                            .add(Restrictions.eq("l.id", listingId))
                            .add(Restrictions.eq("p.id", productId));

                    Listing listing = (Listing) criteria.uniqueResult();

                    tx.commit();
                    session.close();

                    if (listing != null) {
                        Product product = listing.getProduct();

                        // Generate image URL (same style you use)
                        String baseUrl = request.getScheme() + "://"
                                + request.getServerName() + ":"
                                + request.getServerPort()
                                + request.getContextPath();

                        String imagePath = "/ProductImageServlet/" + product.getId() + "/image1.png";
                        String imageUrl = baseUrl + imagePath;

                        // Create CartItem
                        cartItem item = new cartItem(product.getTitle(), imageUrl, listing.getPrice());

                        // Store in session
                        HttpSession httpSession = request.getSession();
                        List<cartItem> cart = (List<cartItem>) httpSession.getAttribute("cart");
                        if (cart == null) {
                            cart = new ArrayList<>();
                        }
                        cart.add(item);
                        httpSession.setAttribute("cart", cart);

                        // Build JSON response
                        JsonArray cartArray = new JsonArray();
                        for (cartItem c : cart) {
                            JsonObject cartJson = new JsonObject();
                            cartJson.addProperty("title", c.getTitle());
                            cartJson.addProperty("imageUrl", imageUrl);
                            cartJson.addProperty("price", c.getPrice());
                            cartArray.add(cartJson);
                        }

                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Product added to cart!");
                        responseObject.add("cart", cartArray);

                    } else {
                        responseObject.addProperty("status", false);
                        responseObject.addProperty("message", "Product/Listing not found!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    responseObject.addProperty("Error adding product to cart!", false);
                }

            }

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
