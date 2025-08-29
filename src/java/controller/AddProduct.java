
package controller;

import com.google.gson.JsonObject;
import hibernate.Avilability;
import hibernate.Category;
import hibernate.Condition;
import hibernate.HibernateUtill;
import hibernate.Listing;
import hibernate.ListingApproval;
import hibernate.Product;
import hibernate.Seller;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@MultipartConfig
@WebServlet(name = "AddProduct", urlPatterns = {"/AddProduct"})
public class AddProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String productId = request.getParameter("productId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String categoryId = request.getParameter("categoryId");
        String conditionId = request.getParameter("conditionId");
        String itemAvailabilityId = request.getParameter("itemAvailabilityId");
        String price = request.getParameter("price");
        String sellerId = request.getParameter("sellerId");
        String approvalId = request.getParameter("approvalId");

        LocalDateTime localDate = LocalDateTime.now();
        
       
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        Session session = HibernateUtill.getSessionFactory().openSession();
        Transaction tx = null;
      
        try {
            
            tx = session.beginTransaction();
         
         Category category = (Category) session.get(Category.class, Integer.parseInt(categoryId));
        Condition condition = (Condition) session.get(Condition.class, Integer.parseInt(conditionId));
        Avilability avilability = (Avilability) session.get(Avilability.class, Integer.parseInt(itemAvailabilityId));
        Seller seller = (Seller) session.get(Seller.class, Integer.parseInt(sellerId));
        ListingApproval approval = (ListingApproval) session.get(ListingApproval.class, Integer.parseInt(approvalId));
        
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
        listing.setListing_date(Timestamp.valueOf(localDate));
        listing.setPrice(Double.parseDouble(price));
        listing.setProduct(product);
        listing.setSeller(seller);
        session.save(listing);
    
        tx.commit();
      
            
        }catch(Exception e){
            e.printStackTrace();
        } 
     

    
    }

    
  
}
