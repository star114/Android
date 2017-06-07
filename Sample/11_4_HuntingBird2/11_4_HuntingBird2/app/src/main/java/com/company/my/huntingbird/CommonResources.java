package com.company.my.huntingbird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CommonResources {
    static public Bitmap[] arBirds = new Bitmap[6];
    static public int bw;
    static public int bh;

    // Set Bitmap <-- GameView
    static public void set(Context context) {
        // 원본 이미지 읽기
        Bitmap org = BitmapFactory.decodeResource(context.getResources(),R.drawable.sparrow);

        // 개별 이미지의 크기
        bw = org.getWidth() / 6;
        bh = org.getHeight();

        // 이미지 분해
        for (int i = 0; i < 6;  i++) {
            arBirds[i] = Bitmap.createBitmap(org, bw * i, 0, bw, bh);
        }

        // 이미지의 폭과 높이(1/2)
        bw /= 2;
        bh /= 2;
    }

}
