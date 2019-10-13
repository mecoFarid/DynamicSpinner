## Description
DynamicSpinner is a library content of each DynamicSpinner changes depending on the previous selection. You can find the demo application `apk` at the [releases](https://github.com/mecoFarid/DynamicSpinner/releases) tab.

![](https://raw.githubusercontent.com/mecoFarid/DynamicSpinner/master/extra_media/library_logo.jpg)

## Reason for development
Probably, you're familiar with this nested list where each item has their own subcategory and we have to filter what subcategory is shown depending on what is selected in previous (parent) selection
![](https://raw.githubusercontent.com/mecoFarid/DynamicSpinner/master/extra_media/nested_item_list.png)

## Demo
Demo application `apk` at the [releases](https://github.com/mecoFarid/DynamicSpinner/releases) tab.


<img src="https://raw.githubusercontent.com/mecoFarid/DynamicSpinner/master/extra_media/dynamic_spinner.gif" width="360">

## Usage
### 1. Integration
Min API level is: `API 21` [![](https://jitpack.io/v/mecoFarid/DynamicSpinner.svg)](https://jitpack.io/#mecoFarid/DynamicSpinner)

**Step 1.** Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
**Step 2.** Add the dependency
```
dependencies {
    ...
    implementation 'com.github.mecoFarid:DynamicSpinner:1.0.0'
}
```
**Step 3.** Create `xml` file to hold your single spinner (let's call it `item_spinner.xml`)

```
<?xml version="1.0" encoding="utf-8"?>
<com.mecofarid.dynamicspinner.view.SearchableView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/searchable_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:ds_icon_closeSearchView="@drawable/default_close_searchview_icon"
    app:ds_icon_openSearchView="@drawable/default_open_searchview_icon"
    app:ds_iconColor_openSearchView="@color/default_color_open_searchview_icon"
    app:ds_iconColor_closeSearcView="@color/default_color_open_searchview_icon"
    app:ds_isSearchable="true"
    app:ds_textSelectionMode="end"/>
```
**Step 4.** Add your `DynamicSpinner` view to `xml` file where you want it
```
<com.mecofarid.dynamicspinner.view.DynamicSpinner
        android:id="@+id/dynamic_spinner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```
**Step 5.** Add your nested list
```
class MainActivity : AppCompatActivity(), DynamicSpinnerAdapter.SpinnerItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val planetStructureJson = ""   // String nested list
        val planetStructureList = ...  // You can use Google's Gson library to convert JSON to List

        // Initialize adapter
        list?.let {
            dynamic_spinner.adapter = DynamicSpinnerAdapter(it, this, R.layout.item_spinner)
        }
    }

    // You can get selected item in this callback
    override fun onItemSelected(itemSpinner: ItemSpinner) {

    }
}
```

## Attributes

| Atribute name                     | Default          | Available              |  Description        |
|     :---                          |      :---        | :---                   |  :---               |
| app:ds_icon_closeSearchView       | <img src="https://raw.githubusercontent.com/mecoFarid/DynamicSpinner/master/extra_media/default_close.png" width="36">  | any drawable     |   - |
| app:ds_icon_openSearchView        | <img src="https://raw.githubusercontent.com/mecoFarid/DynamicSpinner/master/extra_media/default_open.png" width="36">   | any drawable     |   - |
| app:ds_iconColor_openSearchView   | #808080          | any color              |   -                                                                                  |
| app:ds_iconColor_closeSearcView   | #808080          | any color              |   -                                                                                  |
| app:ds_isSearchable               | true             | true \| false          | if `true` content is searchable                                                      |
| app:ds_textSelectionMode          | end              | start \| all \| end    | position of cursor when SearchableView opened, `all` means all text will be selected |