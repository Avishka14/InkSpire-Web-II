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
        // 1. Get transaction details sent by PayHere
        String orderId = request.getParameter("order_id");
        String payhereAmount = request.getParameter("payhere_amount");
        String currency = request.getParameter("currency");
        String statusCode = request.getParameter("status_code"); // 2 = paid
        String md5sig = request.getParameter("md5sig");

        // 2. Verify MD5 hash to ensure notification is valid
        String localMd5 = PayHereUtil.generatePayHereHash(
            MERCHANT_ID,
            orderId,
            payhereAmount,
            currency,
            MERCHANT_SECRET
        );

        // PayHere sends uppercase MD5
        if (!localMd5.equalsIgnoreCase(md5sig)) {
            System.out.println("MD5 verification failed for order: " + orderId);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 3. Process payment based on status code
        // Status codes: 2 = Paid, 0 = Pending, -1 = Cancelled, -2 = Failed
        switch (statusCode) {
            case "2":
                System.out.println("Payment successful for order: " + orderId + ", amount: " + payhereAmount);
                // TODO: Update your database: mark order as PAID
                break;
            case "-1":
                System.out.println("Payment cancelled for order: " + orderId);
                // TODO: Update DB: mark order as CANCELLED
                break;
            case "-2":
                System.out.println("Payment failed for order: " + orderId);
                // TODO: Update DB: mark order as FAILED
                break;
            default:
                System.out.println("Payment pending or unknown status for order: " + orderId);
                // TODO: Update DB: mark order as PENDING
        }

        // 4. Respond with 200 OK to acknowledge receipt
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
