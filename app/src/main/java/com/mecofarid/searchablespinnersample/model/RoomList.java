
package com.mecofarid.searchablespinnersample.model;


import androidx.annotation.NonNull;

import com.mecofarid.searchablemultispinner.model.ItemSpinner;

public class RoomList  extends ItemSpinner {

    public String name;
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
