package com.example.eventplanner.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Service implements Parcelable {

    private Long id;
    private Long categoryId;
    private Long subcategoryId;
    private String name;
    private String description;
    private ArrayList<Uri> images;
    private String specific;
    private Double pricePerHour;
    private Double fullPrice;
    private Double duration;
    private Double durationMin;
    private Double durationMax;
    private String location;
    private Double discount;
    private ArrayList<String> providers;
    private ArrayList<Long> eventIds;
    private String reservationDue;
    private String cancelationDue;
    private Boolean automaticAffirmation;
    private Boolean available;
    private Boolean visible;

    public Service() {
    }

    public Service(Long id, Long categoryId, Long subcategoryId, String name, String description, ArrayList<Uri> images, String specific, Double pricePerHour, Double fullPrice, Double duration, Double durationMin, Double durationMax, String location, Double discount, ArrayList<String> providers, ArrayList<Long> eventIds, String reservationDue, String cancelationDue, Boolean automaticAffirmation, Boolean available, Boolean visible) {
        this.id = id;
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.name = name;
        this.description = description;
        this.images = images;
        this.specific = specific;
        this.pricePerHour = pricePerHour;
        this.fullPrice = fullPrice;
        this.duration = duration;
        this.durationMin = durationMin;
        this.durationMax = durationMax;
        this.location = location;
        this.discount = discount;
        this.providers = providers;
        this.eventIds = eventIds;
        this.reservationDue = reservationDue;
        this.cancelationDue = cancelationDue;
        this.automaticAffirmation = automaticAffirmation;
        this.available = available;
        this.visible = visible;
    }

    protected Service(Parcel in) {
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
        images = new ArrayList<>();
        in.readList(images, Integer.class.getClassLoader());
        specific = in.readString();
        if (in.readByte() == 0) {
            pricePerHour = null;
        } else {
            pricePerHour = in.readDouble();
        }
        if (in.readByte() == 0) {
            fullPrice = null;
        } else {
            fullPrice = in.readDouble();
        }
        if (in.readByte() == 0) {
            duration = null;
        } else {
            duration = in.readDouble();
        }
        if (in.readByte() == 0) {
            durationMin = null;
        } else {
            durationMin = in.readDouble();
        }
        if (in.readByte() == 0) {
            durationMax = null;
        } else {
            durationMax = in.readDouble();
        }
        location = in.readString();
        if (in.readByte() == 0) {
            discount = null;
        } else {
            discount = in.readDouble();
        }
        providers = in.createStringArrayList();
        eventIds = new ArrayList<>();
        in.readList(eventIds, Long.class.getClassLoader());
        reservationDue = in.readString();
        cancelationDue = in.readString();
        byte tmpAutomaticAffirmation = in.readByte();
        automaticAffirmation = tmpAutomaticAffirmation == 0 ? null : tmpAutomaticAffirmation == 1;
        byte tmpAvailable = in.readByte();
        available = tmpAvailable == 0 ? null : tmpAvailable == 1;
        byte tmpVisible = in.readByte();
        visible = tmpVisible == 0 ? null : tmpVisible == 1;
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
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
        dest.writeList(images);
        dest.writeString(specific);
        if (pricePerHour == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(pricePerHour);
        }
        if (fullPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(fullPrice);
        }
        if (duration == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(duration);
        }
        if (durationMin == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(durationMin);
        }
        if (durationMax == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(durationMax);
        }
        dest.writeString(location);
        if (discount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(discount);
        }
        dest.writeStringList(providers);
        dest.writeList(eventIds);
        dest.writeString(reservationDue);
        dest.writeString(cancelationDue);
        dest.writeByte((byte) (automaticAffirmation == null ? 0 : automaticAffirmation ? 1 : 2));
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

    public ArrayList<Uri> getImages() {
        return images;
    }

    public void setImages(ArrayList<Uri> images) {
        this.images = images;
    }

    public String getSpecific() {
        return specific;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Double getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(Double fullPrice) {
        this.fullPrice = fullPrice;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(Double durationMin) {
        this.durationMin = durationMin;
    }

    public Double getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(Double durationMax) {
        this.durationMax = durationMax;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public ArrayList<String> getProviders() {
        return providers;
    }

    public void setProviders(ArrayList<String> providers) {
        this.providers = providers;
    }

    public ArrayList<Long> getEventIds() {
        return eventIds;
    }

    public void setEventIds(ArrayList<Long> eventIds) {
        this.eventIds = eventIds;
    }

    public String getReservationDue() {
        return reservationDue;
    }

    public void setReservationDue(String reservationDue) {
        this.reservationDue = reservationDue;
    }

    public String getCancelationDue() {
        return cancelationDue;
    }

    public void setCancelationDue(String cancelationDue) {
        this.cancelationDue = cancelationDue;
    }

    public Boolean getAutomaticAffirmation() {
        return automaticAffirmation;
    }

    public void setAutomaticAffirmation(Boolean automaticAffirmation) {
        this.automaticAffirmation = automaticAffirmation;
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
