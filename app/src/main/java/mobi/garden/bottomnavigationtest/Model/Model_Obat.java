package mobi.garden.bottomnavigationtest.Model;

public class Model_Obat {
    public String ProductID,ProductName,productPhoto, productDescription, indikasi, kandungan, dosis, carapakai, kemasan,
    golongan, resepYN, kontraindikasi,  carasimpan, principal,categoryID;

    public int productPrice, productDiscount, productPriceAfterdc;

    public Model_Obat(String ProductID, String ProductName, String productPhoto, String productDescription,
                      String indikasi, String kandungan, String dosis, String carapakai, String kemasan,
                      String golongan, String resepYN, String kontraindikasi, String carasimpan, String principal,String categoryID) {
        this.ProductID = ProductID;
        this.ProductName = ProductName;
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

    public Model_Obat(String ProductID, String ProductName, String productPhoto, int productPrice, int productPriceAfterDc, int productDiscount) {
        this.ProductID = ProductID;
        this.ProductName = ProductName;
        this.productPhoto = productPhoto;
        this.productPrice = productPrice;
        this.productPriceAfterdc = productPriceAfterDc;
        this.productDiscount = productDiscount;
    }


    public String getProductID() {

        return ProductID;
    }

    public void setProductID(String ProductID) {

        this.ProductID = ProductID;
    }

    public String getProductName() {

        return ProductName;
    }

    public void setProductName(String ProductName) {

        this.ProductName = ProductName;
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

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(int productDiscount) {
        this.productDiscount = productDiscount;
    }

    public int getProductPriceAfterdc() {
        return productPriceAfterdc;
    }

    public void setProductPriceAfterdc(int productPriceAfterdc) {
        this.productPriceAfterdc = productPriceAfterdc;
    }
}
