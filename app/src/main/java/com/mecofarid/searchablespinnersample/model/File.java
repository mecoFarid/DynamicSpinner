package com.mecofarid.searchablespinnersample.model;

import androidx.annotation.NonNull;

import com.mecofarid.searchablemultispinner.annotation.SubCategory;
import com.mecofarid.searchablemultispinner.model.ItemSpinner;

import java.util.List;

public class File extends ItemSpinner {
    public String name;
    public Integer code;
    @SubCategory
    public List<Content> contentList = null;
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
