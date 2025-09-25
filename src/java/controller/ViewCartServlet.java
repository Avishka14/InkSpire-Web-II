package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewCartServlet", urlPatterns = {"/ViewCartServlet"})
public class ViewCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<AddToCart.cartItem> cart = (List<AddToCart.cartItem>) session.getAttribute("cart");

        JsonArray cartArray = new JsonArray();
        if (cart != null) {
            for (AddToCart.cartItem c : cart) {
                JsonObject cartJson = new JsonObject();
                cartJson.addProperty("title", c.getTitle());
                cartJson.addProperty("image", c.getImage());
                cartJson.addProperty("price", c.getPrice());
                cartJson.addProperty("productId", c.getProductId());
                cartArray.add(cartJson);
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(cartArray));
    }
}
