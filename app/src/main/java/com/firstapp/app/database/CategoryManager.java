package com.firstapp.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.firstapp.app.objects.Category;

import java.util.ArrayList;

public class CategoryManager {

    private static volatile CategoryManager INSTANCE = null;

    private static final String TABLE_NAME = "category";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";

    private CategoryManager() {}

    public static CategoryManager getInstance() {
        if(INSTANCE == null) {
            synchronized (CategoryManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CategoryManager();
                }
            }
        }
        return INSTANCE;
    }

    public String createTable() {
        return "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT)";
    }

    public void addNewCategory(String categoryName, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(NAME_COL, categoryName);
        db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<Category> allCategories(SQLiteDatabase db) {
        Cursor cursorCategories = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<Category> categoryModalArrayList = new ArrayList<>();

        if (cursorCategories.moveToFirst()) {
            do {
                categoryModalArrayList.add(new Category(
                        cursorCategories.getString(cursorCategories.getColumnIndexOrThrow(NAME_COL))));
            } while (cursorCategories.moveToNext());
        }
        cursorCategories.close();
        return categoryModalArrayList;
    }

    public int getCategoryId(SQLiteDatabase db, String category) {
        String query = "SELECT id FROM " + TABLE_NAME + " WHERE name=?";
        String[] selectionArgs = {category};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        int categoryId = -1;
        if (cursor.moveToFirst()) {
            categoryId = cursor.getInt(0);
        }
        cursor.close();
        return categoryId;
    }

    public void deleteCategory(SQLiteDatabase db, String name) {
        db.delete(TABLE_NAME, "name=?", new String[]{String.valueOf(name)});
    }

    public boolean hasBooksIn(SQLiteDatabase db, String categoryName) {
        return  booksNumber(db, categoryName) > 0;
    }

    public int booksNumber(SQLiteDatabase db, String categoryName) {
        String query = "SELECT COUNT(*) FROM book WHERE category = ?";
        String[] selectionArgs = {categoryName};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        return count;
    }
}
