package com.example.donation_app.view;

import android.os.Bundle;
import android.widget.*;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donation_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UploadItemActivity extends ComponentActivity {

    private EditText etName, etDescription;
    private Button btnUpload;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        etName = findViewById(R.id.etItemName);
        etDescription = findViewById(R.id.etItemDescription);
        btnUpload = findViewById(R.id.btnSubmitItem);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnUpload.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String donorId = auth.getCurrentUser().getUid();

            if (!name.isEmpty() && !description.isEmpty()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", name);
                item.put("description", description);
                item.put("donorId", donorId);
                item.put("imageUrl", ""); // optional, or implement later

                db.collection("items")
                        .add(item)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Item uploaded!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload item.", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
