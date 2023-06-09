package com.firstapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;
import com.firstapp.app.objects.Author;
import com.firstapp.app.objects.Book;
import com.firstapp.app.objects.Category;

import java.util.Date;

public class ViewOneBookActivity extends AppCompatActivity {
    private TextView titleTextView, authorTextView, descriptionTextView, seriesTextView,
            volumeTextView, categoryTextView, publishedDateTextView, publisherTextView,
            pagesTextView, borrowTextView, lentTextView;
    private ImageButton deleteBookButton, editBookButton;
    private Database db = new Database(ViewOneBookActivity.this);
    private int idBook;
    private String title, author, description, series, volume, category, publishedDate, publisher;
    private int pages;
    private boolean isBorrowed, isLent;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one_book);

        initializeViews();
        getBookDataFromIntent();
        displayBookInformation();
        setupDeleteButton();
        setupEditButton();
    }

    private void initializeViews() {
        titleTextView = findViewById(R.id.titleTextView);
        authorTextView = findViewById(R.id.authorTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        seriesTextView = findViewById(R.id.seriesTextView);
        volumeTextView = findViewById(R.id.volumeTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        publishedDateTextView = findViewById(R.id.publishedDateTextView);
        publisherTextView = findViewById(R.id.publisherTextView);
        pagesTextView = findViewById(R.id.pagesTextView);
        borrowTextView = findViewById(R.id.borrowedTextView);
        lentTextView = findViewById(R.id.lentTextView);
        deleteBookButton = findViewById(R.id.deleteBookButton);
        editBookButton = findViewById(R.id.editBookButton);
    }

    private void getBookDataFromIntent() {
        idBook = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        description = getIntent().getStringExtra("description");
        series = getIntent().getStringExtra("series");
        volume = getIntent().getStringExtra("volume");
        category = getIntent().getStringExtra("category");
        publishedDate = getIntent().getStringExtra("publishedDate");
        publisher = getIntent().getStringExtra("publisher");
        pages = getIntent().getIntExtra("pages", 0);
        isBorrowed = getIntent().getBooleanExtra("isBorrowed", false);
        isLent = getIntent().getBooleanExtra("isLent", false);
        image = getIntent().getByteArrayExtra("image");
    }

    private void displayBookInformation() {
        titleTextView.setText(title == null || title.isEmpty() ? "N/A" : title);
        authorTextView.setText(author == null || author.isEmpty() ? "N/A" : author);
        descriptionTextView.setText(description == null || description.isEmpty() ? "N/A" : description);
        seriesTextView.setText(series == null || series.isEmpty() ? "N/A" : series);
        volumeTextView.setText(volume == null || volume.isEmpty() ? "N/A" : volume);
        categoryTextView.setText(category == null || category.isEmpty() ? "N/A" : category);
        publishedDateTextView.setText(publishedDate == null || publishedDate.isEmpty() ? "N/A" : publishedDate);
        publisherTextView.setText(publisher == null || publisher.isEmpty() ? "N/A" : publisher);
        pagesTextView.setText(pages == 0 ? "N/A" : String.valueOf(pages));
        borrowTextView.setText(isBorrowed ? "Borrowed" : "Not Borrowed");
        lentTextView.setText(isLent ? "Lent" : "Not Lent");
    }

    private void setupDeleteButton() {
        deleteBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewOneBookActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to delete this book?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteBook(idBook);
                        Toast.makeText(ViewOneBookActivity.this, "Book has been deleted", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ViewOneBookActivity.this, ViewBooksActivity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void setupEditButton() {
        editBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOneBookActivity.this, AddBookActivity.class);

                Book bookToEdit = new Book(idBook, title, new Author(author), description, series, volume, new Category(category), publishedDate, publisher, pages, "", isBorrowed, false, image);
                intent.putExtra("BOOK_TO_EDIT", bookToEdit);
                startActivity(intent);
            }
        });
    }
}
