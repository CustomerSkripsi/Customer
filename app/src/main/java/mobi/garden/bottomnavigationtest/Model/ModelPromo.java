package mobi.garden.bottomnavigationtest.Model;

public class ModelPromo {

    public String ProductID,NameProduct, ProductNameUrl,AlamatApotek;
    public int PriceProduct, ProductPriceAfterDC, ProductQty, StockProductQty;


    public ModelPromo(String productID, int priceProduct, String nameProduct, int productQty, int stockProductQty) {
        ProductID = productID;
        PriceProduct = priceProduct;
        NameProduct = nameProduct;
        ProductQty = productQty;
        StockProductQty =stockProductQty;
    }

    public ModelPromo(String promoNameProduct, String productNameUrl, int priceProduct, int productPriceAfterDC) {
        this.NameProduct = promoNameProduct;
        this.ProductNameUrl = productNameUrl;
        this.PriceProduct = priceProduct;
        this.ProductPriceAfterDC = productPriceAfterDC;
    }




    public ModelPromo(String productID, String promoNameProduct, String productNameUrl, int productPriceAfterDC) {
        ProductID = productID;
        NameProduct = promoNameProduct;
        ProductNameUrl = productNameUrl;
        ProductPriceAfterDC = productPriceAfterDC;
    }

    public ModelPromo(String promoNameProduct, String productNameUrl, int priceProduct) {
        NameProduct = promoNameProduct;
        ProductNameUrl = productNameUrl;
        PriceProduct = priceProduct;
    }

    public ModelPromo(String promoNameProduct, String productNameUrl) {
        this.NameProduct = promoNameProduct;
        this.ProductNameUrl = productNameUrl;
    }

    public int getProductPriceAfterDC() {
        return ProductPriceAfterDC;
    }

    public void setProductPriceAfterDC(int productPriceAfterDC) {
        ProductPriceAfterDC = productPriceAfterDC;
    }


    public ModelPromo() {

    }

    public String getProductID() {
        return ProductID;
    }
    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getNameProduct() {
        return NameProduct;
    }
    public void setNameProduct(String nameProduct) {
        NameProduct = nameProduct;
    }

    public int getPriceProduct() {
        return PriceProduct;
    }
    public void setPriceProduct(int priceProduct) {
        PriceProduct = priceProduct;
    }

    public int getProductQty() {
        return ProductQty;
    }
    public void setProductQty(int productQty) {
        ProductQty = productQty;
    }

    public String getProductNameUrl() {
        return ProductNameUrl;
    }
    public void setProductNameUrl(String productNameUrl) {
        this.ProductNameUrl = productNameUrl;
    }

    public String getPromoNameProduct() {
        return NameProduct;
    }
    public void setPromoNameProduct(String promoNameProduct) {
        this.NameProduct = promoNameProduct;
    }

    public String getAlamatApotek() {
        return AlamatApotek;
    }

    public void setAlamatApotek(String alamatApotek) {
        AlamatApotek = alamatApotek;
    }

}
