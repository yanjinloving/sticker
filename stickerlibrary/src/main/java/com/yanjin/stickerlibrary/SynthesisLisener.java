package com.yanjin.stickerlibrary;

import android.graphics.Bitmap;

public interface SynthesisLisener {
    void success(Bitmap bitmap);
    void fail();
}
