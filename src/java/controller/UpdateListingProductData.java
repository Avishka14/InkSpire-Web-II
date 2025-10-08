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
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
@WebServlet(name = "UpdateListingProductData", urlPatterns = {"/UpdateListingProductData"})
public class UpdateListingProductData extends HttpServlet {

    private static final String UPLOAD_DIR = "C:/InkSpireUploads/product-images/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();

        response.setContentType("application/json");

        Session session = null;
        Transaction tx = null;

        try {

            String listingId = request.getParameter("listingId");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String categoryId = request.getParameter("categoryId");
            String conditionId = request.getParameter("conditionId");
            String itemAvailabilityId = request.getParameter("itemAvailabilityId");
            String price = request.getParameter("price");
            String sellerId = request.getParameter("sellerId");
            String approvalId = request.getParameter("approvalId");
            Part part1 = request.getPart("image1");

            System.out.println(sellerId);

            if (listingId == null || title == null || categoryId == null || conditionId == null
                    || itemAvailabilityId == null || price == null || sellerId == null || approvalId == null) {
                responseObject.addProperty("status", false);
                responseObject.addProperty("message", "Missing required parameters");
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

            Listing listing = (Listing) session.get(Listing.class, Integer.parseInt(listingId));
            Product product = listing.getProduct();

            listing.setPrice(Double.parseDouble(price));
            listing.setListingId(approval);

            product.setTitle(title);
            product.setAvailability(avilability);
            product.setCategory(category);
            product.setCondition(condition);
            product.setDescription(description);

            Integer productId = product.getId();

            if (part1 != null) {

                File productFolder = new File(UPLOAD_DIR, productId.toString());

                if (!productFolder.exists() || !productFolder.isDirectory()) {
                    responseObject.addProperty("status", false);
                    responseObject.addProperty("message", "Product folder not found for update.");
                    response.getWriter().write(gson.toJson(responseObject));
                    return;
                }

                File imageFile = new File(productFolder, "image1.png");

                try {

                    if (imageFile.exists()) {
                        imageFile.delete();
                    }

                    savePartToFile(part1, imageFile);

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Product image updated successfully.");

                } catch (IOException ioEx) {
                    if (tx != null) {
                        tx.rollback();
                    }
                    responseObject.addProperty("status", false);
                    responseObject.addProperty("message", "Failed to update product image.");
                }

            }

            session.update(listing);
            session.update(product);
            tx.commit();

            responseObject.addProperty("status", true);
            responseObject.addProperty("message", "Product added successfully.");
            response.getWriter().write(gson.toJson(responseObject));

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
