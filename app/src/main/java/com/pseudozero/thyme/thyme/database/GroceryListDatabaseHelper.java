package com.pseudozero.thyme.thyme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SAYED on 1/19/2016.
 */
public class GroceryListDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "grocerylist";
    private static final int DB_VERSION = 1;

    public GroceryListDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        updateMyDatabase(db,0,DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion<1)
        {
            db.execSQL("CREATE TABLE LISTS(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+"DATE TEXT,"+"NAME TEXT,"+"ITEMS TEXT,"+"ARCHIVED INTEGER,"+"FAVORITE INTEGER,"+"CHECK_LIST_STATUS TEXT,"+"IS_TO_DO_LIST INTEGER,"+"IS_ALARMED INTEGER,"+"DATE_ALARM TEXT);");
            db.execSQL("CREATE TABLE SETTINGS(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "FONT_FAMILY INTEGER," + "FONT_COLOR INTEGER,"+"FONT_SIZE INTEGER);");
            ContentValues cv = new ContentValues();
            cv.put("FONT_FAMILY", 2);
            cv.put("FONT_COLOR", 0);
            cv.put("FONT_SIZE",1);
            db.insert("SETTINGS", null, cv);
        }

        if(oldVersion<2)
        {
            //
        }
    }
}
