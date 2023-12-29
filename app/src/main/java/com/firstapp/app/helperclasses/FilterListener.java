package com.firstapp.app.helperclasses;

public interface FilterListener {
    void onFiltersApplied(boolean authorChecked, boolean categoryChecked, boolean readChecked, boolean lentChecked,
                          boolean readRdBtnChecked, boolean lentRdBtnChecked, String authorSelected, String categorySelected);
}
