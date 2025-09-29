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
    private final String MERCHANT_SECRET = "";
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
        String city = dataObject.get("city").getAsString();
        String contact = dataObject.get("contact").getAsString();
        String listing = dataObject.get("listing").toString();
        
        System.out.println(total);
        
        String orderId = String.valueOf(System.currentTimeMillis());

        String hash = PayHereUtil.generatePayHereHash(MERCHANT_ID, orderId, total, CURRENCY, MERCHANT_SECRET);
        
        JsonObject paymentResponse = new JsonObject();
        paymentResponse.addProperty("sandbox", true);
        paymentResponse.addProperty("merchant_id", MERCHANT_ID);
        paymentResponse.addProperty("return_url", "http://localhost:8080/InkSpire/payhere/success.html");
        paymentResponse.addProperty("cancel_url", "http://localhost:8080/InkSpire/payhere/cancel.html");
        paymentResponse.addProperty("notify_url", "http://localhost:8080/InkSpire/payhere/notify");
        paymentResponse.addProperty("order_id", orderId);
        paymentResponse.addProperty("items", "Order Listings" +listing);
        paymentResponse.addProperty("amount", total); 
        paymentResponse.addProperty("currency", CURRENCY);
        paymentResponse.addProperty("hash", hash);


        paymentResponse.addProperty("first_name", firstName);
        paymentResponse.addProperty("last_name", lastName);
        paymentResponse.addProperty("email",email);
        paymentResponse.addProperty("phone", contact);
        paymentResponse.addProperty("address", address);
        paymentResponse.addProperty("city", city);
        paymentResponse.addProperty("country", "Sri Lanka");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(paymentResponse.toString());
    }
}
