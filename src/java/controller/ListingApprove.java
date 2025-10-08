package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
import hibernate.Listing;
import hibernate.ListingApproval;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "ListingApprove", urlPatterns = {"/ListingApprove"})
public class ListingApprove extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer listingId = Integer.valueOf(request.getParameter("id"));
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);
        Gson gson = new Gson();

        try {

            Session session = HibernateUtill.getSessionFactory().openSession();

            Criteria c = session.createCriteria(Listing.class);
            c.add(Restrictions.eq("id", listingId));
            
            if(c.list().isEmpty()){
               responseObject.addProperty("message", "Listing is Not Found");
          
            }else{
                
                Listing listing = (Listing) c.list().get(0);
                ListingApproval approval = new ListingApproval();
                approval.setId(1);
               
                listing.setListingId(approval);
                session.update(listing);
                session.beginTransaction().commit();
                session.close();
                responseObject.addProperty("status", true);
                
            }
            

        } catch (Exception e) {
            e.printStackTrace();
            responseObject.addProperty("message", "Exception :" + e);
        }
        
         String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
