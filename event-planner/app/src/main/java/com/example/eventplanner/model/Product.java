package com.example.eventplanner.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Product implements Parcelable {
    private Long id;
    private Long categoryId;
    private Long subcategoryId;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private ArrayList<Uri> images;
    private ArrayList<Long> eventIds;
    private Boolean available;
    private Boolean visible;

    public Product() {
    }

    public Product(Long id, Long categoryId, Long subcategoryId, String name, String description, Double price, Double discount, ArrayList<Uri> images, ArrayList<Long> eventIds, Boolean available, Boolean visible) {
        this.id = id;
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.images = images;
        this.eventIds = eventIds;
        this.available = available;
        this.visible = visible;
    }

    protected Product(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            categoryId = null;
        } else {
            categoryId = in.readLong();
        }
        if (in.readByte() == 0) {
            subcategoryId = null;
        } else {
            subcategoryId = in.readLong();
        }
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        if (in.readByte() == 0) {
            discount = null;
        } else {
            discount = in.readDouble();
        }
        images = new ArrayList<>();
        in.readList(images, Uri.class.getClassLoader());
        eventIds = new ArrayList<>();
        in.readList(eventIds, Long.class.getClassLoader());
        byte tmpAvailable = in.readByte();
        available = tmpAvailable == 0 ? null : tmpAvailable == 1;
        byte tmpVisible = in.readByte();
        visible = tmpVisible == 0 ? null : tmpVisible == 1;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        if (categoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(categoryId);
        }
        if (subcategoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(subcategoryId);
        }
        dest.writeString(name);
        dest.writeString(description);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        if (discount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(discount);
        }
        dest.writeList(images);
        dest.writeList(eventIds);
        dest.writeByte((byte) (available == null ? 0 : available ? 1 : 2));
        dest.writeByte((byte) (visible == null ? 0 : visible ? 1 : 2));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public ArrayList<Uri> getImages() {
        return images;
    }

    public void setImages(ArrayList<Uri> images) {
        this.images = images;
    }

    public ArrayList<Long> getEventIds() {
        return eventIds;
    }

    public void setEventIds(ArrayList<Long> eventIds) {
        this.eventIds = eventIds;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
