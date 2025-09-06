
package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "ProductImageServlet", urlPatterns = {"/ProductImageServlet/*"})
public class ProductImageServlet extends HttpServlet {
     private static final String IMAGE_DIR = "C:/InkSpireUploads/product-images";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         String pathInfo = req.getPathInfo(); // e.g. /278380/image1.png
        File file = new File(IMAGE_DIR, pathInfo);

        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setContentType(getServletContext().getMimeType(file.getName()));
        Files.copy(file.toPath(), resp.getOutputStream());
    }
    }
 
    


