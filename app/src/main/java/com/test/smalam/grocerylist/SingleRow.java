package com.test.smalam.grocerylist;

/**
 * Created by SAYED on 1/14/2016.
 */
public class SingleRow
{
    String title,date,optionMenu;
    int id;

    public SingleRow(int id,String title, String date,String optionMenu)
    {
        this.id = id;
        this.title = title;
        this.date = date;
        this.optionMenu = optionMenu;
    }
}
