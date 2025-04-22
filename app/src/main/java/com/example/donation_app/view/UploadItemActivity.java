package com.example.donation_app.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.donation_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadItemActivity extends AppCompatActivity {

    private EditText etName, etDescription;
    private Button btnUpload;
    private DatabaseReference itemsRef;
    private String donorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        // 1) find views
        etName        = findViewById(R.id.etItemName);
        etDescription = findViewById(R.id.etItemDescription);
        btnUpload     = findViewById(R.id.btnSubmitItem);

        // 2) get donorId from Intent
        donorId = getIntent().getStringExtra("userId");
        if (donorId == null || donorId.isEmpty()) {
            Toast.makeText(this, "User ID missing. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 3) get a reference to /items
        itemsRef = FirebaseDatabase.getInstance()
                .getReference("items");

        // 4) set click listener
        btnUpload.setOnClickListener(v -> {
            String name        = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // 5) generate a new push ID
            String itemId = itemsRef.push().getKey();
            if (itemId == null) {
                Toast.makeText(this, "Error generating item ID", Toast.LENGTH_SHORT).show();
                return;
            }

            // 6) build ItemHelper and write to DB
            ItemHelper item = new ItemHelper(itemId, name, description, donorId);
            itemsRef.child(itemId).setValue(item)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Item uploaded!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }
}
