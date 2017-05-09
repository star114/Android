package com.company.my.butterfly;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

//--------------------------
// 꽃다발
//--------------------------
public class Flower {
    // 위치, 크기, 비트맵
    public float x, y;
    public int w, h;
    public Bitmap img;

    //--------------------------
    // 생성자
    //--------------------------
    public Flower(Context context, int width, int height) {
        //  이미지
        img = BitmapFactory.decodeResource(context.getResources(), R.drawable.flower);
        w = img.getWidth() / 2 ;
        h = img.getHeight() / 2;

        Random rnd = new Random();
        x = rnd.nextInt(width - w * 2) + w;
        y = rnd.nextInt(height - h * 2) + h;
    }

    //--------------------------
    // 이동
    //--------------------------
    public boolean move(float tx, float ty) {
        // 터치 위치가 꽃다발 내부인가?
        float dist = (tx - x) * (tx - x) + (ty - y) * (ty - y);

        // 꽃다발을 터치하면 그 위치로 이동
        if (dist < w * w) {
            x = tx;
            y = ty;
            return true;
        } else {
            return false;
        }
    }

} // flower
