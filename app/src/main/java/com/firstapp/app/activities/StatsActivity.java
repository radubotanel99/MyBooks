package com.firstapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;

import org.eazegraph.lib.charts.PieChart;

import java.util.ArrayList;

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

        int booksNumber = db.getBooksNUmber();
        int readBooks = db.getBooksReadNumber();

//        int percentOfReadBooks = booksNumber > 0 ? (readBooks / booksNumber) * 100 : 0;
        int percentOfReadBooks = 0;
        if (booksNumber > 0) {
            percentOfReadBooks = (readBooks * 100) / booksNumber;
        }

        booksCountTextView.setText("Total number of books: " + String.valueOf(booksNumber));
        booksReadCountTextView.setText("Total number of read books " + String.valueOf(readBooks) + " (" + String.valueOf(percentOfReadBooks) + "%).");
        booksDigitalCountTextView.setText("Total number of eBooks: " + String.valueOf(db.getBooksDigitalNumber()));
        booksLentCountTextView.setText("Total number of lent books: " + String.valueOf(db.getBooksLentNumber()));
    }
}