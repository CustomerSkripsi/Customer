package mobi.garden.bottomnavigationtest.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ASUS on 5/7/2018.
 */

public class obat implements Parcelable {
    public String productID;
    public String productName;



    public String productPhoto;
    public String productDescription;
    public String categoryID;
    public String memberID;
    public int outletProductPrice;
    public int outletProductStockQty;
    public int outletProductPriceafterDsc;
    public int cartProductPrice;
    public int cartProductQty;
    public String indikasi,kandungan,dosis,carapakai,kemasan,golongan,resepYN,kontraindikasi,carasimpan,principal;
    String tempfoto;

    public obat(String idProduk,String productName, String tempfoto, int outletProductPrice, int outletProductStockQty) {
        productID =idProduk;
        this.productName = productName;
        this.tempfoto = tempfoto;
        this.outletProductPrice = outletProductPrice;
        this.outletProductStockQty = outletProductStockQty;
    }
    public obat(int outletProductStockQty) {
        this.outletProductStockQty = outletProductStockQty;
    }
    public obat(String productID, String productName, String productPhoto, String productDescription,
                String indikasi, String kandungan, String dosis, String carapakai, String kemasan,
                String golongan, String resepYN, String kontraindikasi, String carasimpan, String principal, String categoryID)
    {
        this.productID = productID;
        this.productName = productName;
        this.productPhoto = productPhoto;
        this.productDescription = productDescription;
        this.indikasi = indikasi;
        this.kandungan = kandungan;
        this.dosis = dosis;
        this.carapakai = carapakai;
        this.kemasan = kemasan;
        this.golongan = golongan;
        this.resepYN = resepYN;
        this.kontraindikasi = kontraindikasi;
        this.carasimpan = carasimpan;
        this.principal = principal;
        this.categoryID=categoryID;
    }

    public obat(String productID, String productName, String productPhoto,String productDescription,
                String indikasi, String kandungan, String dosis, String carapakai, String kemasan,
                String golongan, String resepYN, String kontraindikasi, String carasimpan, String principal,
                String categoryID, int outletProductPrice,  int outletProductStockQty){
        this.productID = productID;
        this.productName = productName;
        this.productPhoto = productPhoto;
        this.productDescription = productDescription;
        this.indikasi = indikasi;
        this.kandungan = kandungan;
        this.dosis = dosis;
        this.carapakai = carapakai;
        this.kemasan = kemasan;
        this.golongan = golongan;
        this.resepYN = resepYN;
        this.kontraindikasi = kontraindikasi;
        this.carasimpan = carasimpan;
        this.principal = principal;
        this.categoryID = categoryID;
        this.outletProductPrice = outletProductPrice;
        this.outletProductStockQty = outletProductStockQty;
    }

    public obat(String productName, String productID,int cartproductQty,int outletProductStockQty, int cartProductPrice,  int outletProductPriceafterDsc){
        this.productName = productName;
        this.productID = productID;
        cartProductQty = cartproductQty;
        this.outletProductStockQty = outletProductStockQty;
        this.cartProductPrice = cartProductPrice;
        this.outletProductPriceafterDsc = outletProductPriceafterDsc;
    }
    public obat ( String productID,String productName,int outletProductStockQty,int outletProductPrice, String memberID){
        this.productID = productID;
        this.productName = productName;
        this.outletProductStockQty = outletProductStockQty;
        this.outletProductPrice = outletProductPrice;
        this.memberID = memberID;
    }

    public obat(String productID, String productName, String productPhoto, int outletProductPrice) {
        this.productID = productID;
        this.productName = productName;
        this.productPhoto = productPhoto;
        this.outletProductPrice = outletProductPrice;
    }

    public int getOutletProductPrice() {
        return outletProductPrice;
    }

    public void setOutletProductPrice(int outletProductPrice) {
        this.outletProductPrice = outletProductPrice;
    }

    public int getOutletProductStockQty() {
        return outletProductStockQty;
    }

    public void setOutletProductStockQty(int outletProductStockQty) {
        this.outletProductStockQty = outletProductStockQty;
    }

    public int getCartProductPrice() {
        return cartProductPrice;
    }

