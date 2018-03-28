package com.example.android.notify.adapters;


import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.notify.R;
import com.example.android.notify.itemmodels.NotificationSubItemModel;
import com.example.android.notify.utils.TextViewUtils;

import java.util.List;

public class NotificationSubAdapter extends RecyclerView.Adapter<NotificationSubAdapter.NotificationViewHolder> {

    private static final String TAG = NotificationSubAdapter.class.getSimpleName();

    private List<NotificationSubItemModel> mNotificationList;
    private Context mContext;

    public NotificationSubAdapter(List<NotificationSubItemModel> notificationList) {
        mNotificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext())
                                                        .inflate(R.layout.notification_sub_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, int position) {
        final NotificationSubItemModel notificationItemModel = mNotificationList.get(position);
        if (notificationItemModel.mLargeIcon != null) {
            holder.mNotificationImage.setImageBitmap(notificationItemModel.mLargeIcon);
        }
        holder.mNotificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deepLinkToApp(notificationItemModel);
            }
        });
        TextViewUtils.setTextAndVisibility(holder.mNotificationTitle, notificationItemModel.getTitle());
        TextViewUtils.setTextAndVisibility(holder.mNotificationText, notificationItemModel.getText());
        holder.mNotificationCard.setCardBackgroundColor(Color.WHITE);
        TextViewUtils.setTextAndVisibility(holder.mNotificationTimestamp, notificationItemModel.getTimestamp());
        holder.mNotificationTextLines.setAdapter(new TextLinesAdapter(notificationItemModel.getTextLines()));
        holder.mNotificationTextLines.setLayoutManager(new LinearLayoutManager(mContext,
                                                                               RecyclerView.VERTICAL,
                                                                               false));
    }

    private void deepLinkToApp(@NonNull final NotificationSubItemModel notificationSubItemModel) {
        try {
            notificationSubItemModel.mPendingIntent.send();
            // TODO: temporarily catching NPE for sending empty PendingIntents
        } catch (NullPointerException | PendingIntent.CanceledException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mNotificationList == null ? 0 : mNotificationList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        CardView mNotificationCard;
        ImageView mNotificationImage;
        TextView mNotificationTitle;
        TextView mNotificationText;
        TextView mNotificationTimestamp;
        RecyclerView mNotificationTextLines;

        NotificationViewHolder(View itemView) {
            super(itemView);
            mNotificationCard = itemView.findViewById(R.id.notification_sub_card);
            mNotificationImage = itemView.findViewById(R.id.notification_sub_image);
            mNotificationTitle = itemView.findViewById(R.id.notification_sub_title);
            mNotificationText = itemView.findViewById(R.id.notification_sub_text);
            mNotificationTimestamp = itemView.findViewById(R.id.notification_sub_timestamp);
            mNotificationTextLines = itemView.findViewById(R.id.notification_sub_text_lines_recycler_view);
        }
    }
}
