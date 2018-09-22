package com.jsync.freebook.tabTwo;

import android.view.View;

/**
 * Created by JSareen on 3/20/2018.
 */

public class MyRequestsList {
    String title;

    String donatorName;

    String desc;

    public MyRequestsList(String title, String donatorName, String desc) {
        this.title = title;
        this.donatorName = donatorName;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDonatorName() {
        return donatorName;
    }

    public void setDonatorName(String donatorName) {
        this.donatorName = donatorName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
