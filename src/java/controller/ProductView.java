package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
import hibernate.Listing;
import hibernate.Product;
import hibernate.Seller;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "ProductView", urlPatterns = {"/ProductView"})
public class ProductView extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer id = Integer.valueOf(request.getParameter("id"));
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);
        Gson gson = new Gson();

        if (id != null) {

            Session session = HibernateUtill.getSessionFactory().openSession();

            Criteria c = session.createCriteria(Listing.class);
            c.add(Restrictions.eq("id", id));

            if (c.list().isEmpty()) {
                responseObject.addProperty("message", "Listing Not Found");
            } else {

                Listing listing = (Listing) c.list().get(0);

                Seller seller = listing.getSeller();
                Product product = listing.getProduct();

                if (product == null || seller == null) {
                    responseObject.addProperty("message", "Product Not Found!");
                } else {

                    JsonObject productData = new JsonObject();
                    productData.addProperty("id", product.getId());
                    productData.addProperty("title", product.getTitle());
                    productData.addProperty("desc", product.getDescription());
                    productData.addProperty("seller", seller.getSellername());
                    productData.addProperty("category", product.getCategory().getValue());
                    productData.addProperty("categoryId", product.getCategory().getId());
                    productData.addProperty("condition", product.getCondition().getValue());
                    productData.addProperty("conditionId", product.getCondition().getId());
                    productData.addProperty("price", listing.getPrice());
                    productData.addProperty("listingId", listing.getId());

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

                    productData.addProperty("imageUrl", imageUrl);

                    responseObject.addProperty("status", true);
                    responseObject.add("product", productData);

                }
                session.close();
            }

        } else {
            responseObject.addProperty("message", "Error Fetching Products!!");
        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
