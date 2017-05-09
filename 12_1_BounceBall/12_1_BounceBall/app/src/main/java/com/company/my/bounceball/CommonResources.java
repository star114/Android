package com.company.my.bounceball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CommonResources {
    static public Bitmap ball;      // 공 비트맵
    static public int   r = 80;       // 공의 반지름

    //--------------------------
    // SetBitmap <-- GameView
    //--------------------------
    static public void set(Context context) {
        ball = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
        ball = Bitmap.createScaledBitmap(ball, r * 2, r * 2, true);
    }
}
