
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
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


@WebServlet(name = "UpdateUserMobile", urlPatterns = {"/UpdateUserMobile"})
public class UpdateUserMobile extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         Gson gson = new Gson();
          JsonObject updateName = gson.fromJson(request.getReader(), JsonObject.class);
          
          String contact = updateName.get("contact").getAsString();
          
         JsonObject responseObject = new JsonObject();
         responseObject.addProperty("status", false);
         
           if (contact.isEmpty()) {
            responseObject.addProperty("message", "Enter your Mobile Number!");

        } else if (!Util.isMobileValid(contact)) {
            responseObject.addProperty("message", "Please Enter a Valid Mobile Number!");
   
        }else{
 
           Integer id = Integer.valueOf(request.getParameter("id"));
  
                if (id != null) {

                    try {

                        Session hibernateSession = HibernateUtill.getSessionFactory().openSession();
                        Transaction transaction = null;
                        transaction = hibernateSession.beginTransaction();

                        User user = (User) hibernateSession.get(User.class, id);

                        user.setContact(contact);
                        hibernateSession.update(user);
                        transaction.commit();

                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Mobile updated successfully!");
                        hibernateSession.close();

                    } catch (Exception e) {

                        e.printStackTrace();
                        responseObject.addProperty("message", "Error updating Mobile");
                    }

                } 

        }
           
           
        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    
    
    }

    
 
    
}
