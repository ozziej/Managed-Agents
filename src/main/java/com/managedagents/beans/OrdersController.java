/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.beans;

import com.managedagents.constants.DefaultMessages;
import com.managedagents.constants.OrderStatus;
import com.managedagents.constants.UsersStatus;
import com.managedagents.entities.OrderItems;
import com.managedagents.entities.Orders;
import com.managedagents.entities.Products;
import com.managedagents.entities.Users;
import com.managedagents.stateless.OrdersBean;
import com.managedagents.stateless.ProductsBean;
import com.managedagents.stateless.UsersBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
public class OrdersController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginBean loginBean;

    @Inject
    OrdersBean ordersBean;

    @Inject
    ProductsBean productsBean;
    
    @Inject
    UsersBean usersBean;

    private List<Products> productList;

    private Users currentUser;

    private Users selectedUser;

    private List<Users> otherUsersList ;
    
    private Orders selectedOrder;

    private OrderItems selectedOrderItem;

    private LazyDataModel<Orders> orders;

    private Products selectedProduct;

    private String selectedProductId;

    private boolean orderSelectable;

    @PostConstruct
    public void init() {
        currentUser = loginBean.getCurrentUser();
        findAllUsers();
        getOrdersList();
    }

    public void getOrdersList() {
        orders = new LazyDataModel<Orders>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Orders> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Orders> orderList = ordersBean.findAllUserOrders(otherUsersList, first, pageSize, sortField, sortOrder, filters);
                Long rowCount = ordersBean.countAllUserOrders(otherUsersList, sortField, sortOrder, filters);
                orders.setRowCount(rowCount.intValue());
                return orderList;
            }

            @Override
            public Orders getRowData(String rowKey) {
                for (Orders order : orders.getWrappedData()) {
                    if (order.getOrderId().equals(Integer.parseInt(rowKey))) {
                        return order;
                    }
                }
                return null;
            }
        };
    }

    public void updateOrder() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;

        if (selectedOrder != null) {
            selectedOrder.setEditingUser(null);
            selectedOrder.setModificationDate(Calendar.getInstance().getTime());
            if (selectedOrder.getOrderId().equals(0)) {
                selectedOrder = ordersBean.addNewOrder(selectedOrder);
                shortMessage = "Order Created.";
                fullMessage = "New order number " + selectedOrder.getOrderId() + " was updated.";
            }
            else {
                ordersBean.editOrder(selectedOrder);
                shortMessage = "Order Updated.";
                fullMessage = "Order number " + selectedOrder.getOrderId() + " was updated.";
            }
        }
        else {
            shortMessage = DefaultMessages.DEFAULT_ERROR;
            fullMessage = "Nothing selected.";
            severity = FacesMessage.SEVERITY_ERROR;
        }
        selectedOrder = null;
        orderSelectable = false;
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void removeOrderItem(OrderItems item) {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;
        List<OrderItems> orderItemsList = selectedOrder.getOrderItemsList();
        if (item != null) {
            orderItemsList.remove(item);
            selectedOrder.setOrderItemsList(orderItemsList);
            shortMessage = "Removed";
            fullMessage = "Item " + item.getItemDescription() + " was removed.";
        }
        else {
            shortMessage = DefaultMessages.DEFAULT_ERROR;
            fullMessage = "Nothing selected.";
            severity = FacesMessage.SEVERITY_ERROR;
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void addProductToList() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;
        List<OrderItems> orderItemsList = selectedOrder.getOrderItemsList();

        OrderItems item = new OrderItems();
        if (selectedProduct != null) {
            Boolean containsProduct = orderItemsList.stream().anyMatch(i -> i.getProduct().equals(selectedProduct));
            if (!containsProduct) {
                item.setItemId(0);
                item.setItemDescription(selectedProduct.getProductDescription());
                item.setItemPrice(selectedProduct.getProductPrice());
                item.setItemQuantity(1);
                item.setOrder(selectedOrder);
                item.setProduct(selectedProduct);
                item.setUser(currentUser);

                orderItemsList.add(item);
                selectedOrder.setOrderItemsList(orderItemsList);

                shortMessage = "Added.";
                fullMessage = selectedProduct.getProductDescription() + " was added.";
            }
            else {
                severity = FacesMessage.SEVERITY_WARN;
                shortMessage = "Warning.";
                fullMessage = selectedProduct.getProductDescription() + " is already present, please change quantity.";
            }
        }
        else {
            shortMessage = DefaultMessages.DEFAULT_ERROR;
            fullMessage = "Nothing Selected.";
            severity = FacesMessage.SEVERITY_ERROR;
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void newOrder() {
        selectedOrder = new Orders(currentUser);
        productList = productsBean.findAllProducts();
    }

    public void editExistingOrder() {
        selectedOrder.setEditingUser(currentUser);
        selectedOrder.setModificationDate(Calendar.getInstance().getTime());
        ordersBean.editOrder(selectedOrder);
        productList = productsBean.findAllProducts();
    }

    public void cancelChanges() {
        String shortMessage = "Cancel.";
        String fullMessage = "Selection Canceled.";
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;
        Integer orderId = selectedOrder.getOrderId();
        if (orderId != 0) {
            selectedOrder = ordersBean.findOrderById(orderId);
            selectedOrder.setEditingUser(null);
            selectedOrder.setModificationDate(Calendar.getInstance().getTime());
            ordersBean.editOrder(selectedOrder);
        }
        selectedOrder = null;
        orderSelectable = false;
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public boolean isOrderSelectable() {
        return orderSelectable;
    }
    
    public boolean isOrderEditable(){
        if (selectedOrder == null){
            return false;
        }
        else return selectedOrder.getStatusType().equals("NEW");
    }

    private boolean editTimeExpired() {
        Calendar currentTime = Calendar.getInstance();
        Calendar modificationTime = Calendar.getInstance();
        modificationTime.setTime(selectedOrder.getModificationDate());
        currentTime.add(Calendar.MINUTE, -15);
        return currentTime.after(modificationTime);
    }

    public void onRowSelect(SelectEvent event) {
        selectedOrder = (Orders) event.getObject();
        Users editingUser;
        String shortMessage = "";
        String fullMessage = "";
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;
        orderSelectable = false;

        if (selectedOrder != null) {
            editingUser = selectedOrder.getEditingUser();
            if (editingUser == null || editingUser.equals(currentUser) || editTimeExpired()) {
                orderSelectable = true;
            }
        }

        if (!orderSelectable) {
            shortMessage = DefaultMessages.DEFAULT_ERROR;
            fullMessage = "Order currently being edited";
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void findAllUsers() {
        otherUsersList = new ArrayList<>();
        if (currentUser.getUserStatus().equals(UsersStatus.ADMIN.toString())) {
            otherUsersList.addAll(usersBean.findOtherUsers(currentUser));
        }
        else {
            otherUsersList.add(currentUser);
        }
    }

    public List<String> getOrderStatusTypes() {
        List<String> statusList = new ArrayList<>();
        for (OrderStatus s : OrderStatus.values()){
            statusList.add(s.toString());
        }
        return statusList;
    }    

    public void onFilterEvent(FilterEvent event) {
        selectedOrder = null;
    }

    public void onPaginationEvent(PageEvent event) {
        selectedOrder = null;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public Orders getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Orders selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public LazyDataModel<Orders> getOrders() {
        return orders;
    }

    public void setOrders(LazyDataModel<Orders> orders) {
        this.orders = orders;
    }

    public OrderItems getSelectedOrderItem() {
        return selectedOrderItem;
    }

    public void setSelectedOrderItem(OrderItems selectedOrderItem) {
        this.selectedOrderItem = selectedOrderItem;
    }

    public Products getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Products selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<Products> getProductList() {
        return productList;
    }

    public void setProductList(List<Products> productList) {
        this.productList = productList;
    }

    public String getSelectedProductId() {
        return selectedProductId;
    }

    public void setSelectedProductId(String selectedProductId) {
        this.selectedProductId = selectedProductId;
    }

    public Users getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<Users> getOtherUsersList() {
        return otherUsersList;
    }

    public void setOtherUsersList(List<Users> otherUsersList) {
        this.otherUsersList = otherUsersList;
    }

}
