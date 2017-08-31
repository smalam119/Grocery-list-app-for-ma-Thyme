package com.pseudozero.thyme.thyme.about;

/**
 * Created by SAYED on 3/12/2016.
 */
public class IconCreditSingleRow
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

    public IconCreditSingleRow(String name, int image, String url)
    {
        this.name = name;
        this.image = image;
        this.url = url;
    }

}
