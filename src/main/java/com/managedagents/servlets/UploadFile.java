/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.servlets;

import static com.managedagents.constants.DefaultProperties.BASE_DIRECTORY;
import com.managedagents.entities.Companies;
import com.managedagents.stateless.CompaniesBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author james
 */
@WebServlet(name = "UploadFile", urlPatterns = {"/UploadFile"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 10, fileSizeThreshold = 1024 * 1024)
public class UploadFile extends HttpServlet {

    @Inject
    CompaniesBean companiesBean;

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            OutputStream out;
            InputStream filecontent;
            final String path = request.getParameter("companyId");
            final Part filePart = request.getPart("selectedFile");
            Integer companyId = Integer.parseInt(path);
            String fileName = UUID.randomUUID().toString().replaceAll("-", "");

            final String subDirectory = BASE_DIRECTORY + File.separator + "companies" + File.separator;
            if (fileName != null) {
                Files.createDirectories(Paths.get(subDirectory + path));
                out = new FileOutputStream(new File(subDirectory + path + File.separator + fileName + ".jpg"));
                filecontent = filePart.getInputStream();

                int read;
                final byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.close();
                filecontent.close();

                updateCompany(companyId, fileName);

                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    private void updateCompany(Integer companyId, String fileName) {
        String previousFile;
        Companies selectedCompany = companiesBean.findCompaniesById(companyId);
        previousFile = selectedCompany.getImageUri();
        if (previousFile != null && !previousFile.isEmpty() && !selectedCompany.isImageEmpty()) {
            try {
                Path previousDestination = Paths.get(BASE_DIRECTORY + "/companies/" + companyId + "/" + previousFile + ".jpg");
                if (Files.exists(previousDestination)) {
                    Files.delete(previousDestination);
                }
            }
            catch (IOException ex) {
                Logger.getLogger(UploadFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        selectedCompany.setImageUri(fileName);
        companiesBean.editCompany(selectedCompany);
    }

    private String getFileName(final Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
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
            throws ServletException, IOException {
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
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
