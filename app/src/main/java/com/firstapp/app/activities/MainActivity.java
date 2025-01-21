package com.firstapp.app.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toolbar;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;
import com.firstapp.app.helperclasses.GeneralConstants;

import java.io.File;
import java.lang.reflect.Field;

public class MainActivity extends AbstractActivity {

    private Button goToMyBooksBtn;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

//        databaseConfigurations(); //BE VERY CAREFUL!!!!!

        goToMyBooksBtn = (Button) findViewById(R.id.goToMyBooksBtn);
        goToMyBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdministrationActivity();
            }
        });
        MainActivity.context = getApplicationContext();
    }

    public void openAdministrationActivity() {
        Intent intent = new Intent(this, AdministrationActivity.class);
        startActivity(intent);
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }


    private void databaseConfigurations() {
        Database db = Database.getInstance(MainActivity.this);
        db.resetDatabase();
        deleteImages();
//        db.alterDatabase();
    }
    private void deleteImages() {
        String folderPath = getFilesDir() + "/books_Images/";
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file: files) {
                    file.delete();
                }
            }
        }
    }
}