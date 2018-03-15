package com.example.android.notify;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationItemModel> notificationList;

    NotificationAdapter(List<NotificationItemModel> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext())
                                                        .inflate(R.layout.notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        NotificationItemModel notificationItemModel = notificationList.get(position);
        holder.notificationImage.setImageResource(notificationItemModel.imageId);
        holder.notificationPackageName.setText(notificationItemModel.packageName);
        holder.notificationTitle.setText(notificationItemModel.title);
        holder.notificationText.setText(notificationItemModel.text);
    }

    @Override
    public int getItemCount() {
        return notificationList == null ? 0 : notificationList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        ImageView notificationImage;
        TextView notificationPackageName;
        TextView notificationTitle;
        TextView notificationText;

        NotificationViewHolder(View itemView) {
            super(itemView);
            notificationImage = itemView.findViewById(R.id.notification_image);
            notificationPackageName = itemView.findViewById(R.id.notification_package_name);
            notificationTitle = itemView.findViewById(R.id.notification_title);
            notificationText = itemView.findViewById(R.id.notification_text);
        }
    }
}
