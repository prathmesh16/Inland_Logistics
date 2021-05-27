package com.iwlpl.connectme.data_handler;
import android.graphics.drawable.Drawable;

public class CardsModel {
    String title;
    Drawable icon;
    String BottomColor;

    public CardsModel(String title, Drawable icon, String bottomColor) {
        this.title = title;
        this.icon = icon;
        BottomColor = bottomColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getBottomColor() {
        return BottomColor;
    }

    public void setBottomColor(String bottomColor) {
        BottomColor = bottomColor;
    }
}
