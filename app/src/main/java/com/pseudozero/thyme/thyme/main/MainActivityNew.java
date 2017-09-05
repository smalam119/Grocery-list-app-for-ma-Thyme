package com.pseudozero.thyme.thyme.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.pseudozero.thyme.thyme.R;
import com.pseudozero.thyme.thyme.about.AboutActivity;
import com.pseudozero.thyme.thyme.database.GroceryListDatabaseHelper;
import com.pseudozero.thyme.thyme.settings.SettingsActivity;

public class MainActivityNew extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        prepareNavigationDrawer();

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, new PreviousListsFragment());
        tx.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void prepareNavigationDrawer() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName("Create To Do List")
                .withIcon(R.drawable.previous_to_do_icon_grey)
                .withSelectedIcon(R.drawable.previous_to_do_icon_blue);

        PrimaryDrawerItem item2 = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName("Take a Note")
                .withIcon(R.drawable.previous_note_icon_black)
                .withSelectedIcon(R.drawable.previous_note_icon_blue);

        PrimaryDrawerItem item3 = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName("Previous List")
                .withIcon(R.drawable.archive_black)
                .withSelectedIcon(R.drawable.archive_blue)
                .withBadge(getPreviousListCount() + "")
                .withBadgeStyle(new BadgeStyle().withColor(getResources().getColor(R.color.primary)).withTextColor(getResources().getColor(R.color.white)));

        PrimaryDrawerItem item4 = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName("Trash")
                .withIcon(R.drawable.trash_black)
                .withSelectedIcon(R.drawable.trash_blue)
                .withBadge(getArchiveListCount() + "")
                .withBadgeStyle(new BadgeStyle().withColor(getResources().getColor(R.color.primary)).withTextColor(getResources().getColor(R.color.white)));

        SecondaryDrawerItem item5 = new SecondaryDrawerItem()
                .withIdentifier(5)
                .withName("Settings")
                .withTextColor(getResources().getColor(R.color.black))
                .withIcon(R.drawable.action_setting_icon_black)
                .withSelectedIcon(R.drawable.action_setting_icon_blue);

        SecondaryDrawerItem item6 = new SecondaryDrawerItem()
                .withIdentifier(6)
                .withName("About")
                .withTextColor(getResources().getColor(R.color.black))
                .withIcon(R.drawable.about_black)
                .withSelectedIcon(R.drawable.about_blue);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                .addProfiles(
                        new ProfileDrawerItem().withName("Not Set").withEmail("Not Set").withIcon(getResources().getDrawable(R.drawable.avatar_placeholder))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        new DividerDrawerItem(),
                        item5,
                        item6
                )
                .withAccountHeader(headerResult)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Fragment fragment = null;
                        Class fragmentClass;
                        FragmentManager fragmentManager = getSupportFragmentManager();

                        int identifier = (int) drawerItem.getIdentifier();

                        switch(identifier) {
                            case 1:
                                fragmentClass = CreateNewToDoFragment.class;
                                try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                                break;
                            case 2:
                                fragmentClass =  CreateNoteFragment.class;
                                try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                                break;
                            case 3:
                                fragmentClass =  PreviousListsFragment.class;
                                try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                                break;
                            case 4:
                                fragmentClass =  ArchivedListFragment.class;
                                try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                                break;
                            case 5:
                                startActivity( new Intent(MainActivityNew.this, SettingsActivity.class));
                                break;
                            case 6:
                                startActivity( new Intent(MainActivityNew.this, AboutActivity.class));
                                break;

                            default:
                                fragmentClass =  PreviousListsFragment.class;
                                try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }

                        // Insert the fragment by replacing any existing fragment
                        return false;
                    }
                })
                .build();
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            exitByBackKey();
        } else {
            getFragmentManager().popBackStack();
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey()
    {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    private int getPreviousListCount() {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(getApplication());
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS", new String[]{"_id", "NAME", "DATE", "FAVORITE","IS_TO_DO_LIST","IS_ALARMED"}, "ARCHIVED = ?", new String[]{"0"}, null, null, null);
        return cursor.getCount();
    }

    private int getArchiveListCount() {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(getApplication());
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS", new String[] {"_id","NAME","DATE","IS_TO_DO_LIST"},"ARCHIVED = ?", new String[] {"1"},null,null,null);
        return cursor.getCount();
    }

}
