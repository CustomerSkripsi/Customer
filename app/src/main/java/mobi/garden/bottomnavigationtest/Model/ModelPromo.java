package mobi.garden.bottomnavigationtest.Model;

public class ModelPromo {
    public String PromoNameProduct, ProductNameUrl;

    public int getPriceProduct() {
        return PriceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        PriceProduct = priceProduct;
    }

    public int PriceProduct;

    public String getProductNameUrl() {
        return ProductNameUrl;
    }

    public void setProductNameUrl(String productNameUrl) {
        ProductNameUrl = productNameUrl;
    }

    public ModelPromo() {

    }

    public ModelPromo(String promoNameProduct) {
        PromoNameProduct = promoNameProduct;
    }

    public String getPromoNameProduct() {
        return PromoNameProduct;
    }

    public void setPromoNameProduct(String promoNameProduct) {
        PromoNameProduct = promoNameProduct;
    }
}
