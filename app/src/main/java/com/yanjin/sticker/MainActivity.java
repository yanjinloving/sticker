package com.yanjin.sticker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yanjin.stickerlibrary.StickerLayout;
import com.yanjin.stickerlibrary.StickerTool;
import com.yanjin.stickerlibrary.StickerView;
import com.yanjin.stickerlibrary.SynthesisLisener;

public class MainActivity extends AppCompatActivity {

    private StickerLayout stickerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stickerLayout = findViewById(R.id.sticker_layout);
        stickerLayout.setBackgroundResource(R.mipmap.test_bg);
        findViewById(R.id.btn_hetu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerLayout.getSynthesisView(new SynthesisLisener() {
                    @Override
                    public void success(Bitmap bitmap) {
                        StickerTool.saveImageToGallery(MainActivity.this,bitmap);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StickerView stickerView = new StickerView(MainActivity.this);
                stickerView.setImageResource(getResources().getDrawable(R.mipmap.test));
                stickerLayout.addStickerView(stickerView);
            }
        });
    }
}
