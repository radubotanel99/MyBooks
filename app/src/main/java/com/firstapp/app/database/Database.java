package com.firstapp.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.firstapp.app.objects.Book;
import com.firstapp.app.objects.Category;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "library";
    private static final int DB_VERSION = 9;

    private CategoryManager categoryManager = CategoryManager.getInstance();
    private BookManager bookManager = BookManager.getInstance();
    private static volatile Database INSTANCE = null;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static Database getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Database(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db = getReadableDatabase();
        String qyeryCategoryTable = categoryManager.createTable();
        db.execSQL(qyeryCategoryTable);

        String queryBookTable = bookManager.createTable();
        db.execSQL(queryBookTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("CREATE TABLE new_table_name AS SELECT id, title, author," +
                        " description, series AS copies, volume AS lentTo, category, published_date, publisher, pages, isbn, " +
                        " borrowed AS digital, lent, read, image FROM book;");
                db.execSQL("DROP TABLE book;");
                db.execSQL("ALTER TABLE new_table_name RENAME TO book;");

                db.execSQL(bookManager.createVirtualTable());
                db.execSQL(bookManager.insertInVirtualTable());
                db.execSQL("DROP TABLE book");
                db.execSQL("ALTER TABLE new_book RENAME TO book;");

                db.execSQL("UPDATE book SET copies=1");
                break;
            case 8:
                break;
        }
//        dropTables(db);
//        onCreate(db);
    }

    private void dropTables(SQLiteDatabase db) {
        bookManager.dropTable(db);
        categoryManager.dropTable(db);
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

    public ArrayList<String> getAuthors() {
        SQLiteDatabase db = this.getWritableDatabase();
        return bookManager.getAuthors(db);
    }

    public void addNewBook(String title, String author, String publisher, String category,
                           String description, int copies, String lentTo,
                           String publishedDate, int numberOfPages, boolean digital, boolean lent,
                           boolean read, String imagePath) {
        SQLiteDatabase db = this.getReadableDatabase();
        bookManager.addNewBook(db, title, author, publisher, category, description, copies, lentTo, publishedDate, numberOfPages, digital, lent, read, imagePath);
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

    public void updateBook(int id, String updatedTitle, String updatedAuthor, String category, String updatedDescription,
                           String updatedCopies, String updatedLentTo, String updatedPublisher, String updatedPublishedDate,
                           int updatedPages, boolean updatedIsDigital, boolean updateIsLent,
                           boolean updateIsRent, String updatedImagePath) {
        SQLiteDatabase db = this.getReadableDatabase();
        bookManager.updateBook(db, id, updatedTitle, updatedAuthor, category, updatedDescription, updatedCopies,
                updatedLentTo, updatedPublisher, updatedPublishedDate, updatedPages, updatedIsDigital, updateIsLent,
                updateIsRent, updatedImagePath);
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

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        dropTables(db);
        onCreate(db);
        db.close();
    }

    public int getBooksNUmber() {
        SQLiteDatabase db = this.getWritableDatabase();
        return bookManager.getBooksNumber(db);
    }

    public int getBooksReadNumber() {
        SQLiteDatabase db = this.getWritableDatabase();
        return bookManager.getBooksReadNumber(db);
    }

    public int getBooksDigitalNumber() {
        SQLiteDatabase db = this.getWritableDatabase();
        return bookManager.getBooksDigitalNumber(db);
    }

    public int getBooksLentNumber() {
        SQLiteDatabase db = this.getWritableDatabase();
        return bookManager.getBooksLentNumber(db);
    }

    public Book getDuplicate(String title, String author, String publisher, int pageCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        return bookManager.getDuplicate(db, title, author, publisher, pageCount);
    }

    public void alterDatabase() {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE FROM book WHERE id is NULL");
//        String sql = "ALTER TABLE book ADD COLUMN read INTEGER DEFAULT 0;";
//        db.execSQL(sql);
//        db.close();
    }

    public ArrayList<Book> getFilteredBooks(boolean authorFilterIsChecked, boolean categoryFilterIsChecked,
                                            boolean readFilterIsChecked, boolean lentFilterIsChecked, boolean readRdBtnChecked,
                                            boolean lentRdBtnChecked, String authorSelected, String categorySelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        return bookManager.getFilteredBooks(db, authorFilterIsChecked, categoryFilterIsChecked, readFilterIsChecked,
                lentFilterIsChecked, readRdBtnChecked, lentRdBtnChecked, authorSelected, categorySelected);
    }
}
