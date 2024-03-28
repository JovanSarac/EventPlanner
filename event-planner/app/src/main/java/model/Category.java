package model;

public class Category {
    public Long id;
    public String Name;
    public String Description;

    public Category(Long id, String name, String description) {
        this.id = id;
        Name = name;
        Description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
