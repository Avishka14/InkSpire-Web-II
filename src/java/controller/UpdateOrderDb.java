package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
import hibernate.Invoice;
import hibernate.Listing;
import hibernate.ListingInvoice;
import hibernate.OrderStatus;
import hibernate.ShippingAddress;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "UpdateOrderDb", urlPatterns = {"/UpdateOrderDb"})
public class UpdateOrderDb extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject dataObject = gson.fromJson(request.getReader(), JsonObject.class);
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);

        String orderId = dataObject.get("orderId").getAsString();
        String amount = dataObject.get("amount").getAsString();
        String addressId = dataObject.get("addressId").getAsString();
        String userId = dataObject.get("userId").getAsString();

        JsonArray listingArray = dataObject.getAsJsonArray("listing");

        System.out.println(orderId);
        System.out.println(amount);
        System.out.println(addressId);
        System.out.println(userId);
        System.out.println(listingArray);

        if (userId.equals("null")) {
            String responseText = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(responseText);
            return;
        }

        Session hibernateSession = HibernateUtill.getSessionFactory().openSession();
        Transaction tx = hibernateSession.beginTransaction();

        Criteria criteria = hibernateSession.createCriteria(Invoice.class);

        criteria.add(Restrictions.eq("id", orderId));

        if (criteria.list().isEmpty()) {

            ShippingAddress address = (ShippingAddress) hibernateSession.get(ShippingAddress.class, Integer.parseInt(addressId));
            User user = (User) hibernateSession.get(User.class, Integer.parseInt(userId));

            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setId(1);

            Invoice invoice = new Invoice();
            invoice.setId(orderId);
            invoice.setAmount(Double.valueOf(amount));
            invoice.setAddress(address);
            invoice.setDate(new Date());
            invoice.setUser(user);
            invoice.setOrderStatus(orderStatus);

            hibernateSession.save(invoice);

            for (JsonElement itemElement : listingArray) {

                int listingId = itemElement.getAsInt();

                Listing listing = (Listing) hibernateSession.get(Listing.class, listingId);

                ListingInvoice listingInvoice = new ListingInvoice();
                listingInvoice.setInvoice(invoice);
                listingInvoice.setListing(listing);
                hibernateSession.save(listingInvoice);

            }

            tx.commit();
            hibernateSession.close();
            
            responseObject.addProperty("status", Boolean.TRUE);
            String responseText = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(responseText);

        } else {
            hibernateSession.close();
            responseObject.addProperty("message", "Order is Already Completed");
            String responseText = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(responseText);
        }

    }

}
