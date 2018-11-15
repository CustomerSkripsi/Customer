package mobi.garden.bottomnavigationtest.LoginRegister;

public class User {
    String fullName,DOB,gender,contact,email,userName,password,address,codeReferral,userID;
    public String  recipientName, recipientNumber, customerAddress, customerCity, customerPostalCode, customerProvince;
    public String customerLocationID;

    public User(String customerLocationID,String recipientName, String recipientNumber, String customerAddress, String customerCity, String customerPostalCode, String customerProvince){
        this.customerLocationID = customerLocationID;
        this.recipientName = recipientName;
        this.recipientNumber = recipientNumber;
        this.customerAddress = customerAddress;
        this.customerCity = customerCity;
        this.customerPostalCode = customerPostalCode;
        this.customerProvince = customerProvince;
    }

    public User(String fullName,String DOB,String gender,String contact,String email,String userName,String password,String address,String codeReferral){

        this.fullName = fullName;
        this.DOB = DOB;
        this.gender = gender;
        this.contact = contact;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.codeReferral = codeReferral;
    }

    public String getUserID() {
        return userID;
    }

    //    public User(String fullName, String DOB, String gender, String contact, String email, String address, String codeReferral) {
//        this.fullName = fullName;
//        this.DOB = DOB;
//        this.gender = gender;
//        this.contact = contact;
//        this.email = email;
//        this.address = address;
//        this.codeReferral = codeReferral;
//    }

    public User(String userName, String id){
        this.userName=userName;
        this.userID=id;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    public String getCustomerProvince() {
        return customerProvince;
    }

    public void setCustomerProvince(String customerProvince) {
        this.customerProvince = customerProvince;
    }

    public String getCustomerLocationID() {
        return customerLocationID;
    }

    public void setCustomerLocationID(String customerLocationID) {
        this.customerLocationID = customerLocationID;
    }
}
