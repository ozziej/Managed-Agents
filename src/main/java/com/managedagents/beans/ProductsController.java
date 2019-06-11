/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.beans;

import com.managedagents.entities.Products;
import com.managedagents.entities.Users;
import com.managedagents.stateless.ProductsBean;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.FilterEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

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
    
    public void newProduct(){
        selectedProduct = new Products();
    }

    public void updateProduct() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity;
        if (selectedProduct != null) {
            if (selectedProduct.getProductId().equals(0)) {
                productsBean.addNewProduct(selectedProduct);
                severity = FacesMessage.SEVERITY_INFO;
                shortMessage = "Added.";
                fullMessage = selectedProduct.getProductDescription()+ " was added.";
            }
            else {
                productsBean.editProduct(selectedProduct);
                severity = FacesMessage.SEVERITY_INFO;
                shortMessage = "Updated.";
                fullMessage = selectedProduct.getProductDescription()+ " was updated.";
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
    }

    public void onFilterEvent(FilterEvent event) {
        selectedProduct = null;
    }

    public void onPaginationEvent(PageEvent event) {
        selectedProduct = null;
    }

    public Products getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Products selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
}
