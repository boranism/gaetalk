package com.example.gaetalk;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gaetalk.adapter.CustomAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Recy_item extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<MemberInfo> arrayList;
    private CustomAdapter customAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recy_item);
        recyclerView = findViewById(R.id.recylclerView);
        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customAdapter = new CustomAdapter(arrayList, this);
        recyclerView.setAdapter(customAdapter);

        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        MemberInfo m = d.toObject(MemberInfo.class);
                        arrayList.add(m);
                    }
                    customAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "데이터를 찾을수없습니다", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "데이터를 가져오는데 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
