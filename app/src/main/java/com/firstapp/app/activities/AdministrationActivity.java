package com.firstapp.app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class AdministrationActivity extends AppCompatActivity {

    private Button myBooksBtn;
    private Button addBookBtn;
    private Button addCategoryBtn;
    private Button allCategoriesBtn;
    private TextView booksCountTextView;
    private TextView booksReadCountTextView;
    private Database db;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);

        myBooksBtn = (Button) findViewById(R.id.myBooksBtn);
        myBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMyBooksActivity();
            }
        });

        addBookBtn = (Button) findViewById(R.id.addBookBtn);
        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddBookActivity();
            }
        });

        addCategoryBtn = (Button) findViewById(R.id.addCategoryBtn);
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddCategoryActivity();
            }
        });

        allCategoriesBtn = (Button) findViewById(R.id.allCategoriesBtn);
        allCategoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openViewCategoriesActivity();
            }
        });

        booksCountTextView = findViewById(R.id.booksCountTextView);
        booksReadCountTextView = findViewById(R.id.booksReadCountTextView);

        db = Database.getInstance(AdministrationActivity.this);

        int booksNumber = db.getBooksNUmber();
        if (booksNumber == 0 ) {
            booksCountTextView.setText("You don't have any books yet.");
        } else {
            booksCountTextView.setText("You have " + String.valueOf(booksNumber) + (booksNumber == 1 ? " book," : " books,")
                    +  " congratulations!");
        }

        int booksReadNumber = db.getBooksReadNumber();
        if (booksReadNumber == 0) {
            booksReadCountTextView.setText("You didn't read any of them yet.");
        } else {
            booksReadCountTextView.setText("You have read " + String.valueOf(booksReadNumber) + " of them until now.");
        }



    }

    public void openMyBooksActivity() {
        Intent intent = new Intent(this, ViewBooksActivity.class);
        startActivity(intent);
    }

    public void openAddBookActivity() {
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }

    public void openAddCategoryActivity() {
        Intent intent = new Intent(this, AddCategoryActivity.class);
        startActivity(intent);
    }

    public void openViewCategoriesActivity() {
        Intent intent = new Intent(this, ViewCategoriesActivity.class);
        startActivity(intent);
    }

}