package com.example.donation_app.view;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.donation_app.R;
import com.example.donation_app.model.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadItemActivity extends AppCompatActivity {

    private EditText etName, etDescription, etQuantity;
    private Button btnUpload;
    private DatabaseReference itemRef;
    private String donorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        etName = findViewById(R.id.etItemName);
        etDescription = findViewById(R.id.etItemDescription);
        etQuantity = findViewById(R.id.etItemQuantity);
        btnUpload = findViewById(R.id.btnSubmitItem);

        donorId = getIntent().getStringExtra("userId");
        itemRef = FirebaseDatabase.getInstance().getReference("items");

        btnUpload.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String quantityStr = etQuantity.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Quantity must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            String itemId = itemRef.push().getKey();
            Item item = new Item(itemId, name, description, donorId, quantity, "");

            itemRef.child(itemId).setValue(item)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Item uploaded!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
