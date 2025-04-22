package com.example.donation_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donation_app.R;
import com.example.donation_app.adapter.ItemAdapter;
import com.example.donation_app.model.Item;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DonorDashboardActivity extends androidx.appcompat.app.AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private FirebaseFirestore db;

    private Button btnLogout, btnUploadItem;
    private String userId; // from Intent

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_dashboard);

        recyclerView = findViewById(R.id.recyclerViewDonations);
        btnLogout = findViewById(R.id.btnLogout);
        btnUploadItem = findViewById(R.id.btnUploadItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(itemAdapter);

        db = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Toast.makeText(this, "User ID missing. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        loadMyDonations();

        btnUploadItem.setOnClickListener(v -> {
            Intent uploadIntent = new Intent(this, UploadItemActivity.class);
            uploadIntent.putExtra("userId", userId); // pass userId if needed
            startActivity(uploadIntent);
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void loadMyDonations() {
        db.collection("items")
                .whereEqualTo("donorId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Item item = doc.toObject(Item.class);
                        itemList.add(item);
                    }
                    itemAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error loading donations: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
