package mobi.garden.bottomnavigationtest.Model;

public class cart {
    public String apotekName, apotekAddress,apotekPhone, productName, productID,userName,userPhone;
    public int outletDeliveryFee, outletProductStockQty, cartProductQty, cartProductPrice;

    public int cartID,customerID,statudOrderID,orderLocationID,confirmID,deliveryStatusID;
    public String outletID,orderDate,confirmDate;



    public cart(String apotekName, String apotekAddress, String productName, String productID, int outletDeliveryFee, int outletProductStockQty, int cartProductQty, int cartProductPrice) {
        this.apotekName = apotekName;
        this.apotekAddress = apotekAddress;
        this.productName = productName;
        this.productID = productID;
        this.outletDeliveryFee = outletDeliveryFee;
        this.outletProductStockQty = outletProductStockQty;
        this.cartProductQty = cartProductQty;
        this.cartProductPrice = cartProductPrice;
    }

    public cart(int cartID, String outletID,int customerID,int statudOrderID,int orderLocationID,String orderDate,int confirmID,String confirmDate,int deliveryStatusID){
        this.cartID = cartID;
        this.outletID = outletID;
        this.customerID = customerID;
        this.statudOrderID = statudOrderID;
        this.orderLocationID = orderLocationID;
        this.orderDate = orderDate;
        this.confirmID = confirmID;
        this.confirmDate = confirmDate;
        this.deliveryStatusID = deliveryStatusID;
    }

    public cart(String apotekName, String apotekAddress,String apotekPhone,String userName,String userPhone){
        this.apotekName = apotekName;
        this.apotekAddress = apotekAddress;
        this.apotekPhone = apotekPhone;
        this.userName = userName;
        this.userPhone = userPhone;
    }


    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getStatudOrderID() {
        return statudOrderID;
    }

    public void setStatudOrderID(int statudOrderID) {
        this.statudOrderID = statudOrderID;
    }

    public int getOrderLocationID() {
        return orderLocationID;
    }

    public void setOrderLocationID(int orderLocationID) {
        this.orderLocationID = orderLocationID;
    }

    public int getConfirmID() {
        return confirmID;
    }

    public void setConfirmID(int confirmID) {
        this.confirmID = confirmID;
    }

    public int getDeliveryStatusID() {
        return deliveryStatusID;
    }

    public void setDeliveryStatusID(int deliveryStatusID) {
        this.deliveryStatusID = deliveryStatusID;
    }

    public String getOutletID() {
        return outletID;
    }

    public void setOutletID(String outletID) {
        this.outletID = outletID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getApotekName() {
        return apotekName;
    }

    public void setApotekName(String apotekName) {
        this.apotekName = apotekName;
    }

    public String getApotekAddress() {
        return apotekAddress;
    }

    public void setApotekAddress(String apotekAddress) {
        this.apotekAddress = apotekAddress;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getOutletDeliveryFee() {
        return outletDeliveryFee;
    }

    public void setOutletDeliveryFee(int outletDeliveryFee) {
        this.outletDeliveryFee = outletDeliveryFee;
    }

    public int getOutletProductStockQty() {
        return outletProductStockQty;
    }

    public void setOutletProductStockQty(int outletProductStockQty) {
        this.outletProductStockQty = outletProductStockQty;
    }

    public int getCartProductQty() {
        return cartProductQty;
    }

    public void setCartProductQty(int cartProductQty) {
        this.cartProductQty = cartProductQty;
    }

    public int getCartProductPrice() {
        return cartProductPrice;
    }

    public void setCartProductPrice(int cartProductPrice) {
        this.cartProductPrice = cartProductPrice;
    }
}
