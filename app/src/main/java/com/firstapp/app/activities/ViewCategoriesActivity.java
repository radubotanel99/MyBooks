package com.firstapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;
import com.firstapp.app.objects.Category;
import com.firstapp.app.views.CategoryView;

import java.util.ArrayList;

public class ViewCategoriesActivity extends AppCompatActivity {

    private ArrayList<Category> categoriesArrayList;
    private Database db;
    private CategoryView categoryView;
    private RecyclerView categoryRV;
    private ImageButton addCategoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_categories);

        categoriesArrayList = new ArrayList<>();
        db = new Database(ViewCategoriesActivity.this);
        categoriesArrayList = db.allCategories();

        categoryView = new CategoryView(categoriesArrayList, ViewCategoriesActivity.this);
        categoryRV = findViewById(R.id.idRVCategories);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewCategoriesActivity.this, RecyclerView.VERTICAL, false);
        categoryRV.setLayoutManager(linearLayoutManager);
        categoryRV.setAdapter(categoryView);

        addCategoryButton = findViewById(R.id.addCatButton);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddCategoriesActivity();
            }
        });
    }

    public void openAddCategoriesActivity() {
        Intent intent = new Intent(this, AddCategoryActivity.class);
        startActivity(intent);
    }
}