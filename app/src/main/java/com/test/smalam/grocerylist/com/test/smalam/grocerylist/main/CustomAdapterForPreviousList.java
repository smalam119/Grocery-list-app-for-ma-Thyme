package com.test.smalam.grocerylist.com.test.smalam.grocerylist.main;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.test.smalam.grocerylist.R;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.database.GroceryListDatabaseHelper;
import com.test.smalam.grocerylist.com.test.smalam.grocerylist.settings.Settings;

import java.util.ArrayList;


/**
 * Created by SAYED on 1/13/2016.
 */
public class CustomAdapterForPreviousList extends BaseAdapter
{
    Context context;
    ArrayList<SingleRow> a;
    ArrayList<SingleRow> a1;
    ValueFilter valueFilter;
    final String[] spinnerOptions = {
            "",
            "Edit",
            "Delete",
            "Send"
    };

    Settings settings;


    public  static String queryType;

    CustomAdapterForPreviousList(Context c,String typeOfSearch) {
        context = c;
        a = new ArrayList<SingleRow>();
        a1 = new ArrayList<SingleRow>();
        settings = new Settings();
        settings.getSetting(context);
        final SingleRow temp;

        if(typeOfSearch.equals("all"))
        {
            readAllLists();
            queryType = "all";
        }
        else if(typeOfSearch.equals("favorites"))
        {
            readOnlyFavorites();
            queryType = "favorite";
        }
        else if(typeOfSearch.equals("notes"))
        {
            readOnlyNotes(0);
            queryType = "notes";
        }
        else if(typeOfSearch.equals("toDo"))
        {
            readOnlyNotes(1);
            queryType = "toDo";
        }


    }

