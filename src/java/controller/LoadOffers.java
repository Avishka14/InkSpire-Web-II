
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.Condition;
import hibernate.HibernateUtill;
import hibernate.Listing;
import hibernate.OfferType;
import hibernate.Offers;
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


@WebServlet(name = "LoadOffers", urlPatterns = {"/LoadOffers"})
public class LoadOffers extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session session = HibernateUtill.getSessionFactory().openSession();
        JsonObject responseObject = new JsonObject();
        
        responseObject.addProperty("status", Boolean.FALSE);
        
        Criteria catCriteria = session.createCriteria(OfferType.class);
        List<OfferType> offerTypeList = catCriteria.list();
        
       
        session.close();
        
        Gson gson = new Gson();
        
        responseObject.add("offerTypeList", gson.toJsonTree(offerTypeList));


        responseObject.addProperty("status", Boolean.TRUE);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        Gson gson = new Gson();
        JsonObject data = gson.fromJson(request.getReader(), JsonObject.class);
        String newOfferProduct = data.get("offerProduct").getAsString();
        String offerId = data.get("offerId").getAsString();
    
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);
       

        if(newOfferProduct.isEmpty()){
            responseObject.addProperty("message", "Please Enter Product ID!");
        }else if(offerId.isEmpty()){
            responseObject.addProperty("message", "Please Select the Offer!");
        }else{
            
            int listingId = Integer.parseInt(newOfferProduct);
            
            Session session = HibernateUtill.getSessionFactory().openSession();
            
            Criteria criteria = session.createCriteria(Listing.class);
            criteria.add(Restrictions.eq("id" ,listingId ));
            
            if(criteria.list().isEmpty()){
                   responseObject.addProperty("message", "Invalid Listing ID!");
             
            }else{
               
                Offers offers = new Offers();
               
                OfferType offerType = new OfferType();
                offerType.setId(Integer.parseInt(offerId));
                
                Listing listing = new Listing();
                listing.setId(listingId);
                
                offers.setListingId(listing);
                offers.setOfferTypeId(offerType);
                        
                session.save(offers);
                session.beginTransaction().commit();
                responseObject.addProperty("status", true);
                session.close();
                
            }
            
            
        }
        
        String responseJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseJson);
    
    }

    
   

    
      
}
