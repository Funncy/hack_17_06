package com.lattechiffon.swmanager.Project;

import android.graphics.drawable.Drawable;

/**
 * Created by hyojun on 2017-06-26.
 */

public class MemberItem {
    private Drawable iconDrawable ;
    private String Name ;
    private String Content ;
    private String MemberID;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setName(String Name) {
        this.Name = Name ;
    }
    public void setContent(String Content) {
        this.Content = Content ;
    }
    public void setMemberID(String MemberID) {this.MemberID = MemberID;}

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getName() {
        return this.Name ;
    }
    public String getContent() {
        return this.Content ;
    }
    public String getMemberID() {return this.MemberID; }
}


