package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadSellerListings", urlPatterns = {"/LoadSellerListings"})
public class LoadSellerListings extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer sellerId = Integer.parseInt(request.getParameter("id"));
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);

        Session session = HibernateUtill.getSessionFactory().openSession();

        try {

            Criteria c = session.createCriteria(Listing.class, "l")
                    .createAlias("l.seller", "s")
                    .add(Restrictions.eq("s.id", sellerId))
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Listing> listingList = c.list();

            JsonArray listingArray = new JsonArray();

            for (Listing listing : listingList) {

                Product product = listing.getProduct();

                JsonObject productJson = new JsonObject();
                productJson.addProperty("id", product.getId());
                productJson.addProperty("title", product.getTitle());
                productJson.addProperty("desc", product.getDescription());
                productJson.addProperty("price", listing.getPrice());
                productJson.addProperty("listingId", listing.getId());
                productJson.addProperty("status", listing.getListingId().getValue());
                productJson.addProperty("availability", product.getAvailability().getValue());
                productJson.addProperty("availabilityId", product.getAvailability().getId());

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
                listingArray.add(productJson);

            }

            responseObject.add("listingList", listingArray);
            responseObject.addProperty("status", Boolean.TRUE);

        } catch (Exception e) {
            e.printStackTrace();
            responseObject.addProperty("message", "An error occured " + e);
        }
          
        session.close();
        
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(responseObject));

    }

}
