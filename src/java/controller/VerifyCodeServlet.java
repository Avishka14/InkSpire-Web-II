
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "VerifyCodeServlet", urlPatterns = {"/VerifyCodeServlet"})
public class VerifyCodeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        HttpSession session = request.getSession();
        
        if(session == null){
            response.getWriter().write("Session expired. Please Try Again.");
            return;
        }
        
        String storedCode = (String) session.getAttribute("verificationCode");
        String userCode = request.getParameter("userCode");
        
          if (storedCode != null && storedCode.equals(userCode)) {
            response.getWriter().write("Verification successful!");
            session.removeAttribute("verificationCode");
      
          } else {
            response.getWriter().write("Invalid code. Try again.");
        }
    
    }

    
}
