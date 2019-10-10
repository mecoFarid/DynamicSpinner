package com.mecofarid.searchablespinnersample.model;

import androidx.annotation.NonNull;

import com.mecofarid.searchablemultispinner.model.ItemSpinner;

public class ByteList extends ItemSpinner {
    public String name;
    public Integer code;
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
