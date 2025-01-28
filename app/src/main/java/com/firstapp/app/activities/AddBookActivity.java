package com.firstapp.app.activities;

import static com.firstapp.app.helperclasses.GeneralConstants.BOOK_TO_EDIT;
import static com.firstapp.app.helperclasses.GeneralConstants.SELECT_CATEGORY;

import androidx.annotation.Nullable;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;
import com.firstapp.app.objects.Book;
import com.firstapp.app.objects.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class AddBookActivity extends AbstractActivity {

    private EditText titleEdt, authorEdt, publisherEdt, descriptionEdt, copiesEdt, lentToEdt, numberOfPagesEdt;
    private Spinner categorySpn;
    private CheckBox digitalChk, lentChk, readChk;
    private Button addBookBtn;
    private Database db;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private boolean isEditMode = false;
    private Book bookToEdit;

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView bookImage;
    private Button uploadImageButton;

    private Uri imageUri;

    private int idBook;
    private String title, author, description, lentTo, category, publishedDate, publisher;
    private int copies, pages;
    private boolean isDigital, isLent, isRead;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        initializeViews();
        initializeDatabase();
        initializeCategorySpinner();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(BOOK_TO_EDIT)) {
            isEditMode = true;
            bookToEdit = (Book) intent.getSerializableExtra(BOOK_TO_EDIT);
            fillFieldsForEditMode();
        }
