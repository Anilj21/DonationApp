package com.example.donation_app.presenter;

import android.util.Log;

import com.example.donation_app.contract.AuthContract;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AuthPresenter implements AuthContract.Presenter {

    private final AuthContract.View view;
    private final DatabaseReference db;

    public AuthPresenter(AuthContract.View view) {
        this.view = view;
        this.db = FirebaseDatabase.getInstance().getReference(); // Get the Realtime Database reference
    }

    public AuthPresenter(AuthContract.View view, DatabaseReference db) {
        this.view = view;
        this.db = db;
    }

    @Override
    public void login(String email, String password) {
        // For Realtime Database, you could store users by email as keys and passwords as values,
        // but it's not recommended to store passwords in plaintext.
        // Here, we'll demonstrate a simple lookup. In production, consider encrypting passwords.
        db.child("users").orderByChild("email").equalTo(email)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            // Check if password matches
                            String storedPassword = task.getResult().child("password").getValue(String.class);
                            if (storedPassword != null && storedPassword.equals(password)) {
                                view.onLoginSuccess();
                            } else {
                                view.onLoginFailure("Incorrect password.");
                            }
                        } else {
                            view.onLoginFailure("User not found.");
                        }
                    } else {
                        view.onLoginFailure("Login failed: " + task.getException().getMessage());
                    }
                });
    }

    @Override
    public void register(String name, String email, String password, String role, boolean isVolunteer) {
        // Using a unique user ID based on email hash or timestamp or a simple incremented counter for simplicity
        String userId = db.push().getKey(); // Generate a unique user ID
        if (userId != null) {
            Map<String, Object> user = new HashMap<>();
            user.put("id", userId);
            user.put("name", name);
            user.put("email", email);
            user.put("password", password); // Store password (again, consider hashing this in real-world scenarios)
            user.put("role", role); // "donor" or "ngo"
            user.put("isVolunteer", isVolunteer);

            // Save user data under the generated userId
            db.child("users").child(userId).setValue(user)
                    .addOnSuccessListener(unused -> {
                        view.onRegisterSuccess();
                        Log.d("AuthPresenter", "Register success callback triggered");
                    })
                    .addOnFailureListener(e -> {
                        view.onRegisterFailure("Realtime DB save failed: " + e.getMessage());
                    });
        } else {
            view.onRegisterFailure("Failed to generate user ID.");
        }
    }
}
