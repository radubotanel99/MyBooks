package com.firstapp.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;
import com.firstapp.app.helperclasses.CsvExporter;
import com.firstapp.app.helperclasses.CsvImporter;

public class ImportExportActivity extends AbstractActivity {
    private static final int REQUEST_PERMISSION = 100;
    private static final int PICK_CSV_FILE = 101;

    private Button importBtn;
    private Button exportBtn;
    private Database db;
    private CsvImporter importer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export);

        db = Database.getInstance(this);
        importer = new CsvImporter(db, ImportExportActivity.this );

        exportBtn = (Button) findViewById(R.id.exportBtn);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CsvExporter exporter = new CsvExporter();
                exporter.exportToCsv(ImportExportActivity.this, "books", db.allBooks());
            }
        });


        importBtn = (Button) findViewById(R.id.importBtn);
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CsvImporter importer = new CsvImporter(db, ImportExportActivity.this );
                importer.checkPermissionAndOpenFilePicker();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        importer.onActivityResult(this, requestCode, resultCode, data);
    }
}