    public void readAllLists() {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS", new String[]{"_id", "NAME", "DATE", "FAVORITE","IS_TO_DO_LIST"}, "ARCHIVED = ?", new String[]{"0"}, null, null, null);

        while (cursor.moveToNext()) {

            int favorite = cursor.getInt(3);
            int isToDo = cursor.getInt(4);

            if (favorite == 1 && isToDo ==1) {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue));
            } else if (favorite == 0  && isToDo ==1) {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_grey,cursor.getInt(4),R.color.colorAccent));
           }
            else if (isToDo == 0) {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_grey,cursor.getInt(4),R.color.colorAccent));
            }


        }

    }

    public void readOnlyFavorites() {

        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();

        Cursor cursor = db.query("LISTS", new String[]{"_id", "NAME", "DATE", "FAVORITE","IS_TO_DO_LIST"}, "ARCHIVED = ? AND FAVORITE = ?", new String[]{"0","1"}, null, null, null);

        while (cursor.moveToNext()) {

            int favorite = cursor.getInt(3);
            int isToDo = cursor.getInt(4);

            if (favorite == 1 && isToDo ==1) {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue));
            } else if (favorite == 0  && isToDo ==1) {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.color.colorAccent));
            }
            else if (isToDo == 0) {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2), R.drawable.previous_note_icon_blue,cursor.getInt(4),R.color.colorAccent));
            }


        }

    }

    public void readOnlyNotes(int isToDoNote) {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LISTS", new String[]{"_id", "NAME", "DATE", "FAVORITE","IS_TO_DO_LIST"}, "ARCHIVED = ? AND IS_TO_DO_LIST = ?", new String[]{"0", String.valueOf(isToDoNote)}, null, null, null);

        while (cursor.moveToNext()) {

            int favorite = cursor.getInt(3);
            int isToDo = cursor.getInt(4);

            if (favorite == 1 && isToDo ==1) {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_blue,cursor.getInt(4),R.drawable.option_menu_fav_blue));
            } else if (favorite == 0  && isToDo ==1) {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_to_do_icon_grey,cursor.getInt(4),R.color.colorAccent));
            }
            else if (isToDo ==0) {
                a.add(new SingleRow(cursor.getInt(0), cursor.getString(1), cursor.getString(2),R.drawable.previous_note_icon_grey,cursor.getInt(4),R.color.colorAccent));
            }


        }

    }

    public void sendToTrash(SingleRow s)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ARCHIVED", 1);
        db.update("LISTS", cv, "_id=?", new String[]{String.valueOf(s.getId())});
    }


    public void deleteList(SingleRow s)
    {
        SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(context);
        SQLiteDatabase db = groceryListDatabaseHelper.getReadableDatabase();
        db.delete("LISTS",
                "NAME=?",
                new String[]{s.title});
    }

    @Override
    public int getCount() {
        return a.size();
    }

    @Override
    public Object getItem(int position) {
        return a.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.single_row, parent, false);

        TextView tvTitle = (TextView) rowView.findViewById(R.id.textView_title_single_row);
        TextView tvDate = (TextView) rowView.findViewById(R.id.textView_date);
        ImageView icon = (ImageView) rowView.findViewById(R.id.imageView1);
        final SingleRow temp = a.get(position);

        ImageView isFav = (ImageView) rowView.findViewById(R.id.is_fav);

        tvTitle.setText(temp.title);
        tvTitle.setTypeface(Typeface.createFromAsset(context.getAssets(), settings.getFont(settings.getFontNumber())));
        tvDate.setText(temp.date);
        tvDate.setTypeface(Typeface.createFromAsset(context.getAssets(), settings.getFont(settings.getFontNumber())));
        icon.setImageResource(temp.imageResource);
        isFav.setImageResource(temp.isFavImage);

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            showMenu(v,temp);
            return true;
            }
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (temp.getIsToDoList() == 1) {
                    Intent intent = new Intent(context, ToDoViewerActivity.class);
                    intent.putExtra(ToDoViewerActivity.LIST_ID, temp.id);
                    context.startActivity(intent);
                } else if (temp.getIsToDoList() == 0) {
                    Intent intent = new Intent(context, NotesViewerActivity.class);
                    intent.putExtra(NotesViewerActivity.LIST_ID, temp.getId());
                    context.startActivity(intent);
                }
            }
        });

        return rowView;
    }

    public void showMenu(View v, final SingleRow t) {
        PopupMenu popup = new PopupMenu(context, v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        if(t.getIsToDoList() == 1) {
                            Intent intent = new Intent(context, EditToDoActivity.class);
                            intent.putExtra(EditToDoActivity.LIST_ID, t.getId());
                            context.startActivity(intent);
                        }

                        else if (t.getIsToDoList() == 0)
                        {
                            Intent intent = new Intent(context, EditNotesActivity.class);
                            intent.putExtra(EditNotesActivity.LIST_ID, t.getId());
                            context.startActivity(intent);
                        }
                        return true;

                    case R.id.delete:
                        a.remove(t);
                        CustomAdapterForPreviousList.this.notifyDataSetChanged();
                        sendToTrash(t);
                        Toast.makeText(context, "List Deleted", Toast.LENGTH_LONG).show();
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.previous_lists_option_menu);
        popup.show();
    }

    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<SingleRow> filterList = new ArrayList<SingleRow>();
                for (int i = 0; i < a.size(); i++) {
                    if ((a.get(i).getTitle().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        SingleRow sr = new SingleRow(a.get(i).getId(), a.get(i).getTitle(), a.get(i).getDate(), a.get(i).getImageResource(),a.get(i).getId(),a.get(i).getIsFavImage());

                        filterList.add(sr);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else {

                if(CustomAdapterForPreviousList.queryType.equals("all"))
                {
                    a.clear();
                    readAllLists();
                    results.count = a.size();
                    results.values = a;
                }
                else if(CustomAdapterForPreviousList.queryType.equals("favorite"))
                {
                    a.clear();
                    readOnlyFavorites();
                    results.count = a.size();
                    results.values = a;
                }
                else if(CustomAdapterForPreviousList.queryType.equals("notes"))
                {
                    a.clear();
                    readOnlyNotes(0);
                    results.count = a.size();
                    results.values = a;
                }
                else if(CustomAdapterForPreviousList.queryType.equals("toDo"))
                {
                    a.clear();
                    readOnlyNotes(1);
                    results.count = a.size();
                    results.values = a;
                }
            }
            return results;

        }


        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results)
        {
            a = (ArrayList<SingleRow>) results.values;
            notifyDataSetChanged();
        }


    }
}
