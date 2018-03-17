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

    private List<NotificationItemModel> notificationList;
    private Context context;

    public NotificationAdapter(List<NotificationItemModel> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext())
                                                        .inflate(R.layout.notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, int position) {
        final NotificationItemModel notificationItemModel = notificationList.get(position);
        try {
            Resources res =
                context.getPackageManager().getResourcesForApplication(notificationItemModel.packageName);
            Drawable icon = res.getDrawable(notificationItemModel.appIcon);
            holder.notificationImage.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        holder.showMore.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v != null && event.getAction() == MotionEvent.ACTION_DOWN) {
                    toggleDetailCardView((Button) v);
                }
                return true;
            }
        });
        holder.notificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deepLinkToApp(notificationItemModel);
            }
        });
        holder.notificationAppName.setText(notificationItemModel.appName);
        holder.notificationTitle.setText(notificationItemModel.getTitle());
        holder.notificationText.setText(notificationItemModel.getText());
        holder.notificationCard.setCardBackgroundColor(Color.LTGRAY);
        holder.notificationTimestamp.setText(notificationItemModel.getTimestamp());
        String textLines = notificationItemModel.getTextLines();
        holder.notificationTextLines.setText(textLines == null ? "" : textLines);
        holder.notificationTextLines.setVisibility(View.GONE);
        holder.notificationsSubRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                                                                                     RecyclerView.VERTICAL,
                                                                                     false));
        holder.notificationsSubRecyclerView.setAdapter(notificationItemModel.getSubAdapter());
        holder.showMore.setVisibility(textLines == null || textLines.equals("")
                                      ? View.GONE
                                      : View.VISIBLE);
    }

    private void toggleDetailCardView(@NonNull Button showMoreButton) {
        boolean isShowMoreVisible = showMoreButton.getVisibility() == View.VISIBLE && showMoreButton.getText()
                                                                                                    .equals(context.getString(
                                                                                                        R.string.show_more_btn_label));
        showMoreButton.setText(isShowMoreVisible
                               ? context.getString(R.string.show_less_btn_label)
                               : context.getString(R.string.show_more_btn_label));

        TextView textLinesTextView = ((View) showMoreButton.getParent()).findViewById(R.id.notification_text_lines);
        textLinesTextView.setVisibility(isShowMoreVisible
                                        ? showMoreButton.getVisibility() == View.GONE ? View.GONE : View.VISIBLE
                                        : View.GONE);
    }

    private void deepLinkToApp(@NonNull final NotificationItemModel notificationItemModel) {
        try {
            notificationItemModel.pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return notificationList == null ? 0 : notificationList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        CardView notificationCard;
        ImageView notificationImage;
        TextView notificationAppName;
        TextView notificationTitle;
        TextView notificationText;
        TextView notificationTimestamp;
        Button showMore;
        TextView notificationTextLines;
        RecyclerView notificationsSubRecyclerView;

        NotificationViewHolder(View itemView) {
            super(itemView);
            notificationCard = itemView.findViewById(R.id.notification_card);
            notificationImage = itemView.findViewById(R.id.notification_image);
            notificationAppName = itemView.findViewById(R.id.notification_app_name);
            notificationTitle = itemView.findViewById(R.id.notification_title);
            notificationText = itemView.findViewById(R.id.notification_text);
            notificationTimestamp = itemView.findViewById(R.id.notification_timestamp);
            showMore = itemView.findViewById(R.id.notification_see_more);
            notificationTextLines = itemView.findViewById(R.id.notification_text_lines);
            notificationsSubRecyclerView = itemView.findViewById(R.id.notification_sub_list);

        }
    }
}
