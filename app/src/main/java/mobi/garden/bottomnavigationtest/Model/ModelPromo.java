package mobi.garden.bottomnavigationtest.Model;

public class ModelPromo {

    String PromoNameProduct, ProductNameUrl;
    int PriceProduct, ProductPriceAfterDC;
    public ModelPromo(String promoNameProduct, String productNameUrl, int priceProduct,int productPriceAfterDC) {
        this.PromoNameProduct = promoNameProduct;
        this.ProductNameUrl = productNameUrl;
        this.PriceProduct = priceProduct;
        this.ProductPriceAfterDC = productPriceAfterDC;
    }

    public int getProductPriceAfterDC() {
        return ProductPriceAfterDC;
    }

    public void setProductPriceAfterDC(int productPriceAfterDC) {
        ProductPriceAfterDC = productPriceAfterDC;
    }

    public int getPriceProduct() {
        return PriceProduct;
    }
    public void setPriceProduct(int priceProduct) {
        PriceProduct = priceProduct;
    }

    public String getProductNameUrl() {
        return ProductNameUrl;
    }
    public void setProductNameUrl(String productNameUrl) {
        this.ProductNameUrl = productNameUrl;
    }

    public ModelPromo() {

    }


    public String getPromoNameProduct() {
        return PromoNameProduct;
    }

    public void setPromoNameProduct(String promoNameProduct) {
        this.PromoNameProduct = promoNameProduct;
    }
}
