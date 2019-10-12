
package com.mecofarid.dynamicspinnersample.model;


import androidx.annotation.NonNull;

import com.mecofarid.dynamicspinner.annotation.SubCategory;
import com.mecofarid.dynamicspinner.model.ItemSpinner;

import java.util.List;

public class Room extends ItemSpinner {

    public String name;
    @SubCategory
    public List<Item> itemList = null;
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
