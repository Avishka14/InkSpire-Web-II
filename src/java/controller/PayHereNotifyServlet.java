package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.PayHereUtil;


@WebServlet("/payhere/notify")
public class PayHereNotifyServlet extends HttpServlet {

    private final String MERCHANT_ID = ""; 
    private final String MERCHANT_SECRET = ""; 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String orderId = request.getParameter("order_id");
        String payhereAmount = request.getParameter("payhere_amount");
        String currency = request.getParameter("currency");
        String statusCode = request.getParameter("status_code"); 
        String md5sig = request.getParameter("md5sig");

        String localMd5 = PayHereUtil.generatePayHereHash(
            MERCHANT_ID,
            orderId,
            payhereAmount,
            currency,
            MERCHANT_SECRET
        );

        if (!localMd5.equalsIgnoreCase(md5sig)) {
            System.out.println("MD5 verification failed for order: " + orderId);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (statusCode) {
            case "2":
                System.out.println("Payment successful for order: " + orderId + ", amount: " + payhereAmount);
                break;
            case "-1":
                System.out.println("Payment cancelled for order: " + orderId);
                break;
            case "-2":
                System.out.println("Payment failed for order: " + orderId);
                break;
            default:
                System.out.println("Payment pending or unknown status for order: " + orderId);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
