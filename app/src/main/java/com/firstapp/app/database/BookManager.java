package com.firstapp.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.firstapp.app.objects.Author;
import com.firstapp.app.objects.Book;
import com.firstapp.app.objects.Category;

import java.util.ArrayList;

public class BookManager {

    private static final String TABLE_NAME = "book";
    private static final String ID_COL = "id";
    private static final String TITLE_COL = "title";
    private static final String AUTHOR_COL = "author";
    private static final String DESCRIPTION_COL = "description";
    private static final String SERIES_COL = "series";
    private static final String VOLUME_COL = "volume";
    private static final String CATEGORY_COL = "category";
    private static final String PUBLISHED_DATE_COL = "published_date";
    private static final String PUBLISHER_COL = "publisher";
    private static final String PAGES_COL = "pages";
    private static final String ISBN_COL = "isbn";
    private static final String BORROWED_COL = "borrowed";
    private static final String LENT_COL = "lent";
    private static final String IMAGE_COL = "image";
    private static volatile BookManager INSTANCE = null;

    private BookManager() {}

    public static BookManager getInstance() {
        if(INSTANCE == null) {
            synchronized (CategoryManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookManager();
                }
            }
        }
        return INSTANCE;
    }


    public String createTable() {
        return "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " TEXT, "
                + AUTHOR_COL + " TEXT, "
                + DESCRIPTION_COL + " TEXT, "
                + SERIES_COL + " TEXT, "
                + VOLUME_COL + " TEXT, "
                + CATEGORY_COL + " TEXT, "
                + PUBLISHED_DATE_COL + " TEXT, "
                + PUBLISHER_COL + " TEXT, "
                + PAGES_COL + " INTEGER, "
                + ISBN_COL + " TEXT, "
                + BORROWED_COL + " INTEGER, "
                + LENT_COL + " INTEGER, "
                + IMAGE_COL + " BOLB, "
                + "FOREIGN KEY (" + CATEGORY_COL + ") REFERENCES category (id) )";
    }

    public ArrayList<Book> allBooks(SQLiteDatabase db) {
        Cursor cursorBooks = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<Book> booksArrayList = new ArrayList<>();

        if (cursorBooks.moveToFirst()) {
            do {
                booksArrayList.add(createBookFromCursor(cursorBooks));
            } while (cursorBooks.moveToNext());
        }

        cursorBooks.close();
        return booksArrayList;
    }

    private Book createBookFromCursor(Cursor cursor) {
        int id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("id")));
        String title = getStringValue(cursor, TITLE_COL);
        Author author = new Author(getStringValue(cursor, AUTHOR_COL));
        String description = getStringValue(cursor, DESCRIPTION_COL);
        String series = getStringValue(cursor, SERIES_COL);
        String volume = getStringValue(cursor, VOLUME_COL);
        Category category = new Category(getStringValue(cursor, CATEGORY_COL));
        String publishedDate = getStringValue(cursor, PUBLISHED_DATE_COL);
        String publisher = getStringValue(cursor, PUBLISHER_COL);
        int numberOfPages = Integer.parseInt(getStringValue(cursor, PAGES_COL).equals("") ? "0" : getStringValue(cursor, PAGES_COL));
        String isbn = getStringValue(cursor, ISBN_COL);
        boolean borrowed = false;
        if (null != cursor.getString(cursor.getColumnIndexOrThrow(BORROWED_COL))) {
            borrowed = (cursor.getString(cursor.getColumnIndexOrThrow(BORROWED_COL))).equals("0") ? false : true;
        }
        boolean lent = false;
        if (null != cursor.getString(cursor.getColumnIndexOrThrow(LENT_COL))) {
            lent = (cursor.getString(cursor.getColumnIndexOrThrow(LENT_COL))).equals("0") ? false : true;
        }
        byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE_COL));

        return new Book(id, title, author, description, series, volume, category, publishedDate, publisher, numberOfPages, isbn, borrowed, lent, image);
    }

    private String getStringValue(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        String value = cursor.getString(columnIndex);
        return value != null ? value : "";
    }

    public void deleteBook(SQLiteDatabase db, int idBook) {
        db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(idBook)});
    }

    public void addNewBook(SQLiteDatabase db, String title, String author, String publisher, String category, String description, String series, String volume, String publishedDate, int numberOfPages, boolean borrowed, byte[] imageBytes) {
        ContentValues values = new ContentValues();
        values.put(TITLE_COL, title);
        values.put(AUTHOR_COL, author);
        values.put(DESCRIPTION_COL, description);
        values.put(SERIES_COL, series);
        values.put(VOLUME_COL, volume);
        values.put(CATEGORY_COL, category);
        values.put(PUBLISHED_DATE_COL, publishedDate);
        values.put(PUBLISHER_COL, publisher);
        values.put(PAGES_COL, numberOfPages);
        values.put(ISBN_COL, "");
        values.put(BORROWED_COL, borrowed);
        values.put(LENT_COL, "");
        values.put(IMAGE_COL, imageBytes);
        db.insert(TABLE_NAME, null, values);
    }

    public void updateBook(SQLiteDatabase db, int id, String updatedTitle, String updatedAuthor,
                           String updatedCategory, String updatedDescription, String updatedSeries,
                           String updatedVolume, String updatedPublisher, String updatedPublishedDate,
                           int updatedPages,
                           boolean updatedIsBorrowed) {

        ContentValues values = new ContentValues();

        values.put(TITLE_COL, updatedTitle);
        values.put(AUTHOR_COL, updatedAuthor);
        values.put(DESCRIPTION_COL, updatedDescription);
        values.put(SERIES_COL, updatedSeries);
        values.put(VOLUME_COL, updatedVolume);
        values.put(CATEGORY_COL, updatedCategory);
        values.put(PUBLISHED_DATE_COL, updatedPublishedDate);
        values.put(PUBLISHER_COL, updatedPublisher);
        values.put(PAGES_COL, updatedPages);
        values.put(ISBN_COL, "");
        values.put(BORROWED_COL, updatedIsBorrowed);
        values.put(LENT_COL, "");

        db.update(TABLE_NAME, values, "id=?", new String[]{String.valueOf(id)});
    }

    public void dropTable(SQLiteDatabase db) {
//        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
