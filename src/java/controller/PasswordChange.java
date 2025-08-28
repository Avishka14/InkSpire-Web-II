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
import javax.servlet.http.HttpSession;
import model.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

@WebServlet(name = "PasswordChange", urlPatterns = {"/PasswordChange"})
public class PasswordChange extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject passwordChange = gson.fromJson(request.getReader(), JsonObject.class);

        String newPassword = passwordChange.get("newPassword").getAsString();
        String confirmPassword = passwordChange.get("confirmPassword").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (!Util.isPasswordValid(newPassword)) {
            responseObject.addProperty("message", "Password Must Include More than ,\n 8 Character Long and Uppercase , Lowercase ,\n Digit and a Special Character!");

        } else if (!confirmPassword.matches(newPassword)) {
            responseObject.addProperty("message", "Password not maching");

        } else {

            HttpSession session = request.getSession(false);
            if (session != null) {
                Integer userId = (Integer) session.getAttribute("userId");

                if (userId != null) {

                    try {

                        Session hibernateSession = HibernateUtill.getSessionFactory().openSession();
                        Transaction transaction = null;
                        transaction = hibernateSession.beginTransaction();

                        User user = (User) hibernateSession.get(User.class, userId);

                        user.setPassword(confirmPassword);
                        hibernateSession.update(user);
                        transaction.commit();

                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Password updated successfully!");

                    } catch (Exception e) {

                        e.printStackTrace();
                        responseObject.addProperty("message", "Error updating password");
                    }

                } else {
                    responseObject.addProperty("message", "An unexpected error occurred while processing your request.");
                }

            }

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
