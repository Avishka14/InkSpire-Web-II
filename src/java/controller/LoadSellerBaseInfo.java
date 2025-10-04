package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
import hibernate.Seller;
import hibernate.ShippingAddress;
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

@WebServlet(name = "LoadSellerBaseInfo", urlPatterns = {"/LoadSellerBaseInfo"})
public class LoadSellerBaseInfo extends HttpServlet {

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

            Criteria c = session.createCriteria(Seller.class);
            c.add(Restrictions.eq("user.id", id));

            if (c.list().isEmpty()) {
                responseObject.addProperty("message", "Activate a Seller Profile");
            } else {

                Seller seller = (Seller) c.list().get(0);
                JsonObject sellerData = new JsonObject();
                sellerData.addProperty("sellerName", seller.getSellername());
                sellerData.addProperty("regDate", seller.getReg_date().toString());
                responseObject.addProperty("status", true);
                responseObject.add("seller", sellerData);
                
                Cookie userIdCookie = new Cookie("sellerId", String.valueOf(seller.getId()));
                    userIdCookie.setMaxAge(7 * 24 * 60 * 60); 
                    userIdCookie.setPath("/");
                    response.addCookie(userIdCookie);

            }
            session.close();

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
