package com.example.android.notify.adapters;


import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.notify.R;
import com.example.android.notify.itemmodels.NotificationItemModel;

import java.util.List;

import static android.content.ContentValues.TAG;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationItemModel> mNotificationList;
    private Context mContext;

    public NotificationAdapter(List<NotificationItemModel> notificationList) {
        this.mNotificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext())
                                                        .inflate(R.layout.notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, int position) {
        final NotificationItemModel notificationItemModel = mNotificationList.get(position);
        try {
            Resources res =
                mContext.getPackageManager().getResourcesForApplication(notificationItemModel.mPackageName);
            Drawable icon = res.getDrawable(notificationItemModel.mAppIcon);
            holder.mNotificationImage.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        holder.nShowMore.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v != null && event.getAction() == MotionEvent.ACTION_DOWN) {
                    toggleDetailCardView((Button) v);
                }
                return true;
            }
        });
        holder.mNotificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deepLinkToApp(notificationItemModel);
            }
        });
        holder.mNotificationAppName.setText(notificationItemModel.mAppName);
        holder.mNotificationTitle.setText(notificationItemModel.getTitle());
        holder.mNotificationText.setText(notificationItemModel.getText());
        holder.mNotificationCard.setCardBackgroundColor(Color.LTGRAY);
        holder.mNotificationTimestamp.setText(notificationItemModel.getTimestamp());
        String textLines = notificationItemModel.getTextLines();
        holder.mNotificationTextLines.setText(textLines == null ? "" : textLines);
        holder.mNotificationTextLines.setVisibility(View.GONE);
        holder.mNotificationsSubRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,
                                                                                      RecyclerView.VERTICAL,
                                                                                      false));
        holder.mNotificationsSubRecyclerView.setAdapter(notificationItemModel.getSubAdapter());
        holder.nShowMore.setVisibility(textLines == null || textLines.equals("")
                                      ? View.GONE
                                      : View.VISIBLE);
    }

    private void toggleDetailCardView(@NonNull Button showMoreButton) {
        boolean isShowMoreVisible = showMoreButton.getVisibility() == View.VISIBLE && showMoreButton.getText()
                                                                                                    .equals(mContext.getString(
                                                                                                        R.string.show_more_btn_label));
        showMoreButton.setText(isShowMoreVisible
                               ? mContext.getString(R.string.show_less_btn_label)
                               : mContext.getString(R.string.show_more_btn_label));

        TextView textLinesTextView = ((View) showMoreButton.getParent()).findViewById(R.id.notification_text_lines);
        textLinesTextView.setVisibility(isShowMoreVisible
                                        ? showMoreButton.getVisibility() == View.GONE ? View.GONE : View.VISIBLE
                                        : View.GONE);
    }

    private void deepLinkToApp(@NonNull final NotificationItemModel notificationItemModel) {
        try {
            notificationItemModel.mPendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
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
        TextView mNotificationAppName;
        TextView mNotificationTitle;
        TextView mNotificationText;
        TextView mNotificationTimestamp;
        Button nShowMore;
        TextView mNotificationTextLines;
        RecyclerView mNotificationsSubRecyclerView;

        NotificationViewHolder(View itemView) {
            super(itemView);
            mNotificationCard = itemView.findViewById(R.id.notification_card);
            mNotificationImage = itemView.findViewById(R.id.notification_image);
            mNotificationAppName = itemView.findViewById(R.id.notification_app_name);
            mNotificationTitle = itemView.findViewById(R.id.notification_title);
            mNotificationText = itemView.findViewById(R.id.notification_text);
            mNotificationTimestamp = itemView.findViewById(R.id.notification_timestamp);
            nShowMore = itemView.findViewById(R.id.notification_see_more);
            mNotificationTextLines = itemView.findViewById(R.id.notification_text_lines);
            mNotificationsSubRecyclerView = itemView.findViewById(R.id.notification_sub_list);

        }
    }
}
