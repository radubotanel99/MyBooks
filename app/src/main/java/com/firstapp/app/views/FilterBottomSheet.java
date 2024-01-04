package com.firstapp.app.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firstapp.app.R;
import com.firstapp.app.activities.AddBookActivity;
import com.firstapp.app.database.Database;
import com.firstapp.app.helperclasses.FilterListener;
import com.firstapp.app.objects.Category;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class FilterBottomSheet extends BottomSheetDialogFragment {

    private FilterListener filterListener;
    private Context context;
    private Database db;
    private CheckBox checkAuthor, checkCategory, checkReadStatus, checkLentStatus;
    private Spinner authorSpn, categorySpn;
    private RadioGroup readStatusRdoGroup, lentStatusRdoGroup;
    private RadioButton readRdBtn, unreadRdBtn, lentRdBtn, unlentRdBtn;

    private boolean authorIsChecked, categoryIsChecked, readStatusIsChecked, lentStatusIsChecked,
            readRdBtnChecked, lentRdBtnChecked;
    private String authorSelected, categorySelected;

    public FilterBottomSheet(Context context, boolean authorIsChecked, boolean categoryIsChecked, boolean readStatusIsChecked, boolean lentStatusIsChecked, boolean readRdBtnChecked, boolean lentRdBtnChecked, String authorSelected, String categorySelected) {
        this.context = context;
        this.authorIsChecked = authorIsChecked;
        this.categoryIsChecked = categoryIsChecked;
        this.readStatusIsChecked = readStatusIsChecked;
        this.lentStatusIsChecked = lentStatusIsChecked;
        this.readRdBtnChecked = readRdBtnChecked;
        this.lentRdBtnChecked = lentRdBtnChecked;
        this.authorSelected = authorSelected;
        this.categorySelected = categorySelected;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_filter, container, false);

        initializeDatabase();
        initializeElements(view);
        addDataToSpinners();
        setMainElementsCheckedOrUnchecked();
        setSecondElementsCheckedOrUnchecked();
        initializeMainCheckListeners(view);
        applyButtonFunction(view);

        return view;
    }

    private void initializeElements(View view) {
        checkAuthor = view.findViewById(R.id.idChkAuthor);
        checkCategory = view.findViewById(R.id.idChkCategory);
        checkReadStatus = view.findViewById(R.id.idChkReadStatus);
        checkLentStatus = view.findViewById(R.id.idChkLentStatus);
        authorSpn = view.findViewById(R.id.idSpnAuthor);
        categorySpn = view.findViewById(R.id.idSpnCategory);
        readStatusRdoGroup = view.findViewById(R.id.radioReadStatus);
        lentStatusRdoGroup = view.findViewById(R.id.radioLentStatus);
        readRdBtn = view.findViewById(R.id.rdBtnRead);
        unreadRdBtn = view.findViewById(R.id.rdBtnUnread);
        lentRdBtn = view.findViewById(R.id.rdBtnLent);
        unlentRdBtn = view.findViewById(R.id.rdBtnUnlent);
    }

    private void addDataToSpinners() {
        addDataAuthorSpinner();
        addDataCategorySpinner();
    }

    private void setMainElementsCheckedOrUnchecked() {
        checkAuthor.setChecked(authorIsChecked);
        checkCategory.setChecked(categoryIsChecked);
        checkReadStatus.setChecked(readStatusIsChecked);
        checkLentStatus.setChecked(lentStatusIsChecked);
    }

    private void setSecondElementsCheckedOrUnchecked() {
        setAuthorSpinnerOnVisibleOrNot();
        setCategorySpinnerOnVisibleOrNot();
        setReadRadioGroupOnVisibleOrNot();
        setLentRadioGroupOnVisibleOrNot();
    }

    private void initializeMainCheckListeners(View view) {
        authorCheckListener();
        categoryCheckListener();
        readCheckListener();
        lentCheckListener();
    }

    private void readCheckListener() {
        checkReadStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    readStatusRdoGroup.setVisibility(View.VISIBLE);
                    readRdBtn.setChecked(true);
                } else {
                    readStatusRdoGroup.setVisibility(View.GONE);
                }
            }
        });
    }

    private void lentCheckListener() {
        checkLentStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lentStatusRdoGroup.setVisibility(View.VISIBLE);
                    lentRdBtn.setChecked(true);
                } else {
                    lentStatusRdoGroup.setVisibility(View.GONE);
                }
            }
        });
    }

    private void authorCheckListener() {
        checkAuthor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    authorSpn.setVisibility(View.VISIBLE);
                    authorSpn.setSelection(0);
                } else {
                    authorSpn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void categoryCheckListener() {
        checkCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    categorySpn.setVisibility(View.VISIBLE);
                    categorySpn.setSelection(0);
                } else {
                    categorySpn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setAuthorSpinnerOnVisibleOrNot() {
        if (authorIsChecked) {
            authorSpn.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) authorSpn.getAdapter();
            int position = adapter.getPosition(authorSelected);
            authorSpn.setSelection(position);
        } else {
            authorSpn.setVisibility(View.GONE);
        }
    }

    private void setCategorySpinnerOnVisibleOrNot() {
        if (categoryIsChecked) {
            categorySpn.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) categorySpn.getAdapter();
            int position = adapter.getPosition(categorySelected);
            categorySpn.setSelection(position);
        } else {
            categorySpn.setVisibility(View.GONE);
        }
    }

    private void setReadRadioGroupOnVisibleOrNot() {
        if (readStatusIsChecked) {
            readStatusRdoGroup.setVisibility(View.VISIBLE);
            if (readRdBtnChecked) {
                readRdBtn.setChecked(true);
            } else {
                unreadRdBtn.setChecked(true);
            }
        } else {
            readStatusRdoGroup.setVisibility(View.GONE);
        }
    }

    private void setLentRadioGroupOnVisibleOrNot() {
        if (lentStatusIsChecked) {
            lentStatusRdoGroup.setVisibility(View.VISIBLE);
            if (lentRdBtnChecked) {
                lentRdBtn.setChecked(true);
            } else {
                unlentRdBtn.setChecked(true);
            }
        } else {
            lentStatusRdoGroup.setVisibility(View.GONE);
        }
    }

    private void addDataAuthorSpinner() {
        ArrayList<String> authors = db.getAuthors();
        ArrayList<String> dropDownCategories = new ArrayList<>();
        for (String author : authors) {
            if (!author.equals("")) {
                dropDownCategories.add(author);
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, dropDownCategories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        authorSpn.setAdapter(arrayAdapter);
    }

    private void addDataCategorySpinner() {
        ArrayList<Category> categories = db.allCategories();
        ArrayList<String> dropDownCategories = new ArrayList<>();
        for (Category cat : categories) {
            dropDownCategories.add(cat.getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, dropDownCategories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpn.setAdapter(arrayAdapter);
    }

    private void applyButtonFunction(View view) {
        Button applyButton = view.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filterListener.onFiltersApplied(checkAuthor.isChecked(), checkCategory.isChecked(), //
                        checkReadStatus.isChecked(), checkLentStatus.isChecked(), checkReadStatus.isChecked() && readRdBtn.isChecked(),
                        checkLentStatus.isChecked() && lentRdBtn.isChecked(), (String) authorSpn.getSelectedItem(),
                        (String) categorySpn.getSelectedItem());
                if (checkAuthor.isChecked() || checkCategory.isChecked() || checkReadStatus.isChecked() || checkLentStatus.isChecked()) {
                    Toast.makeText(context, "Filters were applied successfully!", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

    private void initializeDatabase() {
        db = new Database(context);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            filterListener = (FilterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FilterListener");
        }
    }
}
