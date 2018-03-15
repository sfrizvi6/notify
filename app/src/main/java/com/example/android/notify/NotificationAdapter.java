package com.example.android.notify;


import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationItemModel> notificationList;
    private Context context;

    NotificationAdapter(List<NotificationItemModel> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext())
                                                        .inflate(R.layout.notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        NotificationItemModel notificationItemModel = notificationList.get(position);
        try {
            Resources res =
                context.getPackageManager().getResourcesForApplication(notificationItemModel.packageName);
            Drawable icon = res.getDrawable(notificationItemModel.imageId);
            holder.notificationImage.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        holder.notificationPackageName.setText(notificationItemModel.packageName);
        holder.notificationTitle.setText(notificationItemModel.title);
        holder.notificationText.setText(notificationItemModel.text);
        holder.notificationCard.setCardBackgroundColor(Color.LTGRAY);
    }

    @Override
    public int getItemCount() {
        return notificationList == null ? 0 : notificationList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        CardView notificationCard;
        ImageView notificationImage;
        TextView notificationPackageName;
        TextView notificationTitle;
        TextView notificationText;

        NotificationViewHolder(View itemView) {
            super(itemView);
            notificationCard = itemView.findViewById(R.id.notification_card);
            notificationImage = itemView.findViewById(R.id.notification_image);
            notificationPackageName = itemView.findViewById(R.id.notification_package_name);
            notificationTitle = itemView.findViewById(R.id.notification_title);
            notificationText = itemView.findViewById(R.id.notification_text);
        }
    }
}
