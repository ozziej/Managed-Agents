/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.beans;

import com.managedagents.entities.OrderItems;
import com.managedagents.entities.Orders;
import com.managedagents.entities.Products;
import com.managedagents.entities.Users;
import com.managedagents.stateless.OrdersBean;
import com.managedagents.stateless.ProductsBean;
import java.io.Serializable;
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
public class OrdersController implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginBean loginBean;

    @Inject
    OrdersBean ordersBean;

    @Inject
    ProductsBean productsBean;

    private List<Products> productList;

    private Users currentUser;

    private Orders selectedOrder;

    private OrderItems selectedOrderItem;

    private LazyDataModel<Orders> orders;

    private Products selectedProduct;

    private String selectedProductId;

    private boolean orderSelectable;

    @PostConstruct
    public void init()
    {
        currentUser = loginBean.getCurrentUser();
        getOrdersList();
    }

    public void getOrdersList()
    {
        orders = new LazyDataModel<Orders>()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public Orders getRowData(String rowKey)
            {
                for (Orders order : orders)
                {
                    if (order.getOrderId().equals(Integer.parseInt(rowKey)))
                    {
                        return order;
                    }
                }
                return null;
            }

            @Override
            public List<Orders> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
            {
                List<Orders> orderList = ordersBean.findAllOrders(first, pageSize, sortField, sortOrder, filters);
                Long rowCount = ordersBean.countAllOrders(sortField, sortOrder, filters);
                orders.setRowCount(rowCount.intValue());
                return orderList;
            }
        };
    }

    public void updateOrder()
    {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;

        if (selectedOrder != null)
        {
            selectedOrder.setEditingUser(null);
            selectedOrder.setModificationDate(Calendar.getInstance().getTime());
            ordersBean.editOrder(selectedOrder);
            shortMessage = "Order Updated.";
            fullMessage = "Order number " + selectedOrder.getOrderId() + " was updated.";
        }
        else
        {
            shortMessage = "Error.";
            fullMessage = "Nothing selected.";
            severity = FacesMessage.SEVERITY_ERROR;
        }
        selectedOrder = null;
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));

    }

    public void removeOrderItem(OrderItems item)
    {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;
        List<OrderItems> orderItemsList = selectedOrder.getOrderItemsList();
        if (item != null)
        {
            orderItemsList.remove(item);
            selectedOrder.setOrderItemsList(orderItemsList);
            shortMessage = "Removed";
            fullMessage = "Item " + item.getItemDescription() + " was removed.";
        }
        else
        {
            shortMessage = "Error.";
            fullMessage = "Nothing selected.";
            severity = FacesMessage.SEVERITY_ERROR;
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void addProductToList()
    {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;
        List<OrderItems> orderItemsList = selectedOrder.getOrderItemsList();

        OrderItems item = new OrderItems();
        if (selectedProduct != null)
        {
            Boolean containsProduct = orderItemsList.stream().filter(i -> i.getProduct().equals(selectedProduct))
                    .findAny()
                    .isPresent();
            if (!containsProduct)
            {
                item.setItemId(0);
                item.setItemDescription(selectedProduct.getProductDescription());
                item.setItemPrice(selectedProduct.getProductPrice());
                item.setItemQuantity(1);
                item.setOrder(selectedOrder);
                item.setProduct(selectedProduct);

                orderItemsList.add(item);
                selectedOrder.setOrderItemsList(orderItemsList);

                shortMessage = "Added.";
                fullMessage = selectedProduct.getProductDescription() + " was added.";
            }
            else
            {
                severity = FacesMessage.SEVERITY_WARN;
                shortMessage = "Warning.";
                fullMessage = selectedProduct.getProductDescription() + " is already present, please change quantity.";
            }
        }
        else
        {
            shortMessage = "Error.";
            fullMessage = "Nothing Selected.";
            severity = FacesMessage.SEVERITY_ERROR;
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void newOrder()
    {
        selectedOrder = new Orders(currentUser);
        productList = productsBean.findAllProducts();
    }

    public void editExistingOrder()
    {
        selectedOrder.setEditingUser(currentUser);
        selectedOrder.setModificationDate(Calendar.getInstance().getTime());
        ordersBean.editOrder(selectedOrder);
        productList = productsBean.findAllProducts();
    }

    public void cancelChanges()
    {
        String shortMessage = "Cancel.";
        String fullMessage = "Selection Canceled.";
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;
        Integer orderId = selectedOrder.getOrderId();
        if (orderId != 0)
        {
            selectedOrder = ordersBean.findOrderById(orderId);
            selectedOrder.setEditingUser(null);
            selectedOrder.setModificationDate(Calendar.getInstance().getTime());
            ordersBean.editOrder(selectedOrder);
        }
        selectedOrder = null;
        orderSelectable = false;
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public boolean isOrderSelectable()
    {
        return orderSelectable;
    }

    private boolean editTimeExpired()
    {
        Calendar currentTime = Calendar.getInstance();
        Calendar modificationTime = Calendar.getInstance();
        modificationTime.setTime(selectedOrder.getModificationDate());
        currentTime.add(Calendar.MINUTE, -15);
        return currentTime.after(modificationTime);
    }

    public void onRowSelect(SelectEvent event)
    {
        selectedOrder = (Orders) event.getObject();
        Users editingUser;
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;
        orderSelectable = false;

        if (selectedOrder != null)
        {
            editingUser = selectedOrder.getEditingUser();
            if (editingUser == null || editingUser.equals(currentUser) || editTimeExpired())
            {
                shortMessage = "";
                fullMessage = "";
                orderSelectable = true;
            }
        }

        if (!orderSelectable)
        {
            shortMessage = "Error.";
            fullMessage = "Order currently being edited";
            context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
        }
    }

    public void onFilterEvent(FilterEvent event)
    {
        selectedOrder = null;
    }

    public void onPaginationEvent(PageEvent event)
    {
        selectedOrder = null;
    }

    public Users getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser)
    {
        this.currentUser = currentUser;
    }

    public Orders getSelectedOrder()
    {
        return selectedOrder;
    }

    public void setSelectedOrder(Orders selectedOrder)
    {
        this.selectedOrder = selectedOrder;
    }

    public LazyDataModel<Orders> getOrders()
    {
        return orders;
    }

    public void setOrders(LazyDataModel<Orders> orders)
    {
        this.orders = orders;
    }

    public OrderItems getSelectedOrderItem()
    {
        return selectedOrderItem;
    }

    public void setSelectedOrderItem(OrderItems selectedOrderItem)
    {
        this.selectedOrderItem = selectedOrderItem;
    }

    public Products getSelectedProduct()
    {
        return selectedProduct;
    }

    public void setSelectedProduct(Products selectedProduct)
    {
        this.selectedProduct = selectedProduct;
    }

    public List<Products> getProductList()
    {
        return productList;
    }

    public void setProductList(List<Products> productList)
    {
        this.productList = productList;
    }

    public String getSelectedProductId()
    {
        return selectedProductId;
    }

    public void setSelectedProductId(String selectedProductId)
    {
        this.selectedProductId = selectedProductId;
    }

}
