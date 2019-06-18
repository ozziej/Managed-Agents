/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.beans;

import com.managedagents.constants.DefaultMessages;
import static com.managedagents.constants.DefaultProperties.BASE_DIRECTORY;
import com.managedagents.converters.ImageConverter;
import com.managedagents.entities.ProductImages;
import com.managedagents.entities.Products;
import com.managedagents.entities.Users;
import com.managedagents.stateless.ProductsBean;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.FilterEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author james
 */
@Named
@ViewScoped
public class ProductsController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    ProductsBean productsBean;

    @Inject
    LoginBean loginBean;

    private List<Products> productList;

    private LazyDataModel<Products> productData;

    private Users currentUser;

    private Products selectedProduct;

    ProductImages selectedProductImage;

    List<ProductImages> imagesList;

    @PostConstruct
    public void init() {
        currentUser = loginBean.getCurrentUser();
        findProductData();
    }

    public void findAllProducts() {
        productList = productsBean.findAllProducts();
    }

    public void findProductData() {
        productData = new LazyDataModel<Products>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Products> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Products> productList = productsBean.findAllProducts(first, pageSize, sortField, sortOrder, filters);
                Long rowCount = productsBean.countAllProducts(sortField, sortOrder, filters);
                productData.setRowCount(rowCount.intValue());
                return productList;
            }

            @Override
            public Products getRowData(String rowKey) {
                for (Products product : productData.getWrappedData()) {
                    if (product.getProductId().equals(Integer.parseInt(rowKey))) {
                        return product;
                    }
                }
                return null;
            }
        };
    }

    public void newProduct() {
        selectedProduct = new Products();
        selectedProductImage = null;
        imagesList = new ArrayList<>();
    }

    public void updateProduct() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity;
        if (selectedProduct != null) {
            selectedProduct.setProductImagesList(imagesList);

            if (selectedProduct.getProductId().equals(0)) {
                productsBean.addNewProduct(selectedProduct);
                severity = FacesMessage.SEVERITY_INFO;
                shortMessage = "Added.";
                fullMessage = selectedProduct.getProductDescription() + " was added.";
            }
            else {
                productsBean.editProduct(selectedProduct);
                severity = FacesMessage.SEVERITY_INFO;
                shortMessage = "Updated.";
                fullMessage = selectedProduct.getProductDescription() + " was updated.";
            }
        }
        else {
            severity = FacesMessage.SEVERITY_ERROR;
            shortMessage = "No Product Selected";
            fullMessage = "You need to select a prodcut before saving.";
            context.validationFailed();
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void handleUploadFile(FileUploadEvent event) {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity;
        UploadedFile uploadedFile = event.getFile();
        String subDirectory;
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        if (selectedProduct != null) {
            subDirectory = selectedProduct.getProductId().toString();
            selectedProductImage = new ProductImages(selectedProduct.getProductDescription(), fileName, selectedProduct);
            Path destination = Paths.get(BASE_DIRECTORY + "/products/" + subDirectory + "/" + fileName + ".jpg");
            try {
                Files.createDirectories(Paths.get((BASE_DIRECTORY + "/products/" + subDirectory)));
                Path scaledImage = ImageConverter.scaleImage(uploadedFile, 600).toPath();
                Files.move(scaledImage, destination, REPLACE_EXISTING);
                severity = FacesMessage.SEVERITY_INFO;
                shortMessage = "Added.";
                fullMessage = "New File Uploaded.";
                imagesList.add(selectedProductImage);
                selectedProduct.setProductImagesList(imagesList);
                productsBean.editProduct(selectedProduct);
            }
            catch (IOException ex) {
                shortMessage = DefaultMessages.DEFAULT_ERROR;
                severity = FacesMessage.SEVERITY_ERROR;
                Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
                fullMessage = "Failed :" + ex.toString();
            }
        }
        else {
            shortMessage = DefaultMessages.DEFAULT_ERROR;
            fullMessage = "Nothing Selected";
            severity = FacesMessage.SEVERITY_ERROR;
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void deleteImage() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity;

        String subDirectory = selectedProductImage.getProduct().getProductId().toString() + "/";
        String fileName = selectedProductImage.getImageUri();
        Path destination = Paths.get(BASE_DIRECTORY + "/products/" + subDirectory + "/" + fileName + ".jpg");
        try {
            Files.delete(destination);
            severity = FacesMessage.SEVERITY_INFO;
            shortMessage = "Deleted";
            fullMessage = selectedProduct.getProductDescription() + " had a file deleted.";
            imagesList.remove(selectedProductImage);
            selectedProduct.setProductImagesList(imagesList);
            productsBean.editProduct(selectedProduct);
        }
        catch (IOException ex) {
            shortMessage = DefaultMessages.DEFAULT_ERROR;
            severity = FacesMessage.SEVERITY_ERROR;
            fullMessage = "Failed :" + ex.getLocalizedMessage();
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public StreamedContent getDownloadFile() {
        String subDirectory = selectedProductImage.getProduct().getProductId().toString() + "/";
        String fileName = selectedProductImage.getImageUri();
        Path destination = Paths.get(BASE_DIRECTORY + "/products/" + subDirectory + "/" + fileName + ".jpg");
        InputStream stream = null;
        try {
            stream = new FileInputStream(destination.toFile());
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new DefaultStreamedContent(stream, "application/octet-stream", fileName + ".jpg");
    }

    public List<Products> getProductList() {
        return productList;
    }

    public void setProductList(List<Products> productList) {
        this.productList = productList;
    }

    public LazyDataModel<Products> getProductData() {
        return productData;
    }

    public void onRowSelect(SelectEvent event) {
        selectedProduct = (Products) event.getObject();
        imagesList = selectedProduct.getProductImagesList();
    }

    public void onFilterEvent(FilterEvent event) {
        selectedProduct = null;
        imagesList = null;
    }

    public void onPaginationEvent(PageEvent event) {
        selectedProduct = null;
        imagesList = null;
    }

    public Products getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Products selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public ProductImages getSelectedProductImage() {
        return selectedProductImage;
    }

    public void setSelectedProductImage(ProductImages selectedProductImage) {
        this.selectedProductImage = selectedProductImage;
    }

    public List<ProductImages> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<ProductImages> imagesList) {
        this.imagesList = imagesList;
    }

}
