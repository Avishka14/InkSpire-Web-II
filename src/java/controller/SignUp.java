package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject signUpData = gson.fromJson(request.getReader(), JsonObject.class);

        String fname = signUpData.get("fname").getAsString();
        String lname = signUpData.get("lname").getAsString();
        String email = signUpData.get("email").getAsString();
        String password = signUpData.get("password").getAsString();
      
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        if(fname.isEmpty()){
            responseObject.addProperty("message", "Please Enter Your First Name!");
        }else if(lname.isEmpty()){
             responseObject.addProperty("message", "Please Enter Your Last Name!");
        }else if(email.isEmpty()){
             responseObject.addProperty("message", "Please Enter Your E-Mail!");
        }else if(!model.Util.isEmailValid(email)){
             responseObject.addProperty("message", "Please Enter Valid E-Mail!");
        }else if(password.isEmpty() ){
            responseObject.addProperty("message", "Please Enter Your Password!");
        }else if(!model.Util.isPasswordValid(password)){
             responseObject.addProperty("message", "Password Must Include More than ,\n 8 Character Long and Uppercase , Lowercase ,\n Digit and a Special Character!");
        }else{
            
            
        }
        
        String responseJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseJson);

    }

}
