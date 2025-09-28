package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dto.CheckoutDataDTO;
import dto.CheckoutItemDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CheckOut", urlPatterns = {"/CheckOut"})
public class CheckOut extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject requestBody = gson.fromJson(request.getReader(), JsonObject.class);
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);
                
        String userId = requestBody.get("user").getAsString();
        double subtotal = requestBody.get("subTotal").getAsDouble();
        double shipping = requestBody.get("shipping").getAsDouble();
        double total = requestBody.get("total").getAsDouble();

        JsonArray products = requestBody.getAsJsonArray("products");
        List<CheckoutItemDTO> productList = new ArrayList<>();
        
        for (JsonElement element : products) {
            JsonObject product = element.getAsJsonObject();

            int listingId = product.get("listingId").getAsInt();
            int productId = product.get("productId").getAsInt();
            String title = product.get("title").getAsString();
            double price = product.get("price").getAsDouble();
            String image = product.get("image").getAsString();
            productList.add(new CheckoutItemDTO(listingId , productId ,  title , price , image));
        }
        
        List<CheckoutDataDTO> checkoutData = new ArrayList<>();
        checkoutData.add(new CheckoutDataDTO(userId, subtotal, shipping, total, productList));

        HttpSession session = request.getSession();
        session.setAttribute("checkoutgoods", productList);
        session.setAttribute("checkoutuser", checkoutData);
        
 
        responseObject.addProperty("status", Boolean.TRUE);
        response.setContentType("application/json");
        response.getWriter().write(responseObject.toString());
        
    }

}
