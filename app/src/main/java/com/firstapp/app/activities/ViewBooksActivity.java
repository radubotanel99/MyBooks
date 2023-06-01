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
import com.firstapp.app.objects.Book;
import com.firstapp.app.views.BookView;

import java.util.ArrayList;

public class ViewBooksActivity extends AppCompatActivity {

    private ArrayList<Book> booksArrayList;
    private Database db;
    private BookView bookView;
    private RecyclerView booksRV;
    private ImageButton addBookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        booksArrayList = new ArrayList<>();
        db = new Database(ViewBooksActivity.this);
        booksArrayList = db.allBooks();
        bookView = new BookView(booksArrayList, ViewBooksActivity.this);
        booksRV = findViewById(R.id.idRVBooks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewBooksActivity.this, RecyclerView.VERTICAL, false);
        booksRV.setLayoutManager(linearLayoutManager);
        booksRV.setAdapter(bookView);

        addBookButton = findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddBookActivity();
            }
        });
    }

    public void openAddBookActivity() {
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }
}