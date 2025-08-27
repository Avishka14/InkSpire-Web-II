package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.EmailDTO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Mail;
import model.Util;

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
        
        try {

            String code = Util.generateCode();

            String htmlContent = "<h2>Your Verification Code</h2>"
                    + "<p style='font-size:20px; font-weight:bold;'>" + code + "</p>";

            Mail.sendMail(emailDto.getEmail(), SUBJECT, htmlContent);
            
            responseObject.addProperty("status", Boolean.TRUE);
              String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
            
        } catch (Exception e) {
            responseObject.addProperty("status", Boolean.FALSE);
            System.out.println(e.getMessage());
        }

    }

}
