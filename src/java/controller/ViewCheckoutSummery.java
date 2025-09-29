package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.CheckoutDataDTO;
import dto.CheckoutItemDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ViewCheckoutSummery", urlPatterns = {"/ViewCheckoutSummery"})
public class ViewCheckoutSummery extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CheckoutItemDTO> proData = (List<CheckoutItemDTO>) session.getAttribute("checkoutgoods");
        List<CheckoutDataDTO> userData = (List<CheckoutDataDTO>) session.getAttribute("checkoutuser");

        JsonArray summeryArray = new JsonArray();

        if (!proData.isEmpty() &&  !userData.isEmpty()) {
            for (CheckoutItemDTO dto : proData) {
                JsonObject proJson = new JsonObject();
                proJson.addProperty("title", dto.getTitle());
                proJson.addProperty("price", dto.getPrice());
                proJson.addProperty("img", dto.getImage());
                proJson.addProperty("listingId", dto.getListingId());
                summeryArray.add(proJson);
            }

            for (CheckoutDataDTO dto : userData) {
                JsonObject userJson = new JsonObject();
                userJson.addProperty("total", String.valueOf(dto.getTotal()));
                userJson.addProperty("userId", dto.getUserId());
                userJson.addProperty("shipping", dto.getShipping());
                summeryArray.add(userJson);
            }

            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(summeryArray));

        }

    }

}
