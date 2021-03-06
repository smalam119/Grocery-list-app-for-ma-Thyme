package com.pseudozero.thyme.thyme.main;

/**
 * Created by SAYED on 1/14/2016.
 */
public class SingleRowToDoList
{
    String title;
    String date;
    int isToDoList;
    int isFavImage;
    String optionMenu;
    int id;
    int imageResource;

    int isAlarmed;

    public int getIsToDoList()
    {
        return isToDoList;
    }

    public void setIsToDoList(int isToDoList)
    {
        this.isToDoList = isToDoList;
    }

    public int getIsFavImage() {
        return isFavImage;
    }

    public void setIsFavImage(int isFavImage) {
        this.isFavImage = isFavImage;
    }

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

    public int getIsAlarmed() {
        return isAlarmed;
    }

    public void setIsAlarmed(int isAlarmed) {
        this.isAlarmed = isAlarmed;
    }

    public SingleRowToDoList(int id, String title, String date, int imageResource, int isToDoList, int isFavImage, int isAlarmed)
    {
        this.id = id;
        this.title = title;
        this.date = date;
        this.imageResource = imageResource;
        this.isToDoList = isToDoList;
        this.isFavImage = isFavImage;
        this.isAlarmed = isAlarmed;
    }
}
