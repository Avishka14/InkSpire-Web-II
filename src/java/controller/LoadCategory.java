
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.HibernateUtill;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadCategory", urlPatterns = {"/LoadCategory"})
public class LoadCategory extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session session = HibernateUtill.getSessionFactory().openSession();
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);
        
        Criteria c = session.createCriteria(Category.class);
        List<Category> categoryList = c.list();
        
        session.close();
        
        Gson gson = new Gson();
        responseObject.add("categoryList", gson.toJsonTree(categoryList));
        
        responseObject.addProperty("status", Boolean.TRUE);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
         Gson gson = new Gson();
         JsonObject data = gson.fromJson(request.getReader(), JsonObject.class);
         String newCategory = data.get("newCategory").getAsString();
         
         JsonObject responseObject = new JsonObject();
         responseObject.addProperty("status", Boolean.FALSE);
         
         if(newCategory.isEmpty()){
              responseObject.addProperty("message", "Please insert the Category name");
         }else{
             
             Session session = HibernateUtill.getSessionFactory().openSession();
             
             Criteria c = session.createCriteria(Category.class);
             c.add(Restrictions.eq("value", newCategory));
             
             if(c.list().isEmpty()){
                 
                 Category category = new Category();
                 category.setValue(newCategory);
                 session.save(category);
                 session.beginTransaction().commit();           
                 responseObject.addProperty("status", true);
                 
             }else{
                 responseObject.addProperty("message", "The Category Already Extists!");
             }
             
             session.close();
         }
        
        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
        
    }
    
    

   

}