//        if (intent != null && intent.hasExtra(ID)) {
//            isEditMode = true;
//            //int id, String title, Author author, String publisher, Category category
//            idBook = getIntent().getIntExtra(ID, 0);
//            title = getIntent().getStringExtra(TITLE);
//            author = getIntent().getStringExtra(AUTHOR);
//            description = getIntent().getStringExtra(DESCRIPTION);
//            copies = getIntent().getIntExtra(COPIES, 0);
//            lentTo = getIntent().getStringExtra(LENT_TO);
//            category = getIntent().getStringExtra(CATEGORY);
//            publishedDate = getIntent().getStringExtra(PUBLISHED_DATE);
//            publisher = getIntent().getStringExtra(PUBLISHER);
//            pages = getIntent().getIntExtra(PAGE_COUNT, 0);
//            isDigital = getIntent().getBooleanExtra(IS_DIGITAL, false);
//            isLent = getIntent().getBooleanExtra(IS_LENT, false);
//            isRead = getIntent().getBooleanExtra(IS_READ, false);
//            imagePath = getIntent().getStringExtra(IMAGE);
//            bookToEdit = new Book(idBook, title, new Author(author), description, copies, lentTo,
//                    new Category(category), publishedDate, publisher, pages, "", isDigital,
//                    isLent, isRead, imagePath);
//            fillFieldsForEditMode();
//        }


        setupAddBookButton();
        setupUploadButton();
        initDatePicker();
        initDateButton();
        lentCheckListener();
    }

    private void fillFieldsForEditMode() {
        titleEdt.setText(bookToEdit.getTitle());
        authorEdt.setText(bookToEdit.getAuthor().getName());
        publisherEdt.setText(bookToEdit.getPublisher());
        descriptionEdt.setText(bookToEdit.getDescription());
        copiesEdt.setText(String.valueOf(bookToEdit.getCopies()));
        numberOfPagesEdt.setText(String.valueOf(bookToEdit.getPages()));
        digitalChk.setChecked(bookToEdit.isDigital());
        lentChk.setChecked(bookToEdit.isLent());
        readChk.setChecked(bookToEdit.isRead());

        lentToEdt.setText(bookToEdit.getLentTo());
        if (lentChk.isChecked()) {
            lentToEdt.setVisibility(View.VISIBLE);
        }

        // Set selected category in the spinner
        if (null != bookToEdit.getCategory()) {
            String selectedCategory = bookToEdit.getCategory().getName();
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) categorySpn.getAdapter();
            int position = adapter.getPosition(selectedCategory);
            categorySpn.setSelection(position);
        }

        String imagePath = bookToEdit.getImagePath();
        if (null != imagePath) {
//            try {
//                Thread.sleep(4000); // it's needed to save the image first, then show it in view
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
            bookImage.setImageBitmap(imageBitmap);
            bookImage.setImageURI(Uri.parse(imagePath));
        }
    }

    private void initializeViews() {
        titleEdt = findViewById(R.id.idEdtTitle);
        authorEdt = findViewById(R.id.idEdtAuthor);
        publisherEdt = findViewById(R.id.idEdtPublisher);
        categorySpn = findViewById(R.id.idSpnCategory);
        descriptionEdt = findViewById(R.id.idEdtDescription);
        copiesEdt = findViewById(R.id.idEdtCopies);
        lentToEdt = findViewById(R.id.idEdtLentTo);
        numberOfPagesEdt = findViewById(R.id.idEdtNumberOfPages);
        digitalChk = findViewById(R.id.idChkDigital);
        lentChk = findViewById(R.id.idChkLent);
        readChk = findViewById(R.id.idChkRead);
        addBookBtn = findViewById(R.id.idBtnAddBook);
        dateButton = findViewById(R.id.datePickerButton);
        bookImage = findViewById(R.id.bookImage);
        desingBookImage();
        uploadImageButton = findViewById(R.id.uploadImageButton);

    }
    private void desingBookImage() {
        GradientDrawable border = new GradientDrawable();
        border.setShape(GradientDrawable.RECTANGLE);
        border.setColor(Color.TRANSPARENT); // Background color
        border.setStroke(4, Color.BLACK); // Border width and color
        border.setCornerRadius(16); // Optional: Rounded corners
        bookImage.setBackground(border);
    }

    private void lentCheckListener() {
        lentChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lentToEdt.setVisibility(View.VISIBLE);
                } else {
                    lentToEdt.setText("");
                    lentToEdt.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initializeDatabase() {
        db = new Database(AddBookActivity.this);
    }

    private void initializeCategorySpinner() {
        ArrayList<Category> categories = db.allCategories();
        ArrayList<String> dropDownCategories = new ArrayList<>();
        dropDownCategories.add(SELECT_CATEGORY);
        for (Category cat : categories) {
            dropDownCategories.add(cat.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dropDownCategories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpn.setAdapter(arrayAdapter);
    }

    private void setupAddBookButton() {
        if (isEditMode) {
            addBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bookToEdit.getId() == 0) {
                        addBook();
                        return;
                    }
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
        int copies = getIntFromEdtTxt(copiesEdt);
        String lentTo = lentToEdt.getText().toString();
        int numberOfPages = getIntFromEdtTxt(numberOfPagesEdt);
        boolean digital = digitalChk.isChecked();
        boolean lent = lentChk.isChecked();
        boolean read = readChk.isChecked();
        Drawable drawable = bookImage.getDrawable();

        String imagePath = "";
        if (null != bookToEdit) {
            imagePath = bookToEdit.getImagePath();
        }
        if (null == imagePath || "".equals(imagePath)) {
            imagePath = addImageToFolder(drawable, title);
        }

        if (isInputInvalid(title, category, lent, lentTo)) {
            return;
        }

        if (db.getDuplicate(title, author, publisher, numberOfPages) != null) {
            Toast.makeText(AddBookActivity.this, "You already have this book!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.addNewBook(title, author, publisher, category, description, copies, lentTo, publishedDate, numberOfPages,
                    digital, lent, read, imagePath);
        Toast.makeText(AddBookActivity.this, "The book has been added.", Toast.LENGTH_SHORT).show();
        resetFields();

        goToAdministrationActivity();
    }

    private void updateBook() {
        String title = titleEdt.getText().toString();
        String author = authorEdt.getText().toString();
        String publisher = publisherEdt.getText().toString();
        String category = categorySpn.getSelectedItem().toString();
        String description = descriptionEdt.getText().toString();
        String publishedDate = dateButton.getText().toString();
        String copies = copiesEdt.getText().toString();
        String lentTo = lentToEdt.getText().toString();
        int numberOfPages = getIntFromEdtTxt(numberOfPagesEdt);
        boolean digital = digitalChk.isChecked();
        boolean lent = lentChk.isChecked();
        boolean read = readChk.isChecked();
        Drawable drawable = bookImage.getDrawable();
        String imagePath = addImageToFolder(drawable, title);

        if (isInputInvalid(title, category, lent, lentTo)) {
            return;
        }

        db.updateBook(bookToEdit.getId(), title, author,category, description, copies, lentTo,  publisher, publishedDate,
                    numberOfPages, digital, lent, read, imagePath);
        Toast.makeText(AddBookActivity.this, "The book has been updated.", Toast.LENGTH_SHORT).show();
        resetFields();

        goToAdministrationActivity();
    }

    private void setupUploadButton() {
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                bookImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getIntFromEdtTxt(EditText editText) {
        int number;
        try {
            number = Integer.parseInt(editText.getText().toString());
        } catch (NumberFormatException e) {
            number = 1;
        }
        return number;
    }

    private boolean isInputInvalid(String title, String category, boolean lent, String lentTo) {
        if (title.isEmpty()) {
            Toast.makeText(AddBookActivity.this, "Please enter the title!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (category.isEmpty() || category.equals(SELECT_CATEGORY)) {
            Toast.makeText(AddBookActivity.this, "Please enter category!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (lent && lentTo.equals("")) {
            Toast.makeText(AddBookActivity.this, "The book it's lent, please provide the name " +
                    "of the person who borrowed it ", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void resetFields() {
        titleEdt.setText("");
        authorEdt.setText("");
        publisherEdt.setText("");
        categorySpn.setSelection(0);
        descriptionEdt.setText("");
        copiesEdt.setText("");
        lentToEdt.setText("");
        numberOfPagesEdt.setText("");
        digitalChk.setChecked(false);
        lentChk.setChecked(false);
        readChk.setChecked(false);
    }

    private void goToAdministrationActivity() {
        Intent intent = new Intent(this, AdministrationActivity.class);
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

    public String addImageToFolder(Drawable drawable, String bookTitle) {
        if (drawable == null) {
            return "";
        }
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
        }
        String directoryPath = getFilesDir() + "/books_Images/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String imageName = bookTitle + ".png";
        File imageFile = new File(directory, imageName);
        String imagePath = directoryPath + imageName;
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }
}
