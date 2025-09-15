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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Avishka Chamod
 */
@WebServlet(name = "ReturnUserData", urlPatterns = {"/ReturnUserData"})
public class ReturnUserData extends HttpServlet {

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

            Criteria c = session.createCriteria(User.class);
            c.add(Restrictions.eq("id", id));

            if (c.list().isEmpty()) {
                responseObject.addProperty("message", "an Error Occured with your account Please try again later!");
            } else {

                User user = (User) c.list().get(0);
                JsonObject userData = new JsonObject();
                userData.addProperty("id", user.getId());
                userData.addProperty("firstName", user.getFirstName());
                userData.addProperty("lastName", user.getLastName());
                userData.addProperty("email", user.getEmail());
                userData.addProperty("regDate", user.getRegDate().toString());

                responseObject.addProperty("status", true);
                responseObject.add("user", userData);

            }

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }

}
