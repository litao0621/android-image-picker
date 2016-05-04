# android-image-picker
android 图片多选控件

# Screenshot
![Image][1]

# Gradle
``` groovy
repositories { 
    maven { url "https://jitpack.io" }
}
dependencies {
    compile 'com.github.sd6352051:android-image-picker:v1.0.1'
}
```

# 如何使用
### Step 0:  Permission
``` xml
	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
### Step 1:  让你的activity 继承 `BaseGalleryActivity` 
``` java
public class YourActivity extends BaseGalleryActivity{

}
```
### Step 2:  添加控件的根布局
``` xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <FrameLayout
        android:id="@+id/gallery_root"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:paddingBottom="48dp"/>
</RelativeLayout>      
```
### Step 3:  在你的activity中调用此 `attachFragment(R.id.gallery_root)` 
``` java 
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachFragment(R.id.gallery_root);
    }
```

### Step 4: 实现接口
``` java
  @Override
    public Configuration getConfiguration() {
        //返回控件配置
        Configuration cfg=new Configuration.Builder()
                .hasCamera(true)
                .hasShade(true)
                .hasPreview(true)
                .setSpaceSize(3)
                .setPhotoMaxWidth(120)
                .setCheckBoxColor(0xFF3F51B5)
                .setDialogHeight(Configuration.DIALOG_HALF)
                .setDialogMode(Configuration.DIALOG_GRID)
                .setMaximum(9)
                .setTip(null)
                .setAblumsTitle(null)
                .build();
        return cfg;
    }

    @Override
    public List<PhotoEntry> getSelectPhotos() {
        //返回当前已经选中的图片 没有是返回null
        return mSelectedPhotos;
    }

    @Override
    public void onSelectedCountChanged(int count) {
        //这个方法将在你图选择发生变化时调用 
        // count:当前被选中图像数量
    }

    @Override
    public void onAlbumChanged(String name) {
        //这个方法将在相册选择发生变化时调用
        //name:当前选中的相册名称
    }

    @Override
    public void onTakePhoto(PhotoEntry entry) {
        //这个方法将在你拍照后调用
        //entry：返回拍照后的图片信息
    }

    @Override
    public void onChoosePhotos(List<PhotoEntry> entries) {
        //这个方法将在你调用 sendPhotos() 方法后调用
        //entries：返回你选中的图片信息
    }

    @Override
    public void onPhotoClick(PhotoEntry entry) {
        //这个方法将在你点击图像时调用，如果configuration hasPreview(false)时 将不回调此方法
        //entry: 返回当前点击的图像信息
    }

```
### Step 5:  其他方法

`openAlbum()`: 打开相册弹窗 

`sendPhotos()`: 这个方法将会被调用 `onChoosePhotos(List<PhotoEntry> entries)` 

[查看示例代码](https://github.com/sd6352051/android-image-picker/tree/master/app)

[下载示例应用](https://github.com/sd6352051/android-image-picker/blob/master/sample.apk)
# 配置项
默认配置
``` java
Configuration cfg=new Configuration.Builder()
                //是否包含相机按钮
                .hasCamera(true)
                //图片选中后是否包含一个浮层
                .hasShade(true)
                //是否包含图像预览功能，为false时点击图像将执行反选效果，true则会调用onPhotoClick方法
                .hasPreview(true)
                //GridView间隔
                .setSpaceSize(4)
                //图像最大宽度
                .setPhotoMaxWidth(120)
                //Checkbox 背景色
                .setCheckBoxColor(0xFF3F51B5)
                //相册弹出默认高度
                .setDialogHeight(Configuration.DIALOG_HALF)
                //相册模式  DIALOG_GRID  网格形式  DIALOG_LIST 列表形式
                .setDialogMode(Configuration.DIALOG_GRID)
                //图像最大选中
                .setMaximum(9)
                //图像达到最大选中时的提示
                .setTip(null)
                //相册标题
                .setAblumsTitle(null)
                .build();
```
  
# License
Copyright 2016 litao.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.














[1]: https://github.com/sd6352051/android-image-picker/blob/master/gif/gallery.gif
