package com.firstapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText categoryNameEdt;
    private Button addCategoryBtn;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryNameEdt = findViewById(R.id.idEdtCategoryName);
        addCategoryBtn = findViewById(R.id.idBtnAddCategory);
        db = new Database(AddCategoryActivity.this);

        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = categoryNameEdt.getText().toString();
                if (categoryName.isEmpty()) {
                    Toast.makeText(AddCategoryActivity.this, "Please enter the category name..", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (db.getCategoryId(categoryName) != -1) {
                    Toast.makeText(AddCategoryActivity.this, "This category already exists..", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.addNewCategory(categoryName);
                Toast.makeText(AddCategoryActivity.this, "Category has been added.", Toast.LENGTH_SHORT).show();
                categoryNameEdt.setText("");

                goToCategoriesActivity();
            }
        });
    }

    private void goToCategoriesActivity() {
        Intent intent = new Intent(this, ViewCategoriesActivity.class);
        startActivity(intent);
    }
}