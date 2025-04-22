package com.example.donation_app.model;

public class Item {
    private String id;
    private String name;
    private String description;
    private String donorId;
    private int quantity;
    private String imageUrl;

    public Item() {
        // Required for Firebase
    }

    public Item(String id, String name, String description, String donorId, int quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.donorId = donorId;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDonorId() { return donorId; }
    public void setDonorId(String donorId) { this.donorId = donorId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
