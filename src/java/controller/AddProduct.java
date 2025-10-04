package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Avilability;
import hibernate.Category;
import hibernate.Condition;
import hibernate.HibernateUtill;
import hibernate.Listing;
import hibernate.ListingApproval;
import hibernate.Product;
import hibernate.Seller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

@MultipartConfig
@WebServlet(name = "AddProduct", urlPatterns = {"/AddProduct"})
public class AddProduct extends HttpServlet {

    private static final String UPLOAD_DIR = "C:/InkSpireUploads/product-images/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();

        response.setContentType("application/json");

        Session session = null;
        Transaction tx = null;

        try {
            String productId = request.getParameter("productId");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String categoryId = request.getParameter("categoryId");
            String conditionId = request.getParameter("conditionId");
            String itemAvailabilityId = request.getParameter("itemAvailabilityId");
            String price = request.getParameter("price");
            String sellerId = request.getParameter("sellerId");
            String approvalId = request.getParameter("approvalId");
            Part part1 = request.getPart("image1");
            

            if (productId == null || title == null || categoryId == null || conditionId == null
                    || itemAvailabilityId == null || price == null || sellerId == null || approvalId == null
                    || part1 == null || part1.getSize() == 0) {
                responseObject.addProperty("status", false);
                responseObject.addProperty("message", "Missing required parameters or product image.");
                response.getWriter().write(gson.toJson(responseObject));
                return;
            }


            session = HibernateUtill.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Category category = (Category) session.get(Category.class, Integer.parseInt(categoryId));
            Condition condition = (Condition) session.get(Condition.class, Integer.parseInt(conditionId));
            Avilability avilability = (Avilability) session.get(Avilability.class, Integer.parseInt(itemAvailabilityId));
            Seller seller = (Seller) session.get(Seller.class, Integer.parseInt(sellerId));
            ListingApproval approval = (ListingApproval) session.get(ListingApproval.class, Integer.parseInt(approvalId));

            if (category == null || condition == null || avilability == null || seller == null || approval == null) {
                responseObject.addProperty("status", false);
                responseObject.addProperty("message", "Invalid reference IDs provided.");
                response.getWriter().write(gson.toJson(responseObject));
                return;
            }

            Product product = new Product();
            product.setId(Integer.parseInt(productId));
            product.setTitle(title);
            product.setDescription(description);
            product.setCategory(category);
            product.setAvailability(avilability);
            product.setCondition(condition);
            session.save(product);

            Listing listing = new Listing();
            listing.setListingId(approval);
            listing.setListing_date(Timestamp.valueOf(LocalDateTime.now()));
            listing.setPrice(Double.parseDouble(price));
            listing.setProduct(product);
            listing.setSeller(seller);
            session.save(listing);


            File productFolder = new File(UPLOAD_DIR, productId);
            if (!productFolder.exists()) {
                productFolder.mkdirs();
            }

            File imageFile = new File(productFolder, "image1.png");
            try {
                savePartToFile(part1, imageFile);
            } catch (IOException ioEx) {
                if (tx != null) tx.rollback(); 
                responseObject.addProperty("status", false);
                responseObject.addProperty("message", "Failed to save product image.");
                response.getWriter().write(gson.toJson(responseObject));
                return;
            }


            tx.commit();

            responseObject.addProperty("status", true);
            responseObject.addProperty("message", "Product added successfully.");
            response.getWriter().write(gson.toJson(responseObject));

        } catch (NumberFormatException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "Invalid number format in request parameters.");
            response.getWriter().write(new Gson().toJson(responseObject));
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "Database error while saving product.");
            response.getWriter().write(new Gson().toJson(responseObject));
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "Unexpected error: " + e.getMessage());
            response.getWriter().write(new Gson().toJson(responseObject));
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    private void savePartToFile(Part part, File file) throws IOException {
        if (part != null && part.getSize() > 0) {
            Files.copy(part.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
