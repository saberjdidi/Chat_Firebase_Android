package com.example.saber.chatfirebase.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saber.chatfirebase.Adapters.AdapterNotification;
import com.example.saber.chatfirebase.Models.ModelNotification;
import com.example.saber.chatfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    RecyclerView recyclerView;
    private ArrayList<ModelNotification> notificationsList;
    private AdapterNotification adapterNotification;

    private FirebaseAuth firebaseAuth;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.notificationsRc);

        firebaseAuth = FirebaseAuth.getInstance();

        getAllNotifications();

        return view;
    }

    private void getAllNotifications() {
        notificationsList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Notifications")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      notificationsList.clear();
                      for (DataSnapshot ds : dataSnapshot.getChildren()){
                          //get data
                          ModelNotification model = ds.getValue(ModelNotification.class);
                          notificationsList.add(model);
                      }
                      adapterNotification = new AdapterNotification(getActivity(), notificationsList);
                      recyclerView.setAdapter(adapterNotification);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
