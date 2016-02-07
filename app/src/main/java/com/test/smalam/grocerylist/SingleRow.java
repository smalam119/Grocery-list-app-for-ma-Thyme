package com.test.smalam.grocerylist;

/**
 * Created by SAYED on 1/14/2016.
 */
public class SingleRow
{
    String title;
    String date;

    public int getIsToDoList()
    {
        return isToDoList;
    }

    public void setIsToDoList(int isToDoList)
    {
        this.isToDoList = isToDoList;
    }

    int isToDoList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOptionMenu() {
        return optionMenu;
    }

    public void setOptionMenu(String optionMenu) {
        this.optionMenu = optionMenu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    String optionMenu;
    int id,imageResource;

    public SingleRow(int id,String title, String date,String optionMenu,int imageResource,int isToDoList)
    {
        this.id = id;
        this.title = title;
        this.date = date;
        this.optionMenu = optionMenu;
        this.imageResource = imageResource;
        this.isToDoList = isToDoList;
    }
}
