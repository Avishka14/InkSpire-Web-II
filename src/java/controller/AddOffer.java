package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.ListingDTO;
import dto.OfferDTO;
import hibernate.HibernateUtill;
import hibernate.Listing;
import hibernate.OfferType;
import hibernate.Offers;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "AddOffer", urlPatterns = {"/AddOffer"})
public class AddOffer extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject data = gson.fromJson(request.getReader(), JsonObject.class);

        String newOffer = data.get("newOffer").getAsString();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (newOffer.isEmpty()) {
            responseObject.addProperty("message", "Please Enter Offer Name !");
        } else {

            Session session = HibernateUtill.getSessionFactory().openSession();

            Criteria c = session.createCriteria(OfferType.class);
            c.add(Restrictions.eq("value", newOffer));

            if (c.list().isEmpty()) {

                OfferType offer = new OfferType();
                offer.setValue(newOffer);
                session.save(offer);
                session.beginTransaction().commit();
                responseObject.addProperty("message", "Offer Added Success!");
                responseObject.addProperty("status", true);

            } else {
                responseObject.addProperty("message", "This Offer Already Exists!");
            }

            session.close();
        }
        String responseJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseJson);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = HibernateUtill.getSessionFactory().openSession();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);

        List<Offers> offerList = session.createCriteria(Offers.class).list();

        List<OfferDTO> offerDTOList = offerList.stream().map(offer -> {
            OfferDTO dto = new OfferDTO();
            dto.setId(offer.getId());
            dto.setOfferTypeName(offer.getOfferTypeId().getValue()); 
            

            List<ListingDTO> listingDTOs = new ArrayList<>();
            if (offer.getListingId() != null) { 
                Listing listing = offer.getListingId();
                ListingDTO lDto = new ListingDTO();
                lDto.setId(listing.getId());
                lDto.setPrice(listing.getPrice());
                lDto.setProductTitle(listing.getProduct().getTitle()); 
                listingDTOs.add(lDto);
            }

            dto.setListings(listingDTOs);
            return dto;
        }).collect(Collectors.toList());

        Gson gson = new Gson();
        responseObject.addProperty("status", Boolean.TRUE);
        responseObject.add("offerList", gson.toJsonTree(offerDTOList));

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);
        
        String id = request.getParameter("id");
        
        if(id == null){
             responseObject.addProperty("message", "Unexpected Error please try again later");
        }else{
            
            Session session = HibernateUtill.getSessionFactory().openSession();
            
            int offerId = Integer.parseInt(id);
            
            Offers offer = (Offers) session.get(Offers.class, offerId);
            
            session.delete(offer);
            session.beginTransaction().commit();
            responseObject.addProperty("status", Boolean.TRUE);
            responseObject.addProperty("message", "Offer Successfully Deleted");
      
            
        }
        
        Gson gson = new Gson();
        String responseJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseJson);
        
    
    }
    
    
    
    

}
