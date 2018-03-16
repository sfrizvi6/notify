package com.example.android.notify.adapters;


import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.notify.R;
import com.example.android.notify.itemmodels.NotificationSubItemModel;

import java.util.List;

import static android.content.ContentValues.TAG;

public class NotificationSubAdapter extends RecyclerView.Adapter<NotificationSubAdapter.NotificationViewHolder> {

    private List<NotificationSubItemModel> notificationList;
    private Context context;

    public NotificationSubAdapter(List<NotificationSubItemModel> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext())
                                                        .inflate(R.layout.notification_sub_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, int position) {
        NotificationSubItemModel notificationItemModel = notificationList.get(position);
        try {
            Resources res =
                context.getPackageManager().getResourcesForApplication(notificationItemModel.packageName);
            Drawable icon = res.getDrawable(notificationItemModel.appIcon);
            holder.notificationImage.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        holder.notificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deepLinkToApp(holder);
            }
        });
        holder.notificationTitle.setText(notificationItemModel.getTitle());
        holder.notificationText.setText(notificationItemModel.getText());
        holder.notificationCard.setCardBackgroundColor(Color.LTGRAY);
        holder.notificationTimestamp.setText(notificationItemModel.getTimestamp());
        String textLines = notificationItemModel.getTextLines();
        if (textLines != null && textLines.length() > 0) {
            holder.notificationTextLines.setText(textLines);
        }
    }

    private void deepLinkToApp(@NonNull NotificationViewHolder holder) {
        // TODO: deeplink here
    }

    @Override
    public int getItemCount() {
        return notificationList == null ? 0 : notificationList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        CardView notificationCard;
        ImageView notificationImage;
        TextView notificationTitle;
        TextView notificationText;
        TextView notificationTimestamp;
        TextView notificationTextLines;

        NotificationViewHolder(View itemView) {
            super(itemView);
            notificationCard = itemView.findViewById(R.id.notification_sub_card);
            notificationImage = itemView.findViewById(R.id.notification_sub_image);
            notificationTitle = itemView.findViewById(R.id.notification_sub_title);
            notificationText = itemView.findViewById(R.id.notification_sub_text);
            notificationTimestamp = itemView.findViewById(R.id.notification_sub_timestamp);
            notificationTextLines = itemView.findViewById(R.id.notification_sub_text_lines);
        }
    }
}
