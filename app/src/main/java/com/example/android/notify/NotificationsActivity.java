package com.example.android.notify;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.example.android.notify.adapters.NotificationAdapter;
import com.example.android.notify.data.NotificationsDbHelper;
import com.example.android.notify.itemmodels.NotificationItemModel;
import com.example.android.notify.itemmodels.NotificationSubItemModel;
import com.example.android.notify.loader.RecreateNotificationLoader;
import com.example.android.notify.receiver.NotificationReceiver;
import com.example.android.notify.services.NotifyListenerService;
import com.example.android.notify.settings.SettingsActivity;
import com.example.android.notify.utils.TestingUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private static final String TAG = NotificationsActivity.class.getSimpleName();
    private static final int RECREATE_NOTIFICATION_LOADER_ID = 1;

    public TestingUtils mTestingUtils;

    private NotificationAdapter mAdapter;
    private NotificationReceiver mNotificationReceiver;
    private NotificationsDbHelper mDbHelper;

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

        // TODO: for testing only
        // mDbHelper.cleanInitDb();

        mNotificationReceiver =
            new NotificationReceiver(this, getSupportLoaderManager(), mDbHelper, mAdapter);
        // Using LocalBroadcastManager instead of Context to registerReceiver and sendBroadcasts
        // to avoid exception: android.app.RemoteServiceException: can't deliver broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(mNotificationReceiver, filter);

        final Context context = this;

        LoaderManager.LoaderCallbacks<List<NotificationSubItemModel>> recreateNotificationListener =
            new LoaderManager.LoaderCallbacks<List<NotificationSubItemModel>>() {
                @Override
                public Loader<List<NotificationSubItemModel>> onCreateLoader(int id, Bundle args) {
                    return new RecreateNotificationLoader(context, mDbHelper);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<List<NotificationSubItemModel>> loader,
                                           List<NotificationSubItemModel> recreatedNotification) {
                    Log.e(TAG,
                          mNotificationReceiver.updateNotificationSubCardIfNotificationPackageExists(
                              recreatedNotification)
                                               .toString());
                    getSupportLoaderManager().destroyLoader(RECREATE_NOTIFICATION_LOADER_ID);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<List<NotificationSubItemModel>> loader) {
                }
            };

        getSupportLoaderManager().initLoader(RECREATE_NOTIFICATION_LOADER_ID, null, recreateNotificationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }

    public void createNotification(View v) {
        mTestingUtils.createInboxStyleNotification(v);
    }

    private void initNotificationListRecyclerView() {
        mAdapter = new NotificationAdapter(new ArrayList<NotificationItemModel>());
        RecyclerView mNotificationsRecyclerView = findViewById(R.id.notification_list);
        mNotificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mNotificationsRecyclerView.setAdapter(mAdapter);
    }
}