    public void setCartProductPrice(int cartProductPrice) {
        this.cartProductPrice = cartProductPrice;
    }

    public int getCartProductQty() {
        return cartProductQty;
    }

    public void setCartProductQty(int cartProductQty) {
        this.cartProductQty = cartProductQty;
    }

    public String getProductID() {

        return productID;
    }

    public void setProductID(String productID) {

        this.productID = productID;
    }

    public String getProductName() {

        return productName;
    }

    public void setProductName(String productName) {

        this.productName = productName;
    }

    public String getProductPhoto() {

        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {

        this.productPhoto = productPhoto;
    }

    public String getProductDescription() {

        return productDescription;
    }

    public int getOutletProductPriceafterDsc() {
        return outletProductPriceafterDsc;
    }

    public void setOutletProductPriceafterDsc(int outletProductPriceafterDsc) {
        this.outletProductPriceafterDsc = outletProductPriceafterDsc;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getIndikasi() {
        return indikasi;
    }

    public void setIndikasi(String indikasi) {
        this.indikasi = indikasi;
    }

    public String getKandungan() {

        return kandungan;
    }

    public void setKandungan(String kandungan) {

        this.kandungan = kandungan;
    }

    public String getDosis() {

        return dosis;
    }

    public void setDosis(String dosis) {

        this.dosis = dosis;
    }

    public String getCarapakai() {

        return carapakai;
    }

    public void setCarapakai(String carapakai) {

        this.carapakai = carapakai;
    }

    public String getKemasan() {
        return kemasan;
    }

    public void setKemasan(String kemasan) {

        this.kemasan = kemasan;
    }

    public String getGolongan() {

        return golongan;
    }

    public void setGolongan(String golongan) {

        this.golongan = golongan;
    }

    public String getResepYN() {

        return resepYN;
    }

    public void setResepYN(String resepYN) {

        this.resepYN = resepYN;
    }

    public String getKontraindikasi() {

        return kontraindikasi;
    }

    public void setKontraindikasi(String kontraindikasi) {

        this.kontraindikasi = kontraindikasi;
    }

    public String getCarasimpan() {

        return carasimpan;
    }

    public void setCarasimpan(String carasimpan) {

        this.carasimpan = carasimpan;
    }

    public String getPrincipal() {

        return principal;
    }

    public void setPrincipal(String principal) {

        this.principal = principal;
    }


    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productID);
        dest.writeString(this.productName);
        dest.writeString(this.productPhoto);
        dest.writeString(this.productDescription);
        dest.writeString(this.categoryID);
        dest.writeInt(this.outletProductPrice);
        dest.writeInt(this.outletProductStockQty);
        dest.writeInt(this.cartProductPrice);
        dest.writeInt(this.cartProductQty);
        dest.writeString(this.indikasi);
        dest.writeString(this.kandungan);
        dest.writeString(this.dosis);
        dest.writeString(this.carapakai);
        dest.writeString(this.kemasan);
        dest.writeString(this.golongan);
        dest.writeString(this.resepYN);
        dest.writeString(this.kontraindikasi);
        dest.writeString(this.carasimpan);
        dest.writeString(this.principal);
    }

    protected obat(Parcel in) {
        this.productID = in.readString();
        this.productName = in.readString();
        this.productPhoto = in.readString();
        this.productDescription = in.readString();
        this.categoryID = in.readString();
        this.outletProductPrice = in.readInt();
        this.outletProductStockQty = in.readInt();
        this.cartProductPrice = in.readInt();
        this.cartProductQty = in.readInt();
        this.indikasi = in.readString();
        this.kandungan = in.readString();
        this.dosis = in.readString();
        this.carapakai = in.readString();
        this.kemasan = in.readString();
        this.golongan = in.readString();
        this.resepYN = in.readString();
        this.kontraindikasi = in.readString();
        this.carasimpan = in.readString();
        this.principal = in.readString();
    }

    public static final Parcelable.Creator<obat> CREATOR = new Parcelable.Creator<obat>() {
        @Override
        public obat createFromParcel(Parcel source) {
            return new obat(source);
        }

        @Override
        public obat[] newArray(int size) {
            return new obat[size];
        }
    };
}
