
package com.mecofarid.searchablespinnersample.model;


import androidx.annotation.NonNull;

import com.mecofarid.searchablemultispinner.annotation.SubCategory;
import com.mecofarid.searchablemultispinner.model.ItemSpinner;

import java.util.List;

public class RoomList  extends ItemSpinner {

    public String name;
    @SubCategory
    public List<ItemList> itemList = null;
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
