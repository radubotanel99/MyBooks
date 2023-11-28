package com.firstapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;

public class StatsActivity extends AppCompatActivity {

    private TextView booksCountTextView;
    private TextView booksReadCountTextView;
    private TextView booksBorrowedCountTextView;
    private TextView booksLentCountTextView;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        booksCountTextView = findViewById(R.id.booksCountTextView);
        booksReadCountTextView = findViewById(R.id.booksReadCountTextView);
        booksBorrowedCountTextView = findViewById(R.id.booksBorrowedCountTextView);
        booksLentCountTextView = findViewById(R.id.booksLentCountTextView);

        db = Database.getInstance(StatsActivity.this);

        booksCountTextView.setText("Total number of your books: " + String.valueOf(db.getBooksNUmber()));
        booksReadCountTextView.setText("You have read " + String.valueOf(db.getBooksReadNumber()) + " of them until now.");
        booksBorrowedCountTextView.setText("Number of books borrowed: " + String.valueOf(db.getBooksBorrowedNumber()));
        booksLentCountTextView.setText("Number of books lent: " + String.valueOf(db.getBooksLentNumber()));

    }
}