package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "RemoveFromCart", value = "/RemoveFromCart")
public class RemoveFromCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject responseObject = new JsonObject();
        Gson gson = new Gson();

        JsonObject requestBody = gson.fromJson(request.getReader(), JsonObject.class);

        if (!requestBody.has("productId")) {
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "Product ID is required!");
            response.getWriter().write(responseObject.toString());
            return;
        }

        int productId = requestBody.get("productId").getAsInt();

        HttpSession session = request.getSession();
        List<AddToCart.cartItem> cart = (List<AddToCart.cartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "Cart is empty!");
            response.getWriter().write(responseObject.toString());
            return;
        }

        cart = cart.stream()
                .filter(item -> item.getProductId() != productId)
                .collect(Collectors.toList());

        session.setAttribute("cart", cart);

        JsonArray cartArray = new JsonArray();
        for (AddToCart.cartItem c : cart) {
            JsonObject cartItemJson = new JsonObject();
            cartItemJson.addProperty("productId", c.getProductId());
            cartItemJson.addProperty("title", c.getTitle());
            cartItemJson.addProperty("imageUrl", c.getImage());
            cartItemJson.addProperty("price", c.getPrice());
            cartArray.add(cartItemJson);
        }

        responseObject.addProperty("status", true);
        responseObject.addProperty("message", "Product removed from cart successfully!");
        responseObject.add("cart", cartArray);

        response.getWriter().write(responseObject.toString());
    }
}
