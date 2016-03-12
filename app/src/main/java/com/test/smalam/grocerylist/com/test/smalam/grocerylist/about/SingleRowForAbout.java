package com.test.smalam.grocerylist.com.test.smalam.grocerylist.about;

import com.test.smalam.grocerylist.com.test.smalam.grocerylist.settings.Settings;

import java.net.URL;

/**
 * Created by SAYED on 3/12/2016.
 */
public class SingleRowForAbout
{
    private String name;
    private String url;
    private int image;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SingleRowForAbout(String name,int image,String url)
    {
        this.name = name;
        this.image = image;
        this.url = url;
    }

}
