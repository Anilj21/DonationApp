package com.example.donation_app.model;

public class Item {
    private String name;
    private String description;
    private String donorId;
    private String imageUrl;

    public Item() {} // Required for Firebase

    public Item(String name, String description, String donorId, String imageUrl) {
        this.name = name;
        this.description = description;
        this.donorId = donorId;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDonorId() { return donorId; }
    public String getImageUrl() { return imageUrl; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setDonorId(String donorId) { this.donorId = donorId; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
