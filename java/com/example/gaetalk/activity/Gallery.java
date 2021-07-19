/*
package com.example.gaetalk.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gaetalk.R;
import com.example.gaetalk.adapter.GalleryAdapter;

public class Gallery extends AppCompatActivity {
    private RecyclerView recylclerView1;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        recylclerView1 = findViewById(R.id.recylclerView);

        recylclerView1.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recylclerView1.setLayoutManager(layoutManager);

        String[] myDataset={"1", "2", "3", "4"};
        mAdapter = new GalleryAdapter(myDataset);
        recylclerView1.setAdapter(mAdapter);
    }
}*/
