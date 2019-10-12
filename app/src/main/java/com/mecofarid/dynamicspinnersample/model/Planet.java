
package com.mecofarid.dynamicspinnersample.model;

import androidx.annotation.NonNull;

import com.mecofarid.dynamicspinner.annotation.SubCategory;
import com.mecofarid.dynamicspinner.model.ItemSpinner;

import java.util.List;

public class Planet extends ItemSpinner {

    public String name;
    @SubCategory
    public List<Country> countryList = null;

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
