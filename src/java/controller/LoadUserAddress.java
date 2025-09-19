
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
import hibernate.ShippingAddress;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "LoadUserAddress", urlPatterns = {"/LoadUserAddress"})
public class LoadUserAddress extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   
        Integer id = Integer.valueOf(request.getParameter("id"));
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);
        Gson gson = new Gson();

        if (id == null) {
            responseObject.addProperty("message", "Please log In once Again!");
        } else {

            Session session = HibernateUtill.getSessionFactory().openSession();

            Criteria c = session.createCriteria(ShippingAddress.class);
            c.add(Restrictions.eq("user.id", id));

            if (c.list().isEmpty()) {
                responseObject.addProperty("message", "Address is Not Found");
            } else {

                ShippingAddress address = (ShippingAddress) c.list().get(0);
                JsonObject addressData = new JsonObject();
                addressData.addProperty("line1", address.getLine1());
                addressData.addProperty("line2", address.getLine2());
                addressData.addProperty("city", address.getCity().getName());
                addressData.addProperty("cityId", address.getCity().getId());
                addressData.addProperty("postal", address.getPostalCode());
                
                   Cookie addressId = new Cookie("shippingAdId", String.valueOf(address.getId()));
                    addressId.setMaxAge(7 * 24 * 60 * 60); 
                    addressId.setPath("/");
                    response.addCookie(addressId);

                responseObject.addProperty("status", true);
                responseObject.add("address", addressData);

            }
          session.close();
        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    
    }

    

}
