package com.example.donation_app.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donation_app.R;
import com.example.donation_app.model.Item;
import com.example.donation_app.adapter.ItemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class DonorDashboardActivity extends ComponentActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private Button btnLogout, btnUploadItem;

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
        auth = FirebaseAuth.getInstance();

        loadMyDonations();

        btnUploadItem.setOnClickListener(v -> {
            startActivity(new Intent(this, UploadItemActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void loadMyDonations() {
        String userId = auth.getCurrentUser().getUid();
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
                });
    }
}
