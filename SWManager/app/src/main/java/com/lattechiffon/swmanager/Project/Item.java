package com.lattechiffon.swmanager.Project;

import android.graphics.drawable.Drawable;

/**
 * Created by hyojun on 2017-06-26.
 */

public class Item {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String descStr ;
    private String ProjID;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }
    public void setProjID(String ProjID) {this.ProjID = ProjID;}

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
    public String getProjID() {return this.ProjID; }
 }


