package model;

public class SubcategoryPlanner {
    private String serialNum;
    private String NameCategory;
    private String NameSubcategory;
    private String Price;

    public SubcategoryPlanner(){

    }

    public SubcategoryPlanner(String serialNum, String nameCategory, String nameSubcategory, String price) {
        this.serialNum = serialNum;
        NameCategory = nameCategory;
        NameSubcategory = nameSubcategory;
        Price = price;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getNameCategory() {
        return NameCategory;
    }

    public void setNameCategory(String nameCategory) {
        NameCategory = nameCategory;
    }

    public String getNameSubcategory() {
        return NameSubcategory;
    }

    public void setNameSubcategory(String nameSubcategory) {
        NameSubcategory = nameSubcategory;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
