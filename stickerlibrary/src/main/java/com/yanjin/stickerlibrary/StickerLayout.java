package com.yanjin.stickerlibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class StickerLayout extends RelativeLayout {

    private RelativeLayout mRlStickerGroup;
    private Context context;
    private ImageView imageView;


    public StickerLayout(Context context) {
        this(context,null);
    }

    public StickerLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StickerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        inflate(context,R.layout.sticker_layout,this);
        mRlStickerGroup = findViewById(R.id.rl_sticker_group);
        mRlStickerGroup.removeAllViews();
        imageView = findViewById(R.id.iv);
    }

    public <T> void  setBackgroundResource(T t){
        if(imageView!=null){
            if(t instanceof Integer){
                imageView.setImageResource((Integer)t);
            }else if(t instanceof Drawable){
                imageView.setImageDrawable((Drawable)t);
            }else if(t instanceof Bitmap){
                imageView.setImageBitmap((Bitmap)t);
            }else if(t instanceof Bitmap){
                imageView.setImageURI((Uri) t);
            }

        }
    }

    public void addStickerView(StickerView stickerView){
        mRlStickerGroup.addView(stickerView);
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) stickerView.getLayoutParams();
        layoutParams.width = StickerTool.dip2px(context,100f);
        layoutParams.height = StickerTool.dip2px(context,100f);
        stickerView.setLayoutParams(layoutParams);
    }

    public void getSynthesisView(final SynthesisLisener synthesisLisener) {
        int childCount = mRlStickerGroup.getChildCount();
        for(int x= 0 ;x<childCount;x++){
            View child = mRlStickerGroup.getChildAt(x);
            if(child instanceof StickerView){
                StickerView stickerView = (StickerView) child;
                stickerView.hindOptionLayout();
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = StickerTool.getViewBitmap(StickerLayout.this);
                        if(synthesisLisener != null){
                            if(bitmap!=null){
                                synthesisLisener.success(bitmap);
                                StickerLayout.this.setDrawingCacheEnabled(false);
                            }else{
                                synthesisLisener.fail();
                            }

                        }
                    }
                });
            }
        }).start();
    }
}
