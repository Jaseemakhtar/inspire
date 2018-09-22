package com.jsync.freebook.tabOne;

import com.parse.ParseFile;

/**
 * Created by JasS on 2/2/2018.
 */

public class BooksListItems {

   private String bookAuthor;
   private ParseFile bookImage;
   private String bookName;
   private String bookId;
   private String bookPrice;

    public BooksListItems(String bookAuthor, ParseFile bookImage, String bookName, String bookId, String bookPrice) {
        this.bookAuthor = bookAuthor;
        this.bookImage = bookImage;
        this.bookName = bookName;
        this.bookId = bookId;
        this.bookPrice = bookPrice;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public ParseFile getBookImage() {
        return bookImage;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookPrice() {
        return bookPrice;
    }
}
