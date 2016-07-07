# imagePicker
an imagePicker for android ,with it,you can select single or multiple image,crop it for avatar,take a photo and use it,preview and zoom etc.

![screenshot](static/Screenshot1.png)-
![screenshot](static/Screenshot2.png)-
![screenshot](static/Screenshot3.png)-
![screenshot](static/Screenshot4.png)-
![screenshot](static/Screenshot5.png)

## Features
 * choosing single or multi image with preview
 * crop a image for avatar simply
 * custom new version easily.
 * easily use


## Usage
just 1 minutes using it,and make your own version as you need. 

### Gradle
``` groovy
dependencies {
    compile project(':imagepickerModule')
    compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.3'  //optional
    compile 'com.github.bumptech.glide:glide:3.6.1'   //optional
    compile 'com.squareup.picasso:picasso:2.4.0'   //optional
}
```

``` java
//single select
AndroidImagePicker.getInstance().pickSingle(MainActivity.this, isShowCamera, new AndroidImagePicker.OnImagePickCompleteListener() {
                    @Override
                    public void onImagePickComplete(List<ImageItem> items) {
                        if(items != null && items.size() > 0){
                            Log.i(TAG,"=====selected："+items.get(0).path);
                            mAdapter.clear();
                            mAdapter.addAll(items);
                        }
                    }
                });


//multi select
AndroidImagePicker.getInstance().pickMulti(MainActivity.this, isShowCamera, new AndroidImagePicker.OnImagePickCompleteListener() {
                    @Override
                    public void onImagePickComplete(List<ImageItem> items) {
                        if(items != null && items.size() > 0){
                            Log.i(TAG,"=====selected："+items.get(0).path);
                            mAdapter.clear();
                            mAdapter.addAll(items);
                        }
                    }
                });

//select and crop avatar
AndroidImagePicker.getInstance().pickAndCrop(MainActivity.this, true, 120, new AndroidImagePicker.OnImageCropCompleteListener() {
                    @Override
                    public void onImageCropComplete(Bitmap bmp, float ratio) {
                        Log.i(TAG,"=====onImageCropComplete (get bitmap="+bmp.toString());
                        ivCrop.setVisibility(View.VISIBLE);
                        ivCrop.setImageBitmap(bmp);
                    }
                });

```

### Eclipse
```
read my code and do it yourself.
```



## Licence

``` text
Copyright (C) 2016 1feng (easonline7@gmail.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.