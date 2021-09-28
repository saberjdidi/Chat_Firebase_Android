package com.example.saber.chatfirebase.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saber.chatfirebase.Activities.PostDetailActivity;
import com.example.saber.chatfirebase.Models.ModelNotification;
import com.example.saber.chatfirebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.HolderNotification> {

    Context context;
    ArrayList<ModelNotification> modelNotificationList;

    private FirebaseAuth firebaseAuth;

    public AdapterNotification(Context context, ArrayList<ModelNotification> modelNotificationList) {
        this.context = context;
        this.modelNotificationList = modelNotificationList;

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HolderNotification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_notification, parent, false);

        return new HolderNotification(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNotification holder, int position) {
        //get data
      ModelNotification modelNotification = modelNotificationList.get(position);
      String name = modelNotification.getsName();
      String notification = modelNotification.getNotification();
      String image = modelNotification.getsImage();
      String timeStamp = modelNotification.getTimestamp();
      String senderUid = modelNotification.getsUid();
      String pId = modelNotification.getpId();

        //convert timestamp to date
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

        //get name, email, image of user of notification from his uid
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(senderUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      for (DataSnapshot ds : dataSnapshot.getChildren()){
                          String name = ""+ds.child("name").getValue();
                          String email = ""+ds.child("email").getValue();
                          String image = ""+ds.child("image").getValue();

                          //add to model
                          modelNotification.setsName(name);
                          modelNotification.setsEmail(email);
                          modelNotification.setsImage(image);

                          //set to view
                          holder.nameTv.setText(name);
                          try {
                              Picasso.get().load(image).placeholder(R.drawable.ic_default_img).into(holder.avatarIv);
                          } catch (Exception e){
                              holder.avatarIv.setImageResource(R.drawable.ic_default_img);
                          }
                      }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        holder.notificationTv.setText(notification);
        holder.timeTv.setText(pTime);

        //click notification to open post
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//start PostDetailActivity
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("postId", pId); //get detail of post use id
                context.startActivity(intent);
            }
        });
        //long press to show delete notification option
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new  AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this Notification");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                        ref.child(firebaseAuth.getUid()).child("Notifications").child(timeStamp)
                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Notification deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                    }
                });
                builder.create().show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelNotificationList.size();
    }

    class HolderNotification extends RecyclerView.ViewHolder{

        ImageView avatarIv;
        TextView nameTv, notificationTv, timeTv;
        public HolderNotification(@NonNull View itemView) {
            super(itemView);

            avatarIv = itemView.findViewById(R.id.avatarIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            notificationTv = itemView.findViewById(R.id.notificationTv);
            timeTv = itemView.findViewById(R.id.timeTv);
        }
    }
}
