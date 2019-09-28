package com.arunsinghsaab98.project_feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arunsinghsaab98.project_feedback.ViewHolder.RecyclerHolder;
import com.arunsinghsaab98.project_feedback.model.RVContentDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class AdminPanel extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<RVContentDetails> options;
    private FirebaseRecyclerAdapter<RVContentDetails,RecyclerHolder> adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Complaints").child("JaYCN6T87AV9Tyl2hKpp635d7nP2");
        databaseReference.keepSynced(true);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        options = new FirebaseRecyclerOptions.Builder<RVContentDetails>().setQuery(databaseReference,RVContentDetails.class).build();

        adapter = new FirebaseRecyclerAdapter<RVContentDetails, RecyclerHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerHolder recyclerHolder, int i, @NonNull RVContentDetails rvContentDetails) {

                recyclerHolder.userName.setText(rvContentDetails.getUser_name());
                Picasso.with(getApplicationContext())
                        .load(rvContentDetails.getImageUrl())
                        .into(recyclerHolder.imageView);


            }


            @NonNull
            @Override
            public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RecyclerHolder(LayoutInflater.from(AdminPanel.this).inflate(R.layout.content_detail,parent,false));
            }
        };


        mRecyclerView.setAdapter(adapter);




    }

}
