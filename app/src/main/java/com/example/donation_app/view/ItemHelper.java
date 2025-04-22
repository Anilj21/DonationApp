package com.example.donation_app.view;

public class ItemHelper {
    public String id;
    public String name;
    public String description;
    public String donorId;

    // Required empty constructor
    public ItemHelper() {}

    public ItemHelper(String id, String name, String description, String donorId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.donorId = donorId;
    }
}
