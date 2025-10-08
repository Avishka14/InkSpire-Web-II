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
import org.hibernate.Transaction;

@WebServlet(name = "LoadListingApprovalDataAdmin", urlPatterns = {"/LoadListingApprovalDataAdmin"})
public class LoadListingApprovalDataAdmin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session session = HibernateUtill.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {

            Criteria criteria = session.createCriteria(Listing.class);
            List<Listing> listings = criteria.list();

            tx.commit();
            session.close();

            JsonArray listingArray = new JsonArray();

            for (Listing list : listings) {

                if (list.getListingId().getId() == 2) {
                    JsonObject listingObject = new JsonObject();
                    Product product = list.getProduct();

                    listingObject.addProperty("listingId", list.getId());
                    listingObject.addProperty("price", list.getPrice());
                    listingObject.addProperty("title", product.getTitle());
                    listingObject.addProperty("desc", product.getDescription());
                    listingObject.addProperty("category", product.getCategory().getValue());
                    listingObject.addProperty("condition", product.getCondition().getValue());
                    listingObject.addProperty("productId", product.getId());
                    listingObject.addProperty("seller", list.getSeller().getSellername());

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

                    listingObject.addProperty("imageUrl", imageUrl);
                    listingArray.add(listingObject);

                }

            }

            response.setContentType("application/json");
            JsonObject responseObject = new JsonObject();
            responseObject.add("listingArray", listingArray);

            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(responseObject));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
