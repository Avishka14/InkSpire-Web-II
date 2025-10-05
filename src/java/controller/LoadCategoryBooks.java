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
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadCategoryBooks", urlPatterns = {"/LoadCategoryBooks"})
public class LoadCategoryBooks extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoryIdParam = request.getParameter("categoryId");
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);

        if (categoryIdParam == null || categoryIdParam.isEmpty()) {
            responseObject.addProperty("message", "Error occurred in Product Loading");
        } else {
            Session session = HibernateUtill.getSessionFactory().openSession();
            try {
                int categoryId = Integer.parseInt(categoryIdParam);

                Criteria c = session.createCriteria(Listing.class, "l")
                        .createAlias("l.product", "p")
                        .createAlias("p.category", "cat")
                        .add(Restrictions.eq("cat.id", categoryId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

                List<Listing> listingList = c.list();

                JsonArray productArray = new JsonArray();

                for (Listing listing : listingList) {
                    Product product = listing.getProduct();

                    JsonObject productJson = new JsonObject();
                    productJson.addProperty("id", product.getId());
                    productJson.addProperty("title", product.getTitle());
                    productJson.addProperty("description", product.getDescription());
                    productJson.addProperty("price", listing.getPrice()); 
                    productJson.addProperty("category", product.getCategory().getValue());
                    productJson.addProperty("listingId", listing.getId());

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
                    productArray.add(productJson);
                }

                responseObject.add("productList", productArray);
                responseObject.addProperty("status", Boolean.TRUE);

            } catch (NumberFormatException e) {
                responseObject.addProperty("message", "Invalid Category ID");
            } finally {
                session.close();
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(responseObject));
    
    
    }
   
    
    
    
}

