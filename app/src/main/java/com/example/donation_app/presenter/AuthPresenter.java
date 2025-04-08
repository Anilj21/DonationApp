package com.example.donation_app.presenter;


import androidx.annotation.NonNull;

import com.example.donation_app.contract.AuthContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthPresenter implements AuthContract.Presenter {

    private final AuthContract.View view;
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    public AuthPresenter(AuthContract.View view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        view.onLoginSuccess();
                    } else {
                        view.onLoginFailure("Login failed. Please check credentials.");
                    }
                });
    }

    @Override
    public void register(String name, String email, String password, String role, boolean isVolunteer) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            Map<String, Object> user = new HashMap<>();
                            user.put("id", userId);
                            user.put("name", name);
                            user.put("email", email);
                            user.put("role", role); // "donor" or "ngo"
                            user.put("isVolunteer", isVolunteer);

                            db.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener(unused -> view.onRegisterSuccess())
                                    .addOnFailureListener(e -> view.onRegisterFailure("Registration failed."));
                        }
                    } else {
                        view.onRegisterFailure("Authentication failed.");
                    }
                });
    }
}
