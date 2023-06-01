package com.firstapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firstapp.app.R;

public class AdministrationActivity extends AppCompatActivity {

    private Button myBooksBtn;
    private Button addBookBtn;
    private Button addCategoryBtn;
    private Button allCategoriesBtn;

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