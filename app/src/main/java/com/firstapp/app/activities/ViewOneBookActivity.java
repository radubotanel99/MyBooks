package com.firstapp.app.activities;


import static com.firstapp.app.helperclasses.GeneralConstants.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;
import com.firstapp.app.objects.Author;
import com.firstapp.app.objects.Book;
import com.firstapp.app.objects.Category;

import java.io.File;

public class ViewOneBookActivity extends AbstractActivity {
    private TextView titleTextView, authorTextView, descriptionTextView, copiesTextView,
            lentToTextView, categoryTextView, publishedDateTextView, publisherTextView,
            pagesTextView, digitalTextView, lentTextView, readTextView;
    private ImageView bookImageView;
    private ImageButton deleteBookButton, editBookButton;
    private Database db = new Database(ViewOneBookActivity.this);
    private int idBook;
    private String title, author, description, lentTo, category, publishedDate, publisher;
    private int copies, pages;
    private boolean isDigital, isLent, isRead;
    private String imagePath;

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
        copiesTextView = findViewById(R.id.copiesTextView);
        lentToTextView = findViewById(R.id.lentToTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        publishedDateTextView = findViewById(R.id.publishedDateTextView);
        publisherTextView = findViewById(R.id.publisherTextView);
        pagesTextView = findViewById(R.id.pagesTextView);
        digitalTextView = findViewById(R.id.digitalTextView);
        lentTextView = findViewById(R.id.lentTextView);
        readTextView = findViewById(R.id.readTextView);
        bookImageView = findViewById(R.id.bookImageView);
        deleteBookButton = findViewById(R.id.deleteBookButton);
        editBookButton = findViewById(R.id.editBookButton);

        descriptionTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    private void getBookDataFromIntent() {
        idBook = getIntent().getIntExtra(ID, 0);
        title = getIntent().getStringExtra(TITLE);
        author = getIntent().getStringExtra(AUTHOR);
        description = getIntent().getStringExtra(DESCRIPTION);
        copies = getIntent().getIntExtra(COPIES, 0);
        lentTo = getIntent().getStringExtra(LENT_TO);
        category = getIntent().getStringExtra(CATEGORY);
        publishedDate = getIntent().getStringExtra(PUBLISHED_DATE);
        publisher = getIntent().getStringExtra(PUBLISHER);
        pages = getIntent().getIntExtra(PAGE_COUNT, 0);
        isDigital = getIntent().getBooleanExtra(IS_DIGITAL, false);
        isLent = getIntent().getBooleanExtra(IS_LENT, false);
        isRead = getIntent().getBooleanExtra(IS_READ, false);
        imagePath = getIntent().getStringExtra(IMAGE);
    }

    private void displayBookInformation() {
        titleTextView.setText(title == null || title.isEmpty() ? "N/A" : title);
        authorTextView.setText(author == null || author.isEmpty() ? "N/A" : author);
        descriptionTextView.setText(description == null || description.isEmpty() ? "N/A" : description);
        copiesTextView.setText("Nr of copies: " + copies);
        categoryTextView.setText("Category: " + (category == null || category.isEmpty() ? "N/A" : category));
        publishedDateTextView.setText("Published Date:\n" + (publishedDate == null || publishedDate.isEmpty() ? "N/A" : publishedDate));
        publisherTextView.setText("Publisher:" + (publisher == null || publisher.isEmpty() ? "N/A" : publisher));
        pagesTextView.setText("Nr of pages:" + (pages == 0 ? "N/A" : String.valueOf(pages)));
        digitalTextView.setText(isDigital ? "eBook" : "Physical Book");
        lentTextView.setText(isLent ? "Lent" : "Not Lent");
        if (lentTextView.getText().equals("Lent")) {
            lentToTextView.setText("Lent to: " + (lentTo == null || lentTo.isEmpty() ? "N/A" : lentTo));
            lentToTextView.setVisibility(View.VISIBLE);
        } else {
            lentToTextView.setVisibility(View.GONE);
        }
        readTextView.setText(isRead ? "Read" : "Unread");

        if (null != imagePath && !imagePath.equals("")) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
            bookImageView.setImageBitmap(imageBitmap);
        }
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
                        File imageFileToDelete = new File(imagePath);
                        if (imageFileToDelete.exists()) {
                            imageFileToDelete.delete();
                        }
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

                Book bookToEdit = new Book(idBook, title, new Author(author), description, copies, lentTo,
                        new Category(category), publishedDate, publisher, pages, "", isDigital,
                        isLent, isRead, imagePath);
                intent.putExtra(BOOK_TO_EDIT, bookToEdit);
                startActivity(intent);
            }
        });
    }
}
