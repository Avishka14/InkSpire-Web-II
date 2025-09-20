package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
import hibernate.Seller;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "VerifySellerAccount", urlPatterns = {"/VerifySellerAccount"})
public class VerifySellerAccount extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject setupAc = gson.fromJson(request.getReader(), JsonObject.class);

        String username = setupAc.get("username").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (username.isEmpty()) {
            responseObject.addProperty("message", "Enter your Seller Name!");
        } else if (!Util.isString(username)) {
            responseObject.addProperty("message", "Invalid Seller Name. Use Only Letters!");
        } else {

            Integer id = Integer.valueOf(request.getParameter("id"));

            if (id != null) {

                try {

                    Session hibernateSession = HibernateUtill.getSessionFactory().openSession();
                    Transaction transaction = null;
                    transaction = hibernateSession.beginTransaction();

                    User user = (User) hibernateSession.get(User.class, id);

                    Seller seller = new Seller();
                    seller.setReg_date(new Date());
                    seller.setSellername(username);
                    seller.setUser(user);
                    seller.setVerificationCode(1);

                    hibernateSession.save(seller);
                    transaction.commit();

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Seller Account Acivated Successfully!");
                    hibernateSession.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    responseObject.addProperty("message", "an Error Occured Please Log In Again!");
                }

            } else {
                responseObject.addProperty("message", "an Error Occured Please Log In Again!");
            }

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
        
    }

}
