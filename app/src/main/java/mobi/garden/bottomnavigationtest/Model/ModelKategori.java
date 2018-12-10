package mobi.garden.bottomnavigationtest.Model;

public class ModelKategori {
    public String CategoryName;
    public boolean isChecked;

    public ModelKategori() {
    }

    public ModelKategori(String categoryName) {
        CategoryName = categoryName;
    }
    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
        isChecked = false;
    }

    public String getCategoryName() {
        return CategoryName;
    }
}
