package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
import hibernate.Seller;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

@WebServlet(name = "UpdateSellerName", urlPatterns = {"/UpdateSellerName"})
public class UpdateSellerName extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject updateSeller = gson.fromJson(request.getReader(), JsonObject.class);

        String sellerName = updateSeller.get("sellerName").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (sellerName.isEmpty()) {
            responseObject.addProperty("message", "Enter your Seller Name!");
        } else if (!Util.isString(sellerName)) {
            responseObject.addProperty("message", "Invalid Seller Name!");
        } else {
            
                       Integer userId = Integer.valueOf(request.getParameter("id"));
  
                if (userId != null) {

                    try {

                        Session hibernateSession = HibernateUtill.getSessionFactory().openSession();
                        Transaction transaction = null;
                        transaction = hibernateSession.beginTransaction();

                User user =(User) hibernateSession.get(User.class, userId);

                if (user != null) {

                    Seller seller = user.getSeller(); 

                    if (seller != null) {
                        seller.setSellername(sellerName);
                        hibernateSession.update(seller);
                        transaction.commit();

                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Seller name updated successfully!");
                    } else {
                        responseObject.addProperty("message", "Seller not found for this User!");
                    }
                } else {
                    responseObject.addProperty("message", "User not found!");
                }

                        hibernateSession.close();

                    } catch (Exception e) {

                        e.printStackTrace();
                        responseObject.addProperty("message", "Error updating Seller Name");
                    }

                } 

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
