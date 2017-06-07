package com.company.my.ancientboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CommonResources {
    // 소년
    static public Bitmap[][] arBoy = new Bitmap[3][5];
    static public int bw, bh;

    // 그림자
    static public Bitmap shadow;
    static public int sw, sh;


    //--------------------------
    // Set Resource  <-- GameView
    //--------------------------
    static public void set(Context context) {
        int animCnt = 5;

        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy);
        int tw = tmp.getWidth() / animCnt;
        int th = tmp.getHeight() / 3;

        // 이미지 분리
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < animCnt; j++) {
                arBoy[i][j] = Bitmap.createBitmap(tmp, tw * j, th * i, tw, th);
            }
        }

        // 이미지 크기
        bw = tw / 2;
        bh = th / 2;

        // 그림자
        sw = bw / 2;
        sh = bh / 4;

        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.shadow);
        shadow = Bitmap.createScaledBitmap(tmp, sw * 2, sh * 2, true);
    }

} // CommonResources
