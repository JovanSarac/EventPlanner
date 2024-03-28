package model;


public class Subcategory {
    public Long id;

    public Long CategoryId;
    public String NameCategory;
    public String Name;
    public String Description;

    public enum Type{
        PRODUCT,
        SERVICE
    }

    public Type TypeSubcategory;

    @Override
    public String toString() {
        return "Subcategory{" +
                "id=" + id +
                ", CategoryId=" + CategoryId +
                ", NameCategory='" + NameCategory + '\'' +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", TypeSubcategory=" + TypeSubcategory +
                '}';
    }

    public Subcategory(Long id, Long categoryId, String nameCategory, String name, String description, Type typeSubcategory) {
        this.id = id;
        CategoryId = categoryId;
        NameCategory = nameCategory;
        Name = name;
        Description = description;
        TypeSubcategory = typeSubcategory;
    }

    public Subcategory(String nameCategory, String name, String description, Type typeSubcategory) {
        NameCategory = nameCategory;
        Name = name;
        Description = description;
        TypeSubcategory = typeSubcategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(Long categoryId) {
        CategoryId = categoryId;
    }

    public String getNameCategory() {
        return NameCategory;
    }

    public void setNameCategory(String nameCategory) {
        NameCategory = nameCategory;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTypeSubcategory() {
        if(TypeSubcategory == Type.PRODUCT){
            return "Product";
        }else{
            return "Service";
        }

    }

    public void setTypeSubcategory(Type typeSubcategory) {
        TypeSubcategory = typeSubcategory;
    }
}
