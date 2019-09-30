package com.yanjin.stickerlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.support.v4.math.MathUtils.clamp;

public class StickerView extends RelativeLayout implements View.OnClickListener {
    private RelativeLayout mRlRoot;
    private int startX;
    private int startY;
    private int parentWidth;
    private int parentHeight;
    private boolean mDoubleFingers=false;
    private float scale = 1; // 伸缩比例
    private float spacing;
    private float degree;
    private float rotation; // 旋转角度
    private ImageView imageView;
    private ImageView ivDelete;
    private RelativeLayout rlOption;
    private int sfWidthivSF;
    private int sfHeightivSF;
    private boolean optionIsHind = true;
    private int currentL;
    private int currentT;


    public StickerView(Context context) {
        this(context,null);
    }

    public StickerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context,R.layout.sticker_view_layout,this);
        mRlRoot = findViewById(R.id.rl_root);
        imageView = findViewById(R.id.iv);
        ivDelete = findViewById(R.id.iv_delete);
        ivDelete.setOnClickListener(this);
        rlOption = findViewById(R.id.rl_option);
    }

    public void hindOptionLayout(){
        rlOption.setVisibility(INVISIBLE);
        optionIsHind = true;
    }

    public <T> void  setImageResource(T t){
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int pCount = event.getPointerCount();// 触摸设备时手指的数量
        int action = event.getAction();
        if(parentHeight==0 || parentWidth==0){
            ViewGroup parent = (ViewGroup) this.getParent();
            parentWidth = parent.getWidth();
            parentHeight = parent.getHeight();

        }
        if(sfWidthivSF==0 || sfHeightivSF==0){
            sfWidthivSF = ivDelete.getWidth();
            sfHeightivSF = ivDelete.getHeight();
        }
        if((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN && pCount == 1){
            mDoubleFingers= false;
            startX = (int) event.getX();
            startY = (int) event.getY();
            optionRL();
            Log.d("yanjin","optionIsHind----"+optionIsHind);
            if(optionIsHind == true){
                rlOption.setVisibility(VISIBLE);
                optionIsHind = false;
            }else{
                hindOptionLayout();
            }


        }else if((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE && pCount == 1){
            if(mDoubleFingers){
                return true;
            }else {
                int offsetX = x - startX;
                int offsetY = y - startY;
                currentL = getLeft() + offsetX;
                currentT = getTop() + offsetY;
                if(currentT <=0){
                    currentT = 0;
                }else if(currentT>=(parentHeight-getHeight())){
                    currentT = parentHeight-getHeight();
                }
                if(currentL <=0){
                    currentL = 0;
                }else if(currentL>=(parentWidth-getWidth())){
                    currentL = parentWidth-getWidth();
                }
                RelativeLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
                layoutParams.setMargins(currentL,currentT,0,0);
                setLayoutParams(layoutParams);
            }

        }else if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN && pCount == 2){
            mDoubleFingers = true;
            spacing = getSpacing(event);
            degree = getDegree(event);
            hindOptionLayout();
            optionRL();
        }else if((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE && pCount == 2){
            hindOptionLayout();
            scale = scale * getSpacing(event) / spacing;
            setScaleX(scale);
            setScaleY(scale);

            rotation = rotation + getDegree(event) - degree;
            if (rotation > 360) {
                rotation = rotation - 360;
            }
            if (rotation < -360) {
                rotation = rotation + 360;
            }
            setRotation(rotation);
        }


        return true;
    }


    private void optionRL() {
        int childCount = ((ViewGroup)getParent()).getChildCount();
        for(int x= 0;x<childCount;x++){
            View child = ((ViewGroup)getParent()).getChildAt(x);
            if(child instanceof StickerView){
                StickerView stickerView = (StickerView) child;
                stickerView.hindOptionLayout();
            }
        }
    }

    // 触碰两点间距离
    private float getSpacing(MotionEvent event) {
        //通过三角函数得到两点间的距离
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    // 取旋转角度
    private float getDegree(MotionEvent event) {
        //得到两个手指间的旋转角度
        double delta_x = event.getX(0) - event.getX(1);
        double delta_y = event.getY(0) - event.getY(1);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.iv_delete){
            if(optionIsHind == false){
                ViewGroup parent = (ViewGroup) getParent();
                parent.removeView(this);
            }
        }
    }

}
