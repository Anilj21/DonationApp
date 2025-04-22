package com.example.donation_app.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class NGOActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo); // Ensure this layout exists

        recyclerView = findViewById(R.id.recyclerViewItems); // Add this ID in your layout XML
        progressBar = findViewById(R.id.progressBar);         // Add this ID in your layout XML
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, itemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        loadDonationItems();
    }

    private void loadDonationItems() {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference("items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        itemList.clear();
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            Item item = itemSnapshot.getValue(Item.class);
                            if (item != null) {
                                itemList.add(item);
                            }
                        }
                        itemAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(NGOActivity.this, "Failed to load items.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
