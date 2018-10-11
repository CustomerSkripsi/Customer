package mobi.garden.bottomnavigationtest.LoginRegister;

public class User {
    String fullName,DOB,gender,contact,email,userName,password,address,codeReferral,userID;

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
}
