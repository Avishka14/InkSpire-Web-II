package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.City;
import hibernate.HibernateUtill;
import hibernate.ShippingAddress;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

@WebServlet(name = "AddUserAddress", urlPatterns = {"/AddUserAddress"})
public class AddUserAddress extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject data = gson.fromJson(request.getReader(), JsonObject.class);

        String line1 = data.get("line1").getAsString();
        String line2 = data.get("line2").getAsString();
        Integer city = data.get("city").getAsInt();
        String postal = data.get("postal").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (line1.isEmpty()) {
            responseObject.addProperty("message", "Enter your Street (Address Line 01)!");

        } else if (line2.isEmpty()) {
            responseObject.addProperty("message", "Enter yourAddress Line 02!");

        } else if (city == null) {
            responseObject.addProperty("message", "Select your City!");

        } else if (postal.isEmpty()) {
            responseObject.addProperty("message", "Enter Your Postal Code!");

        } else {

            Integer id = Integer.valueOf(request.getParameter("id"));

                try {

                    Session session = HibernateUtill.getSessionFactory().openSession();
                    Transaction transaction = null;
                    transaction = session.beginTransaction();

                    ShippingAddress address = new ShippingAddress();
                    
                    User user = (User) session.get(User.class, id);

                    City cityid = (City) session.get(City.class, city);

                    address.setLine1(line1);
                    address.setLine2(line2);
                    address.setPostalCode(postal);
                    address.setUser(user);
                    address.setCity(cityid);
                    session.save(address);
                    transaction.commit();

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Address successfully Added!");
                    session.close();

                } catch (Exception e) {

                    e.printStackTrace();
                    responseObject.addProperty("message", "Error Adding Address");
                }


        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
