package com.example.donation_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donation_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectText = findViewById(R.id.goToRegister);

        loginButton.setOnClickListener(v -> {
            if (!validateEmail() | !validatePassword()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else {
                checkUser();
            }
        });

        signupRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private Boolean validateEmail() {
        String val = emailInput.getText().toString();
        if (val.isEmpty()) {
            emailInput.setError("Email cannot be empty");
            return false;
        } else {
            emailInput.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = passwordInput.getText().toString();
        if (val.isEmpty()) {
            passwordInput.setError("Password cannot be empty");
            return false;
        } else {
            passwordInput.setError(null);
            return true;
        }
    }

    private void checkUser() {
        String userEmail = emailInput.getText().toString().trim();
        String userPassword = passwordInput.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("email").equalTo(userEmail);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                        String role = userSnapshot.child("role").getValue(String.class);
                        if (Objects.equals(passwordFromDB, userPassword)) {
                            // Login success
                            String usernameKey = userSnapshot.getKey(); // e.g., "anil"

                            Intent intent = new Intent(LoginActivity.this, DonorDashboardActivity.class);
                            intent.putExtra("userId", usernameKey); // pass the ID
                            startActivity(intent);
                            finish();
                            return;
                        } else {
                            passwordInput.setError("Invalid password!");
                            passwordInput.requestFocus();
                            return;
                        }
                    }
                } else {
                    emailInput.setError("User does not exist");
                    emailInput.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
