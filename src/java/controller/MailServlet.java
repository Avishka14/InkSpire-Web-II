package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.EmailDTO;
import hibernate.HibernateUtill;
import hibernate.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Mail;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "MailServlet", urlPatterns = {"/MailServlet"})
public class MailServlet extends HttpServlet {

    private static final String SUBJECT = "Your Verification Code";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        EmailDTO emailDto = gson.fromJson(reader, EmailDTO.class);

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);

        String email = emailDto.getEmail();

        Session s = HibernateUtill.getSessionFactory().openSession();

        Criteria criteria = s.createCriteria(User.class);

        Criterion crtion = Restrictions.eq("email", email);

        criteria.add(crtion);

        if (criteria.list().isEmpty()) {

            responseObject.addProperty("message", "E-Mail is Invalid or didn't Registered.");

        } else {

            User user = (User) criteria.list().get(0);

            try {

                String code = Util.generateCode();

                String htmlContent = "<div style='font-family:Arial,sans-serif; text-align:center; padding:20px;'>"
                        + "<h2 style='color:#0f1724;'>Your Verification Code</h2>"
                        + "<p style='font-size:22px; font-weight:bold; letter-spacing:4px; color:#111;'>"
                        + code
                        + "</p>"
                        + "<p style='font-size:13px; color:#555;'>This code will expire in 10 minutes.</p>"
                        + "</div>";

                boolean mailStatus = Mail.sendMail(user.getEmail(), SUBJECT, htmlContent);

                if (mailStatus) {

                    HttpSession session = request.getSession();
                    session.setAttribute("verificationCode", code);
                    session.setAttribute("userId", user.getId());

                    responseObject.addProperty("status", Boolean.TRUE);
                   
                }

            } catch (Exception e) {
                responseObject.addProperty("status", Boolean.FALSE);
                System.out.println(e.getMessage());
            }

        }
        
        s.close();
        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
