package com.example.donation_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donation_app.R;
import com.example.donation_app.adapter.ItemAdapter;
import com.example.donation_app.model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class DonorDashboardActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private Button btnLogout, btnUploadItem;
    private String userId;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_dashboard);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerViewDonations);
        btnLogout = findViewById(R.id.btnLogout);
        btnUploadItem = findViewById(R.id.btnUploadItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(itemAdapter);

        userId = getIntent().getStringExtra("userId");

        if (userId == null) {
            Toast.makeText(this, "User ID missing. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        loadMyDonations(); // initial load

        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadMyDonations();
        });

        btnUploadItem.setOnClickListener(v -> {
            Intent uploadIntent = new Intent(this, UploadItemActivity.class);
            uploadIntent.putExtra("userId", userId);
            startActivity(uploadIntent);
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyDonations(); // reload on return to dashboard
    }

    private void loadMyDonations() {
        swipeRefreshLayout.setRefreshing(true); // show loader
        FirebaseDatabase.getInstance().getReference("items")
                .orderByChild("donorId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        itemList.clear();
                        for (DataSnapshot itemSnap : snapshot.getChildren()) {
                            Item item = itemSnap.getValue(Item.class);
                            itemList.add(item);
                        }
                        itemAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false); // stop loader
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(DonorDashboardActivity.this, "Error loading items: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false); // stop loader
                    }
                });
    }

}
