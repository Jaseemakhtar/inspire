package com.jsync.freebook.tabThree;

import com.parse.ParseFile;

/**
 * Created by JSareen on 3/14/2018.
 */

public class OldBookListItems {
    private String bookTitle;
    private String bookDesc;
    private String donatorName;

    private ParseFile oldBookImage;

    public OldBookListItems(String bookTitle, String bookDesc, String donatorName, ParseFile oldBookImage) {
        this.bookTitle = bookTitle;
        this.bookDesc = bookDesc;
        this.donatorName = donatorName;
        this.oldBookImage = oldBookImage;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public String getDonatorName() {
        return donatorName;
    }

    public void setDonatorName(String donatorName) {
        this.donatorName = donatorName;
    }

    public ParseFile getOldBookImage() {
        return oldBookImage;
    }

    public void setOldBookImage(ParseFile oldBookImage) {
        this.oldBookImage = oldBookImage;
    }


}
