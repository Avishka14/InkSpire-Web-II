package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.HibernateUtill;
import hibernate.Listing;
import hibernate.Product;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

@WebServlet(name = "LoadLatestProducts", urlPatterns = {"/LoadLatestProducts"})
public class LoadLatestProducts extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session session = HibernateUtill.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        int limit = 12;
        try {
            String limitParam = request.getParameter("limit");
            if (limitParam != null) {
                limit = Integer.parseInt(limitParam);
            }
        } catch (NumberFormatException e) {

            limit = 12;
        }

        Criteria c = session.createCriteria(Listing.class, "i")
                .addOrder(Order.desc("i.listing_date"))
                .setMaxResults(limit);

        List<Listing> listings = c.list();

        tx.commit();
        session.close();

        JsonArray dealsArray = new JsonArray();

        for (Listing listing : listings) {

            Product product = listing.getProduct();
            Category category = product.getCategory();

            if (listing.getListingId().getId() == 1) {

                JsonObject productJson = new JsonObject();
                productJson.addProperty("productName", product.getTitle());
                productJson.addProperty("productCategory", category.getValue());
                productJson.addProperty("productPrice", String.valueOf(listing.getPrice()));
                productJson.addProperty("id", listing.getId());
                productJson.addProperty("proId", product.getId());

                String baseUrl = request.getScheme() + "://"
                        + request.getServerName() + ":"
                        + request.getServerPort()
                        + request.getContextPath();

                String imagePath = "/ProductImageServlet/" + product.getId() + "/image1.png";
                File productImage = new File("C:/InkSpireUploads/product-images/" + product.getId() + "/image1.png");

                String imageUrl;
                if (productImage.exists()) {
                    imageUrl = baseUrl + imagePath;
                } else {
                    imageUrl = baseUrl + "/ProductImageServlet/null-image/404.webp";
                }

                productJson.addProperty("imageUrl", imageUrl);
                dealsArray.add(productJson);

            }

        }

        response.setContentType("application/json");
        JsonObject responseObject = new JsonObject();
        responseObject.add("latestListings", dealsArray);

        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(responseObject));

    }

}
