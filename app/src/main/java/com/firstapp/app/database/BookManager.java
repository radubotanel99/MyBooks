package com.firstapp.app.database;

import android.annotation.SuppressLint;
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
    private static final String COPIES_COL = "copies";
    private static final String LENT_TO_COL = "lentTo";
    private static final String CATEGORY_COL = "category";
    private static final String PUBLISHED_DATE_COL = "published_date";
    private static final String PUBLISHER_COL = "publisher";
    private static final String PAGES_COL = "pages";
    private static final String ISBN_COL = "isbn";
    private static final String DIGITAL_COL = "digital";
    private static final String LENT_COL = "lent";
    private static final String READ_COL = "read";
    private static final String IMAGE_COL = "image";
    private static final String[] COLUMNS = {ID_COL, TITLE_COL, AUTHOR_COL, DESCRIPTION_COL, COPIES_COL, LENT_TO_COL,
        CATEGORY_COL, PUBLISHED_DATE_COL, PUBLISHER_COL, PAGES_COL, ISBN_COL, DIGITAL_COL, LENT_COL, READ_COL,
        IMAGE_COL};
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
                + COPIES_COL + " INTEGER, "
                + LENT_TO_COL + " TEXT, "
                + CATEGORY_COL + " TEXT, "
                + PUBLISHED_DATE_COL + " TEXT, "
                + PUBLISHER_COL + " TEXT, "
                + PAGES_COL + " INTEGER, "
                + ISBN_COL + " TEXT, "
                + DIGITAL_COL + " INTEGER, "
                + LENT_COL + " INTEGER, "
                + READ_COL + " INTEGER, "
                + IMAGE_COL + " TEXT, "
                + "FOREIGN KEY (" + CATEGORY_COL + ") REFERENCES category (id) )";
    }

    public ArrayList<Book> allBooks(SQLiteDatabase db) {
        Cursor cursorBooks = db.rawQuery("SELECT * FROM " + TABLE_NAME + " order by " + TITLE_COL, null);
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
        int copies = Integer.parseInt(getStringValue(cursor, COPIES_COL).equals("") ? "0" : getStringValue(cursor, COPIES_COL));
        String lentTo = getStringValue(cursor, LENT_TO_COL);
        Category category = new Category(getStringValue(cursor, CATEGORY_COL));
        String publishedDate = getStringValue(cursor, PUBLISHED_DATE_COL);
        String publisher = getStringValue(cursor, PUBLISHER_COL);
        int numberOfPages = Integer.parseInt(getStringValue(cursor, PAGES_COL).equals("") ? "0" : getStringValue(cursor, PAGES_COL));
        String isbn = getStringValue(cursor, ISBN_COL);
        boolean digital = false;
        if (null != cursor.getString(cursor.getColumnIndexOrThrow(DIGITAL_COL))) {
            digital = (cursor.getString(cursor.getColumnIndexOrThrow(DIGITAL_COL))).equals("0") ? false : true;
        }
        boolean lent = false;
        if (null != cursor.getString(cursor.getColumnIndexOrThrow(LENT_COL))) {
            lent = (cursor.getString(cursor.getColumnIndexOrThrow(LENT_COL))).equals("0") ? false : true;
        }
        boolean read = false;
        if (null != cursor.getString(cursor.getColumnIndexOrThrow(READ_COL))) {
            read = (cursor.getString(cursor.getColumnIndexOrThrow(READ_COL))).equals("0") ? false : true;
        }
        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_COL));

        return new Book(id, title, author, description, copies, lentTo, category, publishedDate, publisher,
                numberOfPages, isbn, digital, lent, read, imagePath);
    }

    private String getStringValue(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        String value = cursor.getString(columnIndex);
        return value != null ? value : "";
    }

    public void deleteBook(SQLiteDatabase db, int idBook) {
        db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(idBook)});
    }

    public void addNewBook(SQLiteDatabase db, String title, String author, String publisher, String category, String description,
                           int copies, String lentTo, String publishedDate, int numberOfPages, boolean digital, boolean lent,
                           boolean read, String imagePath) {
        ContentValues values = new ContentValues();
        values.put(TITLE_COL, title);
        values.put(AUTHOR_COL, author);
        values.put(DESCRIPTION_COL, description);
        values.put(COPIES_COL, copies);
        values.put(LENT_TO_COL, lentTo);
        values.put(CATEGORY_COL, category);
        values.put(PUBLISHED_DATE_COL, publishedDate);
        values.put(PUBLISHER_COL, publisher);
        values.put(PAGES_COL, numberOfPages);
        values.put(ISBN_COL, "");
        values.put(DIGITAL_COL, digital);
        values.put(LENT_COL, lent);
        values.put(READ_COL, read);
        values.put(IMAGE_COL, imagePath);
        db.insert(TABLE_NAME, null, values);
    }

    public void updateBook(SQLiteDatabase db, int id, String updatedTitle, String updatedAuthor,
                           String updatedCategory, String updatedDescription, String updatedCopies,
                           String updatedLentTo, String updatedPublisher, String updatedPublishedDate,
                           int updatedPages,
                           boolean updatedIsDigital, boolean updateIsLent, boolean updateIsRead, String updatedImagePath) {

        ContentValues values = new ContentValues();

        values.put(TITLE_COL, updatedTitle);
        values.put(AUTHOR_COL, updatedAuthor);
        values.put(DESCRIPTION_COL, updatedDescription);
        values.put(COPIES_COL, updatedCopies);
        values.put(LENT_TO_COL, updatedLentTo);
        values.put(CATEGORY_COL, updatedCategory);
        values.put(PUBLISHED_DATE_COL, updatedPublishedDate);
        values.put(PUBLISHER_COL, updatedPublisher);
        values.put(PAGES_COL, updatedPages);
        values.put(ISBN_COL, "");
        values.put(DIGITAL_COL, updatedIsDigital);
        values.put(LENT_COL, updateIsLent);
        values.put(READ_COL, updateIsRead);
        values.put(IMAGE_COL, updatedImagePath);

        db.update(TABLE_NAME, values, "id=?", new String[]{String.valueOf(id)});
    }

    public void dropTable(SQLiteDatabase db) {
//        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public int getBooksNumber(SQLiteDatabase db) {
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        return count;
    }

    public int getBooksReadNumber(SQLiteDatabase db) {
        return getBooksWithPropertyNumber(db, READ_COL);
    }

    public int getBooksDigitalNumber(SQLiteDatabase db) {
        return getBooksWithPropertyNumber(db, DIGITAL_COL);
    }

    public int getBooksLentNumber(SQLiteDatabase db) {
        return getBooksWithPropertyNumber(db, LENT_COL);
    }

    private int getBooksWithPropertyNumber(SQLiteDatabase db, String colName) {
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME  + " WHERE " + colName + "=1";
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        return count;
    }

    public Book getDuplicate(SQLiteDatabase db, String title, String author, String publisher, int pageCount) {
        Book book = null;

        String selection = TITLE_COL + " = ? AND " + AUTHOR_COL + " = ? AND " + PUBLISHER_COL + " = ? AND " + PAGES_COL + " = ?";
        String[] selectionArgs = {title, author, publisher, String.valueOf(pageCount)};

        if (publisher == null) {
            selection = TITLE_COL + " = ? AND " + AUTHOR_COL + " = ? AND " + PAGES_COL + " = ?";
            selectionArgs = new String[]{title, author, String.valueOf(pageCount)};
        }

        Cursor cursor = db.query(TABLE_NAME, COLUMNS, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            book = createBookFromCursor(cursor);
        }
        return book;
    }

    public ArrayList<String> getAuthors(SQLiteDatabase db) {
        ArrayList<String> authorsList = new ArrayList<>();
        String query = "SELECT DISTINCT " + AUTHOR_COL + " FROM " + TABLE_NAME + " order by " + AUTHOR_COL;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(AUTHOR_COL));
                authorsList.add(author);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return authorsList;
    }

    public ArrayList<Book> getFilteredBooks(SQLiteDatabase db, boolean authorFilterIsChecked, boolean categoryFilterIsChecked,
                                            boolean readFilterIsChecked, boolean lentFilterIsChecked, boolean readRdBtnChecked,
                                            boolean lentRdBtnChecked, String authorSelected, String categorySelected) {

        ArrayList<Book> filteredBooks = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM book WHERE 1 = 1 ");
        if (authorFilterIsChecked) {
            query.append("AND author = '").append(authorSelected).append("' ");
        }

        if (categoryFilterIsChecked) {
            query.append("AND category = '").append(categorySelected).append("' ");
        }

        if (readFilterIsChecked) {
            int readValue = readRdBtnChecked ? 1 : 0;
            query.append("AND read = ").append(readValue).append(" ");
        }

        if (lentFilterIsChecked) {
            int lentValue = lentRdBtnChecked ? 1 : 0;
            query.append("AND lent = ").append(lentValue).append(" ");
        }

        query.append("order by " + TITLE_COL);

        Cursor cursor = db.rawQuery(query.toString(), null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve book details from the cursor and add to filteredBooks list
                // Assuming Book class has a constructor to initialize with cursor values
                Book book = createBookFromCursor(cursor);
                filteredBooks.add(book);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return filteredBooks;
    }


    public String createVirtualTable() {
        return "CREATE TABLE " + "new_book" + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " TEXT, "
                + AUTHOR_COL + " TEXT, "
                + DESCRIPTION_COL + " TEXT, "
                + COPIES_COL + " INTEGER, "
                + LENT_TO_COL + " TEXT, "
                + CATEGORY_COL + " TEXT, "
                + PUBLISHED_DATE_COL + " TEXT, "
                + PUBLISHER_COL + " TEXT, "
                + PAGES_COL + " INTEGER, "
                + ISBN_COL + " TEXT, "
                + DIGITAL_COL + " INTEGER, "
                + LENT_COL + " INTEGER, "
                + READ_COL + " INTEGER, "
                + IMAGE_COL + " TEXT, "
                + "FOREIGN KEY (" + CATEGORY_COL + ") REFERENCES category (id) )";
    }

    public String insertInVirtualTable() {
        return "INSERT INTO new_book (" +
                TITLE_COL + ", "
                + AUTHOR_COL + ", "
                + DESCRIPTION_COL + ", "
                + COPIES_COL + ", "
                + LENT_TO_COL + ", "
                + CATEGORY_COL + ", "
                + PUBLISHED_DATE_COL + ", "
                + PUBLISHER_COL + ", "
                + PAGES_COL + ", "
                + ISBN_COL + ", "
                + DIGITAL_COL + ", "
                + LENT_COL + ", "
                + READ_COL + ", "
                + IMAGE_COL +
                ") SELECT " +
                TITLE_COL + ", "
                + AUTHOR_COL + ", "
                + DESCRIPTION_COL + ", "
                + COPIES_COL + ", "
                + LENT_TO_COL + ", "
                + CATEGORY_COL + ", "
                + PUBLISHED_DATE_COL + ", "
                + PUBLISHER_COL + ", "
                + PAGES_COL + ", "
                + ISBN_COL + ", "
                + DIGITAL_COL + ", "
                + LENT_COL + ", "
                + READ_COL + ", "
                + IMAGE_COL +
                " FROM " + TABLE_NAME;
    }

}
