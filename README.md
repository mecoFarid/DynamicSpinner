## Description
DynamicSpinner is a library content of each DynamicSpinner changes depending on the previous selection. You can find the demo application `apk` at the [releases](https://github.com/mecoFarid/DynamicSpinner/releases) tab.

![](https://raw.githubusercontent.com/mecoFarid/DynamicSpinner/master/extra_media/library_logo.jpg)

## Reason for development
Probably, you're familiar with this nested list where each item has their own subcategory and we have to filter what subcategory is shown depending on what is selected in previous (parent) selection.

<img src="https://raw.githubusercontent.com/mecoFarid/DynamicSpinner/master/extra_media/nested_item_list.png" width="600">

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
    implementation 'com.github.mecoFarid:DynamicSpinner:1.0.1'
}
```
### 2. Code Sample
**Step 1.** Create `xml` file to hold your single spinner (let's call it `item_spinner.xml`)

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
    app:ds_textSelectionMode="end"
    app:cardElevation="@dimen/cardview_default_elevation"
    app:cardCornerRadius="@dimen/cardview_default_radius"
    app:cardUseCompatPadding="true"/>
```
**Step 2.** Add your `DynamicSpinner` view to `xml` file where you want it
```
<com.mecofarid.dynamicspinner.view.DynamicSpinner
        android:id="@+id/dynamic_spinner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```
**Step 3.** Create model objects

Remember to extend  `ItemSpinner` and add `@SubCategory` annotation to subcategory

```
class Country: ItemSpinner {

    var name: String? = null
    var code: Integer = 0
    
    @SubCategory
    var cityList:List<City>? = emptyList()
}
```

**Step 4.** Add your nested list
```
class MainActivity : AppCompatActivity(), DynamicSpinnerAdapter.SpinnerItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         // Nested country list (JSON)
        val planetStructureJson = "{\n" +
                "            \"countryList\": [\n" +
                "            {\n" +
                "                \"code\": 6,\n" +
                "                \"name\": \"Estonia\",\n" +
                "                \"cityList\": [\n" +
                "                {\n" +
                "                    \"name\": \"Tallin\",\n" +
                "                    \"code\": 60\n" +
                "                }\n" +
                "                ]\n" +
                "            }\n" +
                "            ]\n" +
                "        }" 
                
        // You can use Google's Gson library to convert JSON to List 
        val planetStructureList = ...  

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
| app:ds_icon_closeSearchView       | <img src="https://raw.githubusercontent.com/mecoFarid/DynamicSpinner/master/extra_media/default_close.png" width="20">  | any drawable     |   - |
| app:ds_icon_openSearchView        | <img src="https://raw.githubusercontent.com/mecoFarid/DynamicSpinner/master/extra_media/default_open.png" width="20">   | any drawable     |   - |
| app:ds_iconColor_openSearchView   | #808080          | any color              |   -                                                                                  |
| app:ds_iconColor_closeSearcView   | #808080          | any color              |   -                                                                                  |
| app:ds_isSearchable               | true             | true \| false          | if `true` content is searchable                                                      |
| app:ds_textSelectionMode          | end              | start \| all \| end    | position of cursor when SearchableView opened, `all` means all text will be selected |
| app:cardCornerRadius              | 2dp              | any dimension          | radius on the edges              |
| app:cardElevation                 | 2dp              | any dimension          | layout elevation                 |
| app:cardUseCompatPadding          | true             | true \| false          | padding between spinner items    |