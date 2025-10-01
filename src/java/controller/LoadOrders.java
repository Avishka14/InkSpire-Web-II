package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtill;
import hibernate.Invoice;
import hibernate.Listing;
import hibernate.ListingInvoice;
import hibernate.Product;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadOrders", urlPatterns = {"/LoadOrders"})
public class LoadOrders extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer id = Integer.valueOf(request.getParameter("id"));
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", Boolean.FALSE);
        Gson gson = new Gson();

        try {

            Session session = HibernateUtill.getSessionFactory().openSession();

            Criteria c = session.createCriteria(Invoice.class);
            c.add(Restrictions.eq("user.id", id));

            List<Invoice> invoices = c.list();

            if (invoices.isEmpty()) {
                responseObject.addProperty("code", "404");
                return;
            }

            List<JsonObject> invoiceArray = new ArrayList<>();

            for (Invoice invoice : invoices) {

                JsonObject invoiceJson = new JsonObject();
                invoiceJson.addProperty("invoiceId", invoice.getId());
                invoiceJson.addProperty("amount", String.valueOf(invoice.getAmount()));
                invoiceJson.addProperty("date", invoice.getDate().toString());
                invoiceJson.addProperty("status", invoice.getOrderStatus().getValue());

                Criteria liCri = session.createCriteria(ListingInvoice.class);
                liCri.add(Restrictions.eq("invoice.id", invoice.getId()));
                List<ListingInvoice> listingInvoices = liCri.list();

                List<JsonObject> productArray = new ArrayList<>();

                for (ListingInvoice li : listingInvoices) {

                    Listing listing = li.getListing();
                    Product product = listing.getProduct();

                    JsonObject productJson = new JsonObject();
                    productJson.addProperty("title", product.getTitle());

                    String baseUrl = request.getScheme() + "://"
                            + request.getServerName() + ":"
                            + request.getServerPort()
                            + request.getContextPath();

                    String imagePath = "/ProductImageServlet/" + product.getId() + "/image1.png";
                    File productImage = new File("C:/InkSpireUploads/product-images/" + product.getId() + "/image1.png");

                    String imageUrl;
                    if (productImage.exists()) {
                        imageUrl = baseUrl + imagePath;
                    } else {
                        imageUrl = baseUrl + "/ProductImageServlet/null-image/404.webp";
                    }
                    
                      productJson.addProperty("imgUrl", imageUrl);
                    
                      productArray.add(productJson);

                }
                
                
                invoiceJson.add("products", gson.toJsonTree(productArray));
                invoiceArray.add(invoiceJson);

            }
            
            responseObject.addProperty("status", Boolean.TRUE);
            responseObject.add("invoices", gson.toJsonTree(invoiceArray));
            
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(responseObject));

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
