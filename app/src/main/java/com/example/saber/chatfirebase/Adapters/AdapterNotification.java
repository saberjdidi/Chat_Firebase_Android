package com.example.saber.chatfirebase.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saber.chatfirebase.Models.ModelNotification;
import com.example.saber.chatfirebase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.HolderNotification> {

    Context context;
    ArrayList<ModelNotification> modelNotificationList;

    public AdapterNotification(Context context, ArrayList<ModelNotification> modelNotificationList) {
        this.context = context;
        this.modelNotificationList = modelNotificationList;
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

        //convert timestamp to date
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
      //set to view
        holder.nameTv.setText(name);
        holder.notificationTv.setText(notification);
        holder.timeTv.setText(pTime);

        try {
            Picasso.get().load(image).placeholder(R.drawable.ic_default_img).into(holder.avatarIv);
        } catch (Exception e){
           holder.avatarIv.setImageResource(R.drawable.ic_default_img);
        }
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
