package com.firstapp.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.firstapp.app.objects.Book;
import com.firstapp.app.objects.Category;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "library";
    private static final int DB_VERSION = 1;

    private CategoryManager categoryManager = CategoryManager.getInstance();
    private BookManager bookManager = BookManager.getInstance();

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String qyeryCategoryTable = categoryManager.createTable();
        db.execSQL(qyeryCategoryTable);

        String queryBookTable = bookManager.createTable();
        db.execSQL(queryBookTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addNewCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        categoryManager.addNewCategory(categoryName, db);
        db.close();
    }

    public ArrayList<Category> allCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return categoryManager.allCategories(db);
    }

    public void addNewBook(String title, String author, String publisher, String category,
                           String description, String series, String volume,
                           String publishedDate, int numberOfPages, boolean borrowed) {
        SQLiteDatabase db = this.getReadableDatabase();
        bookManager.addNewBook(db, title, author, publisher, category, description, series, volume, publishedDate, numberOfPages, borrowed);
        db.close();
    }

    public ArrayList<Book> allBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return bookManager.allBooks(db);
    }

    public void deleteBook(int idBook) {
        SQLiteDatabase db = this.getReadableDatabase();
        bookManager.deleteBook(db, idBook);
        db.close();
    }

    public void updateBook(int id, String updatedTitle, String updatedAuthor, String category, String updatedDescription, String updatedSeries, String updatedVolume, String updatedPublisher, String updatedPublishedDate, int updatedPages, boolean updatedIsBorrowed) {
        SQLiteDatabase db = this.getReadableDatabase();
        bookManager.updateBook(db, id, updatedTitle, updatedAuthor, category, updatedDescription, updatedSeries,
                updatedVolume, updatedPublisher, updatedPublishedDate, updatedPages, updatedIsBorrowed);
        db.close();
    }

    public int getCategoryId(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return categoryManager.getCategoryId(db, category);
    }

    public void deleteCategory(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        categoryManager.deleteCategory(db, name);
        db.close();
    }

    public boolean hasBooksInCategory(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return categoryManager.hasBooksIn(db, categoryName);
    }

    public int booksNumber(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return categoryManager.booksNumber(db, categoryName);
    }
}
