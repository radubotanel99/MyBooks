package com.firstapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;
import com.firstapp.app.objects.Book;
import com.firstapp.app.objects.Category;

import java.util.ArrayList;
import java.util.Calendar;

public class AddBookActivity extends AppCompatActivity {

    private EditText titleEdt, authorEdt, publisherEdt, descriptionEdt, seriesEdt, volumeEdt, numberOfPagesEdt;
    private Spinner categorySpn;
    private CheckBox borrowedChk;
    private Button addBookBtn;
    private Database db;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private boolean isEditMode = false;
    private Book bookToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        initializeViews();
        initializeDatabase();
        initializeCategorySpinner();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("BOOK_TO_EDIT")) {
            isEditMode = true;
            bookToEdit = (Book) intent.getSerializableExtra("BOOK_TO_EDIT");
            fillFieldsForEditMode();
        }

        setupAddBookButton();
        initDatePicker();
        initDateButton();

    }

    private void fillFieldsForEditMode() {
        titleEdt.setText(bookToEdit.getTitle());
        authorEdt.setText(bookToEdit.getAuthor().getName());
        publisherEdt.setText(bookToEdit.getPublisher());
        descriptionEdt.setText(bookToEdit.getDescription());
        seriesEdt.setText(bookToEdit.getSeries());
        volumeEdt.setText(bookToEdit.getVolume());
        numberOfPagesEdt.setText(String.valueOf(bookToEdit.getPages()));
        borrowedChk.setChecked(bookToEdit.isBorrowed());

        // Set selected category in the spinner
        String selectedCategory = bookToEdit.getCategory().getName();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) categorySpn.getAdapter();
        int position = adapter.getPosition(selectedCategory);
        categorySpn.setSelection(position);
    }

    private void initializeViews() {
        titleEdt = findViewById(R.id.idEdtTitle);
        authorEdt = findViewById(R.id.idEdtAuthor);
        publisherEdt = findViewById(R.id.idEdtPublisher);
        categorySpn = findViewById(R.id.idSpnCategory);
        descriptionEdt = findViewById(R.id.idEdtDescription);
        seriesEdt = findViewById(R.id.idEdtSeries);
        volumeEdt = findViewById(R.id.idEdtVolume);
        numberOfPagesEdt = findViewById(R.id.idEdtNumberOfPages);
        borrowedChk = findViewById(R.id.idChkBorrowed);
        addBookBtn = findViewById(R.id.idBtnAddBook);
        dateButton = findViewById(R.id.datePickerButton);
    }

    private void initializeDatabase() {
        db = new Database(AddBookActivity.this);
    }

    private void initializeCategorySpinner() {
        ArrayList<Category> categories = db.allCategories();
        ArrayList<String> dropDownCategories = new ArrayList<>();
        dropDownCategories.add("Select category...");
        for (Category cat : categories) {
            dropDownCategories.add(cat.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dropDownCategories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpn.setAdapter(arrayAdapter);
    }

    private void setupAddBookButton() {
        if (isEditMode) {
            addBookBtn.setText("Update Book");
            addBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateBook();
                }
            });
        } else {
            addBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addBook();
                }
            });
        }
    }

    private void addBook() {
        String title = titleEdt.getText().toString();
        String author = authorEdt.getText().toString();
        String publisher = publisherEdt.getText().toString();
        String category = categorySpn.getSelectedItem().toString();
        String description = descriptionEdt.getText().toString();
        String publishedDate = dateButton.getText().toString();
        String series = seriesEdt.getText().toString();
        String volume = volumeEdt.getText().toString();
        int numberOfPages = getNumberOfPages();
        boolean borrowed = borrowedChk.isChecked();

        if (isInputInvalid(title, author, publisher, category)) {
            Toast.makeText(AddBookActivity.this, "Please enter the book..", Toast.LENGTH_SHORT).show();
            return;
        }

        db.addNewBook(title, author, publisher, category, description, series, volume, publishedDate, numberOfPages, borrowed);
        Toast.makeText(AddBookActivity.this, "The book has been added.", Toast.LENGTH_SHORT).show();
        resetFields();

        goToMyBooksActivity();
    }

    private void updateBook() {
        String title = titleEdt.getText().toString();
        String author = authorEdt.getText().toString();
        String publisher = publisherEdt.getText().toString();
        String category = categorySpn.getSelectedItem().toString();
        String description = descriptionEdt.getText().toString();
        String publishedDate = dateButton.getText().toString();
        String series = seriesEdt.getText().toString();
        String volume = volumeEdt.getText().toString();
        int numberOfPages = getNumberOfPages();
        boolean borrowed = borrowedChk.isChecked();

        if (isInputInvalid(title, author, publisher, category)) {
            Toast.makeText(AddBookActivity.this, "Please enter the book..", Toast.LENGTH_SHORT).show();
            return;
        }

        db.updateBook(bookToEdit.getId(), title, author,category, description, series, volume,  publisher, publishedDate, numberOfPages, borrowed);
        Toast.makeText(AddBookActivity.this, "The book has been updated.", Toast.LENGTH_SHORT).show();
        resetFields();

        goToMyBooksActivity();
    }

    private int getNumberOfPages() {
        int numberOfPages;
        try {
            numberOfPages = Integer.parseInt(numberOfPagesEdt.getText().toString());
        } catch (NumberFormatException e) {
            numberOfPages = 0;
        }
        return numberOfPages;
    }

    private boolean isInputInvalid(String title, String author, String publisher, String category) {
        return title.isEmpty() || author.isEmpty() || publisher.isEmpty() || category.equals("Select category...");
    }

    private void resetFields() {
        titleEdt.setText("");
        authorEdt.setText("");
        publisherEdt.setText("");
        categorySpn.setSelection(0);
        descriptionEdt.setText("");
        seriesEdt.setText("");
        volumeEdt.setText("");
        numberOfPagesEdt.setText("");
        borrowedChk.setChecked(false);
    }

    private void goToMyBooksActivity() {
        Intent intent = new Intent(this, ViewBooksActivity.class);
        startActivity(intent);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "JAN";
        }
    }

    public void initDateButton() {
        if (isEditMode) {
            dateButton.setText(bookToEdit.getPublishedDate());
        } else {
            dateButton.setText(getTodaysDate());
        }
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}
