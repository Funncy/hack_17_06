package com.lattechiffon.swmanager.Project;

import android.graphics.drawable.Drawable;

/**
 * Created by hyojun on 2017-06-26.
 */

public class MessageItem {
    private String content ;
    private String otherID;
    private String Type;
    private String otherProjectID;

    public void setcontent(String content) {
        this.content = content ;
    }
    public void setotherID(String otherID) {this.otherID = otherID;}
    public void setType(String Type) {this.Type = Type;}
    public void setotherProjectID(String otherProjectID) {this.otherProjectID = otherProjectID;}

    public String getcontent() {
        return this.content ;
    }
    public String getotherID() {return this.otherID; }
    public String getType() {return this.Type; }
    public String getotherProjectID() {return this.otherProjectID; }
}


