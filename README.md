# sticker
添加贴图并把view合成bitmap的工具

#使用方式
布局中加入
```
<com.yanjin.stickerlibrary.StickerLayout
        android:id="@+id/sticker_layout"
        android:layout_width="281dp"
        android:layout_height="500dp"
        android:layout_centerInParent="true">

 </com.yanjin.stickerlibrary.StickerLayout>
 ```
 
 在activity中使用
 1、添加背景图片
 ```
 stickerLayout.setBackgroundResource(R.mipmap.test_bg);
 ```
 
 2、添加贴图
 ```
StickerView stickerView = new StickerView(MainActivity.this);
stickerView.setImageResource(getResources().getDrawable(R.mipmap.test));
stickerLayout.addStickerView(stickerView);
```

3、把view合成bitmap并保存
```
stickerLayout.getSynthesisView(new SynthesisLisener() {
                    @Override
                    public void success(Bitmap bitmap) {
                        StickerTool.saveImageToGallery(MainActivity.this,bitmap);
                    }

                    @Override
                    public void fail() {

                    }
});
```
