package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Util;
import hibernate.HibernateUtill;
import hibernate.User;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LogIn", urlPatterns = {"/LogIn"})
public class LogIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject logInData = gson.fromJson(request.getReader(), JsonObject.class);

        String email = logInData.get("email").getAsString();
        String password = logInData.get("password").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);

        if (email.equals("admin@gmail.com") && password.equals("12345")) {
            responseObject.addProperty("status", Boolean.TRUE);
            responseObject.addProperty("message", "600");

        } else {

            if (email.isEmpty()) {
                responseObject.addProperty("message", "E-Mail Can not be empty !");

            } else if (!Util.isEmailValid(email)) {
                responseObject.addProperty("message", "E-Mail is Invalid !");

            } else if (password.isEmpty()) {
                responseObject.addProperty("message", "Enter your Password !");

            } else {

                Session session = HibernateUtill.getSessionFactory().openSession();

                Criteria criteria = session.createCriteria(User.class);

                Criterion cr1 = Restrictions.eq("email", email);
                Criterion cr2 = Restrictions.eq("password", password);

                criteria.add(cr1);
                criteria.add(cr2);

                if (criteria.list().isEmpty()) {
                    responseObject.addProperty("message", "Invalid E-Mail or Password !");
                } else {

                    User user = (User) criteria.list().get(0);
                    HttpSession httpSession = request.getSession();
                    responseObject.addProperty("status", Boolean.TRUE);

                    Cookie userIdCookie = new Cookie("userId", String.valueOf(user.getId()));
                    userIdCookie.setMaxAge(7 * 24 * 60 * 60); 
                    userIdCookie.setPath("/");
                    response.addCookie(userIdCookie);
                    
                    httpSession.setAttribute("user", user);
                    responseObject.addProperty("message", "200");

                    session.close();

                }

            }

        }

        String responseJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseJson);

    }

}
