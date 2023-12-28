package com.firstapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;

public class StatsActivity extends AbstractActivity {

    private TextView booksCountTextView;
    private TextView booksReadCountTextView;
    private TextView booksDigitalCountTextView;
    private TextView booksLentCountTextView;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        booksCountTextView = findViewById(R.id.booksCountTextView);
        booksReadCountTextView = findViewById(R.id.booksReadCountTextView);
        booksDigitalCountTextView = findViewById(R.id.booksDigitalCountTextView);
        booksLentCountTextView = findViewById(R.id.booksLentCountTextView);

        db = Database.getInstance(StatsActivity.this);

        booksCountTextView.setText("Total number of your books: " + String.valueOf(db.getBooksNUmber()));
        booksReadCountTextView.setText("You have read " + String.valueOf(db.getBooksReadNumber()) + " of them until now.");
        booksDigitalCountTextView.setText("Number of eBooks: " + String.valueOf(db.getBooksDigitalNumber()));
        booksLentCountTextView.setText("Number of books lent: " + String.valueOf(db.getBooksLentNumber()));

    }
}