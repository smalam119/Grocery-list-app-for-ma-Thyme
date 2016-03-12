package com.test.smalam.grocerylist.com.test.smalam.grocerylist.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.widget.Toast;

import com.test.smalam.grocerylist.R;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.main.MainActivity;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.main.NotesViewerActivity;

import java.util.Random;

/**
 * Created by SAYED on 2/29/2016.
 */
public class ReminderService extends IntentService
{
    public static final String EXTRA_MESSAGE = "message";
    public static final String LIST_ID = "id";
    public static final int NOTIFICATION_ID = 5453;
    public static final String DELAY = "";
    String listId;
    String delay;
    private SQLiteDatabase db;


    public ReminderService()
    {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

        try
        {
            SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
            db = groceryListDatabaseHelper.getReadableDatabase();
        }
        catch(SQLiteException e)
        {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        delay = intent.getStringExtra(DELAY);
        synchronized (this) {
            try {
                wait(Long.valueOf(delay).longValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        listId = intent.getStringExtra(LIST_ID);
        showReminder(text);

        removeReminderIcon();
    }

    private void showReminder(final String text)
    {
        Intent i = new Intent(this, NotesViewerActivity.class);
        i.putExtra(NotesViewerActivity.LIST_ID,listId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(i);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.option_menu_reminder_grey)
                .setContentTitle("You have a notification")
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText(text)
                .setLights(Color.BLUE, 1, 1)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.notify(Integer.parseInt(listId), notification);

    }

    public void removeReminderIcon()
    {
        ContentValues cv = new ContentValues();
        cv.put("IS_ALARMED", 0);
        //Toast.makeText(this,"Reminder Set",Toast.LENGTH_SHORT).show();
        db.update("LISTS", cv, "_id=?", new String[]{listId});
    }
}
