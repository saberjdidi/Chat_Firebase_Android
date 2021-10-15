package com.example.saber.chatfirebase.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.saber.chatfirebase.Adapters.AdapterUsers;
import com.example.saber.chatfirebase.Models.User;
import com.example.saber.chatfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostLikedByActivity extends AppCompatActivity {

    String postId;
    private RecyclerView recyclerView;
    private List<User> userList;
    private AdapterUsers adapterUsers;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_liked_by);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post Liked By");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        actionBar.setSubtitle(auth.getCurrentUser().getEmail());

        recyclerView = findViewById(R.id.recyclerView);

        //get id of post use intent
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");

        userList = new ArrayList<>();
        //get the list of users who liked the post
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Likes");
        ref.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              userList.clear();
              for (DataSnapshot ds : dataSnapshot.getChildren()){
                  String hisUid = ""+ds.getRef().getKey();

                  //get user info from each id
                  getUsers(hisUid);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUsers(String hisUid) {
        //get information of each user using uid
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(hisUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                     User user = ds.getValue(User.class);
                     userList.add(user);
                }
                adapterUsers = new AdapterUsers(PostLikedByActivity.this, userList);
                recyclerView.setAdapter(adapterUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
