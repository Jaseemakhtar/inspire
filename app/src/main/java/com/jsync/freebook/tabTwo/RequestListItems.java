package com.jsync.freebook.tabTwo;

import com.parse.ParseFile;

/**
 * Created by JSareen on 2/11/2018.
 */

public class RequestListItems {
    private ParseFile imageFile;
    private String userName;
    private String bookTitle;
    private String bookPrice;
    private String phone ;

    public RequestListItems(ParseFile imageFile, String userName, String bookTitle, String bookPrice, String phone) {
        this.imageFile = imageFile;
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.bookPrice = bookPrice;
        this.phone = phone;
    }

    public ParseFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(ParseFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
