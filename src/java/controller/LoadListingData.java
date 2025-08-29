
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.Condition;
import hibernate.HibernateUtill;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;


@WebServlet(name = "LoadListingData", urlPatterns = {"/LoadListingData"})
public class LoadListingData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session session = HibernateUtill.getSessionFactory().openSession();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);
        
        Criteria catCriteria = session.createCriteria(Category.class);
        List<Category> categoryList = catCriteria.list();
        
        Criteria condCriteria = session.createCriteria(Condition.class);
        List<Condition> conditionList = condCriteria.list();
       
        session.close();
        
        Gson gson = new Gson();
        
        responseObject.add("categoryList", gson.toJsonTree(categoryList));
        responseObject.add("conditionList", gson.toJsonTree(conditionList));
      
        responseObject.addProperty("status", Boolean.TRUE);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
        
    
    }
 
    
    
}
