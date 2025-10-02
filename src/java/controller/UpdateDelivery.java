package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
import hibernate.Invoice;
import hibernate.OrderStatus;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "UpdateDelivery", urlPatterns = {"/UpdateDelivery"})
public class UpdateDelivery extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject dataObject = gson.fromJson(request.getReader(), JsonObject.class);
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);

        int userId = dataObject.get("user").getAsInt();
        String invoiceID = dataObject.get("invoice").getAsString();

        try {

            Session session = HibernateUtill.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            Criteria userC = session.createCriteria(User.class);
            userC.add(Restrictions.eq("id", userId));

            if (!userC.list().isEmpty()) {

                Invoice invoice = (Invoice) session.get(Invoice.class, invoiceID);

                if (invoice != null) {
                  
                    OrderStatus orderStatus = (OrderStatus) session.get(OrderStatus.class, 1);
                    
                    invoice.setOrderStatus(orderStatus);
                    session.update(invoice);
                    tx.commit();

                    invoice.setOrderStatus(orderStatus);
                    responseObject.addProperty("status", Boolean.TRUE);

                } else {
                  responseObject.addProperty("message", "Invoice not Found!");
                }

            } else {
                responseObject.addProperty("message", "Please Log In Again!");
            }

            session.close();

        } catch (Exception e) {
            e.printStackTrace();
            responseObject.addProperty("message", "Exception" + e);
        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
