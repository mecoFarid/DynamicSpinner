package com.mecofarid.dynamicspinnersample.model;

import androidx.annotation.NonNull;

import com.mecofarid.dynamicspinner.model.ItemSpinner;

public class Byte extends ItemSpinner {
    public String name;
    public Integer code;
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
