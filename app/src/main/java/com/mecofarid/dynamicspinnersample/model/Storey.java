
package com.mecofarid.dynamicspinnersample.model;

import androidx.annotation.NonNull;

import com.mecofarid.dynamicspinner.annotation.SubCategory;
import com.mecofarid.dynamicspinner.model.ItemSpinner;

import java.util.List;

public class Storey extends ItemSpinner {

    public String name;
    public Integer code;
    @SubCategory
    public List<Apartment> apartmentList = null;

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
