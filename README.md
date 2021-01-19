[![](https://jitpack.io/v/SWRevo/ImagePicker.svg)](https://jitpack.io/#SWRevo/ImagePicker)

# ImagePicker
Android custom photo album, completely imitating the WeChat UI, realizes the functions of taking pictures, picture selection (single selection/multiple selection), cropping, rotation, etc.

### This library is an extension of jeasonlzy/ImagePicker
Repair outdated code, migrate to AndroidX and increase dependency to new version

This project references：

* [https://github.com/jeasonlzy/ImagePicker](https://github.com/jeasonlzy/ImagePicker)
* [https://github.com/pengjianbo/GalleryFinal](https://github.com/pengjianbo/GalleryFinal) 
* [https://github.com/easonline/AndroidImagePicker](https://github.com/easonline/AndroidImagePicker)
 
## Demo
 ![image](https://github.com/jeasonlzy/Screenshots/blob/master/ImagePicker/demo1.png)![image](https://github.com/jeasonlzy/Screenshots/blob/master/ImagePicker/demo2.gif)
 ![image](https://github.com/jeasonlzy/Screenshots/blob/master/ImagePicker/demo3.gif)![image](https://github.com/jeasonlzy/Screenshots/blob/master/ImagePicker/demo5.gif)

## 1.Usage

Before use, for Android Studio users, you can choose to add:

```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```


```java
	implementation 'com.github.SWRevo:ImagePicker:1.1.0'
```

## 2.Function and parameter meaning

### Reminder: The preview interface in the library currently has a check box for the original image. For the time being, only the UI is made, and the compression logic has not been made yet

|Configuration parameters|Parameter meaning|
|:--:|--|
|multiMode|Picture selection mode, single/multiple selection|
|selectLimit|Multi-select limit number, default is 9|
|showCamera|Choose whether to show the camera button when taking photos|
|crop|Whether to allow cropping|
|style|When there is cropping, is the crop box rectangular or circular|
|focusWidth|Rectangular cropping frame width (round automatically take the minimum width and height)|
|focusHeight|Rectangular crop box height (round automatically take the minimum width and height)|
|outPutX|The width of the image to be saved after cropping|
|outPutY|The height of the picture to be saved after cropping|
|isSaveRectangle|Whether the cropped picture is saved in a rectangular area or in the shape of a crop box, for example, when circular cropping, this parameter is set to true, then the saved picture is a rectangular area, if this parameter is given to fale, the saved picture is a circular area|
|imageLoader|The image loader that needs to be used, you need to implement the ImageLoader interface|

## 3.Code reference

For more use, please download the demo to see the source code

1. First you need to inherit `id.indosw.imagepicker.loader.ImageLoader` This interface implements the methods in it, for example, the following code is used `Picasso` Implemented by the three-party loading library
```java
public class PicassoImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Picasso.get()//
                .load(Uri.fromFile(new File(path)))//
                .placeholder(R.mipmap.default_image)//
                .error(R.mipmap.default_image)//
                .resize(width, height)//
                .centerInside()//
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
        //Here is the method to clear the cache, implement it yourself as needed
    }
}
```

2. Then configure the image selector. Generally, you can initialize the configuration once in Application. Here you need to set the image loader above, and the rest of the configuration can be set as needed
```java
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_picker);
    
    ImagePicker imagePicker = ImagePicker.getInstance();
    imagePicker.setImageLoader(new PicassoImageLoader());   //Set up the image loader
    imagePicker.setShowCamera(true);  //Show camera button
    imagePicker.setCrop(true);        //Allow cropping (only valid for single selection)
    imagePicker.setSaveRectangle(true); //Whether to save in rectangular area
    imagePicker.setSelectLimit(9);    //Check the quantity limit
    imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //The shape of the crop box
    imagePicker.setFocusWidth(800);   //The width of the crop box. Unit pixel (the circle automatically takes the minimum width and height)
    imagePicker.setFocusHeight(800);  //The height of the crop box. Unit pixel (the circle automatically takes the minimum width and height)
    imagePicker.setOutPutX(1000);//The width of the saved file. Unit pixel
    imagePicker.setOutPutY(1000);//The height of the saved file. Unit pixel
}
```

3. After the above configuration is completed, open the album in the appropriate method, such as when clicking the button
```java
public void onClick(View v) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);  
    }
}
```

4. If you want to call the camera directly
```java
Intent intent = new Intent(this, ImageGridActivity.class);
intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // Whether to open the camera directly
      startActivityForResult(intent, REQUEST_CODE_SELECT);
```

5. Override the `onActivityResult` method to call back the result
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
        if (data != null && requestCode == IMAGE_PICKER) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            MyAdapter adapter = new MyAdapter(images);
            gridView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
        }
    }
}
```

## Licenses
```
 Copyright 2016 jeasonlzy(廖子尧)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```

