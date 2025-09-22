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

@WebServlet(name = "UpdateUserPassword", urlPatterns = {"/UpdateUserPassword"})
public class UpdateUserPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject updateName = gson.fromJson(request.getReader(), JsonObject.class);

        String oldPass = updateName.get("oldPass").getAsString();
        String newPas = updateName.get("newPas").getAsString();
        String conPass = updateName.get("conPass").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (oldPass.isEmpty()) {
            responseObject.addProperty("message", "Enter your Current Password!");

        } else if (newPas.isEmpty()) {
            responseObject.addProperty("message", "Enter your New Password!");

        } else if (!Util.isPasswordValid(newPas)) {
            responseObject.addProperty("message", "Password Must Include More than ,\n 8 Character Long and Uppercase , Lowercase ,\n Digit and a Special Character!");

        } else if (conPass.isEmpty()) {
            responseObject.addProperty("message", "Confim your New Password!");

        } else if (!conPass.equals(newPas)) {
            responseObject.addProperty("message", "Password Doesn't Match!");

        } else {

            Integer id = Integer.valueOf(request.getParameter("id"));

            if (id != null) {

                try {
                    Session hibernateSession = HibernateUtill.getSessionFactory().openSession();
                    Transaction transaction = null;
                    transaction = hibernateSession.beginTransaction();

                    User user = (User) hibernateSession.get(User.class, id);

                    if (user.getPassword().equals(oldPass)) {

                        user.setPassword(conPass);
                        hibernateSession.update(user);
                        transaction.commit();

                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Password Successfully Updated!");
                        hibernateSession.close();

                    } else {
                        responseObject.addProperty("message", "Incorrect Password!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    responseObject.addProperty("message", "Error updating Pasword" + e);
                }

            }

        }
        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
