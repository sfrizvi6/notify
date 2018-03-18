package com.example.android.notify;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.example.android.notify.adapters.NotificationAdapter;
import com.example.android.notify.data.NotificationsDbHelper;
import com.example.android.notify.itemmodels.NotificationItemModel;
import com.example.android.notify.itemmodels.NotificationSubItemModel;
import com.example.android.notify.receiver.NotificationReceiver;
import com.example.android.notify.services.NotifyListenerService;
import com.example.android.notify.utils.NotificationCategory;
import com.example.android.notify.utils.NotificationUpdateState;
import com.example.android.notify.utils.TestingUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.notify.data.NotificationContract.NotificationEntry;
import static com.example.android.notify.data.NotificationContract.NotificationEntry.TABLE_NAME;

public class NotificationsActivity extends AppCompatActivity {

    private static final String TAG = NotificationsActivity.class.getSimpleName();

    public TestingUtils mTestingUtils;

    private RecyclerView mNotificationsRecyclerView;
    private NotificationAdapter mAdapter;
    private NotificationReceiver mNotificationReceiver;
    private List<NotificationItemModel> mData;
    private NotificationsDbHelper mDbHelper;
    private static SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        mTestingUtils = new TestingUtils(this);


        initNotificationListRecyclerView();

        startService(new Intent(this, NotifyListenerService.class));

        IntentFilter filter = new IntentFilter();
        filter.addAction("notification_created");


        mDbHelper = new NotificationsDbHelper(this);
        mDb = mDbHelper.getWritableDatabase();

        mNotificationReceiver = new NotificationReceiver(mDb, mDbHelper, mData, mAdapter);
        // Using LocalBroadcastManager instead of Context to registerReceiver and sendBroadcasts
        // to avoid exception: android.app.RemoteServiceException: can't deliver broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(mNotificationReceiver, filter);
        recreatePersistedNotificationCards();
    }

    public void createNotification(View v) {
        mTestingUtils.createInboxStyleNotification(v);
    }

    private void recreatePersistedNotificationCards() {
        Cursor cursor = mDb.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_NOTIFICATION_ID));
            String category = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_NOTIFICATION_CATEGORY));
            String appName = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_APP_NAME));
            int icon = cursor.getInt(cursor.getColumnIndex(NotificationEntry.COLUMN_APP_ICON));
            // TODO: figure this out
            Bitmap largeIcon = null;
            String packageName = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_APP_PACKAGE_NAME));
            // TODO: figure this out
            PendingIntent pendingIntent = null;
            String groupKey = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_GROUP_KEY));
            String title = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TITLE));
            String text = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TEXT));
            String timestamp = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TIMESTAMP));
            String textLinesString = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TEXTLINES));

            NotificationSubItemModel recreatedNotification =
                new NotificationSubItemModel(this,
                                             id,
                                             NotificationCategory.getCategory(category),
                                             appName,
                                             icon,
                                             largeIcon,
                                             packageName,
                                             pendingIntent,
                                             groupKey,
                                             title,
                                             text,
                                             timestamp,
                                             textLinesString);
            Log.e(TAG, NotificationUpdateState.RECREATED_NOTIFICATION.toString());
            Log.e(TAG,
                  mNotificationReceiver.updateNotificationSubCardIfNotificationPackageExists(recreatedNotification)
                                       .toString());
        }
    }

    private void initNotificationListRecyclerView() {
        mData = new ArrayList<>();
        mAdapter = new NotificationAdapter(mData);
        mNotificationsRecyclerView = findViewById(R.id.notification_list);
        mNotificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mNotificationsRecyclerView.setAdapter(mAdapter);
    }
}
