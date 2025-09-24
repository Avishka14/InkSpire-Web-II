
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.HibernateUtill;
import hibernate.Listing;
import hibernate.Offers;
import hibernate.Product;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadDailyDeals", urlPatterns = {"/LoadDailyDeals"})
public class LoadDailyDeals extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        Session session = HibernateUtill.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
       Criteria criteria = session.createCriteria(Offers.class , "o")
                .createAlias("o.offerType", "ot")
               .add(Restrictions.eq("ot.id", 3))
                .setMaxResults(12);
       
       List<Offers> offers = criteria.list();
        
       tx.commit();
       session.close();
       
        JsonArray dealsArray = new JsonArray();
        
        for(Offers offer : offers){
            
            Listing listing = offer.getListingId();
            Product product = listing.getProduct();
            Category category = product.getCategory();
            
            String price = String.valueOf(listing.getPrice());
            
            JsonObject productJson = new JsonObject();
            productJson.addProperty("productName", product.getTitle());
            productJson.addProperty("productCategory", category.getValue());
            productJson.addProperty("productPrice", price );
            productJson.addProperty("id", listing.getId() );
            productJson.addProperty("proId", product.getId() );
             
            String baseUrl = request.getScheme() + "://" 
                             +request.getServerName() + ":"
                            +request.getServerPort()
                           +request.getContextPath();
            
            String imagePath = "/ProductImageServlet/" + product.getId() + "/image1.png";
            File productImage = new File("C:/InkSpireUploads/product-images/" +product.getId() +"/image1.png");
            
            String imageUrl;
            if(productImage.exists()){
                imageUrl = baseUrl + imagePath;
            }else{
                imageUrl = baseUrl + "/ProductImageServlet/null-image/404.webp";
            }

            
            productJson.addProperty("imageUrl", imageUrl);
            dealsArray.add(productJson);
  
        }
 
        response.setContentType("application/json");
        JsonObject responseObject = new JsonObject();
        responseObject.add("dailyDeals", dealsArray);
        
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(responseObject));
                
       
        
    }

    
 
}
