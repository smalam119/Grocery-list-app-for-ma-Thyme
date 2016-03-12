package com.test.smalam.grocerylist.com.test.smalam.grocerylist.settings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;

import com.test.smalam.grocerylist.R;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;

/**
 * Created by SAYED on 2/26/2016.
 */
public class Settings
{

    private int fontColorNumber;
    private int fontNumber;
    private int fontSizeNumber;

    public int getFontSizeNumber() {
        return fontSizeNumber;
    }

    public void setFontSizeNumber(int fontSize) {
        this.fontSizeNumber = fontSize;
    }

    public int getFontNumber() {
        return fontNumber;
    }

    public void setFontNumber(int fontNumber) {
        this.fontNumber = fontNumber;
    }

    public int getFontColorNumber() {
        return fontColorNumber;
    }

    public void setFontColorNumber(int fontColorNumber) {
        this.fontColorNumber = fontColorNumber;
    }



    public String getFont(int fontNumber)
    {
        switch(fontNumber)
        {
            case 0: return "fonts/DroidSansMono.ttf";

            case 1: return "fonts/SF_Arch_Rival.ttf";

            case 2: return "fonts/Raleway-Regular.ttf";

            case 3: return "fonts/Walkway_Black.ttf";

            default:

        }

        return "";
    }

    public int getFontSize(int fontNumber)
    {
        switch(fontNumber)
        {
            case 0: return R.dimen.text_small;

            case 1: return R.dimen.text_medium;

            case 2: return R.dimen.text_large;

            default:

        }

        return 0;
    }

    public String getFontColor(int fontColorNumber)
    {
        switch(fontColorNumber)
        {
            case 0: return "";

            case 1: return "";

            case 2: return "";

            case 3: return "";

            default:

        }

        return "";
    }

    public void getSetting(android.support.v4.app.Fragment f)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(f.getContext());
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("SETTINGS",
                new String[]{"FONT_FAMILY", "FONT_COLOR","FONT_SIZE"},
                "_id=?",
                new String[]{"1"},
                null, null, null);

        if (cursor.moveToFirst())
        {
            this.fontNumber = cursor.getInt(0);
            this.fontColorNumber = cursor.getInt(1);
            this.fontSizeNumber = cursor.getInt(2);
        }
    }

    public void getSetting(Activity a)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(a);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("SETTINGS",
                new String[]{"FONT_FAMILY", "FONT_COLOR","FONT_SIZE"},
                "_id=?",
                new String[]{"1"},
                null, null, null);

        if (cursor.moveToFirst())
        {
            this.fontNumber = cursor.getInt(0);
            this.fontColorNumber = cursor.getInt(1);
            this.fontSizeNumber = cursor.getInt(2);
        }
    }

    public void getSetting(Context c)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(c);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("SETTINGS",
                new String[]{"FONT_FAMILY", "FONT_COLOR","FONT_SIZE"},
                "_id=?",
                new String[]{"1"},
                null, null, null);

        if (cursor.moveToFirst())
        {
            this.fontNumber = cursor.getInt(0);
            this.fontColorNumber = cursor.getInt(1);
            this.fontSizeNumber = cursor.getInt(2);
        }
    }
}
