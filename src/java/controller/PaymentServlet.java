package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.PayHereUtil;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/api/payhere/hash")
public class PaymentServlet extends HttpServlet {

    private final String MERCHANT_ID = "";
    private final String MERCHANT_SECRET = "==";
    private final String CURRENCY = "LKR";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Gson gson = new Gson();
        JsonObject dataObject = gson.fromJson(request.getReader(), JsonObject.class);

        String firstName = dataObject.get("firstName").getAsString();
        String lastName = dataObject.get("lastName").getAsString();
        String email = dataObject.get("email").getAsString();
        String address = dataObject.get("address").getAsString();
        String total = dataObject.get("total").getAsString();
        String user = dataObject.get("user").getAsString();
        String listing = dataObject.get("listing").toString();

        String orderId = "ORDER_" + System.currentTimeMillis();

        String hash = PayHereUtil.generatePayHereHash(MERCHANT_ID, orderId, total, CURRENCY, MERCHANT_SECRET);

        JsonObject paymentResponse = new JsonObject();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(paymentResponse.toString());
    }
}
