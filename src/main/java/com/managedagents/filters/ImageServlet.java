/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.filters;

import com.managedagents.beans.LoginBean;
import static com.managedagents.constants.DefaultProperties.BASE_DIRECTORY;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author james
 */
@WebServlet(name = "ImageServlet", urlPatterns ={"/ImageServlet/*"})
public class ImageServlet extends HttpServlet
{
    @Inject
    LoginBean loginBean;
    
    private static final int DEFAULT_BUFFER_SIZE = 10240;
    private static final long serialVersionUID = 1L;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        String requestedImage = request.getPathInfo();
        try 
        {	    
            if (requestedImage == null) 
            {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            
            File image = new File(BASE_DIRECTORY, URLDecoder.decode(requestedImage,"UTF-8"));
            
            if (!image.exists()) 
            {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            else
            {
                String contentType = getServletContext().getMimeType(image.getName());

                if (contentType == null || !contentType.startsWith("image")) 
                {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                response.reset();
                response.setBufferSize(DEFAULT_BUFFER_SIZE);
                response.setContentType(contentType);
                response.setHeader("Content-Length", String.valueOf(image.length()));
                response.setHeader("Content-Disposition","inline; filename='" + image.getName() + "'");
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                response.setDateHeader("Expires", 0);
                BufferedInputStream input = null;
                BufferedOutputStream output = null;

                try 
                {
                    input = new BufferedInputStream(new FileInputStream(image),DEFAULT_BUFFER_SIZE);
                    output = new BufferedOutputStream(response.getOutputStream(),DEFAULT_BUFFER_SIZE);

                    // Write file contents to response.
                    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                    int length;
                    while ((length = input.read(buffer)) > 0) 
                    {
                        output.write(buffer, 0, length);
                    }
                } 
                finally 
                {
                    close(output);
                    close(input);
                }
            }
        }
        catch (IOException e)
        {
            Logger.getLogger(ImageServlet.class.getName()).log(Level.INFO, e.getMessage());
        }    
    }
    
    private static void close(Closeable resource) 
    {
        if (resource != null) 
        {
            try 
            {
                resource.close();
            }
            catch (IOException e) 
            {
                Logger.getLogger(ImageServlet.class.getName()).log(Level.INFO, e.toString());
            }
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}
