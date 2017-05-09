package com.company.my.ninja;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Sky {
    // 화면 크기
    private int w, h;

    // 비트맵, 이미지 수
    private Bitmap[] arSky = new Bitmap[2];
    private int imgCnt = 2;

    // Offset, 이미지 번호
    private float offset;
    private int imgNum1, imgNum2;

    // 스크롤 방향, 속도
    private int dir;
    private int speed = 25;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Sky(Context context, int width, int height) {
        w = width;
        h = height;

        makeBitmap(context);
    }

    //-----------------------------
    // update <-- Thread
    //-----------------------------
    public void update() {
        scroll();
    }

    //-----------------------------
    // draw <-- OnDraw
    //-----------------------------
    public void draw(Canvas canvas) {
        canvas.drawBitmap(arSky[imgNum1], -offset, 0, null);
        canvas.drawBitmap(arSky[imgNum1], w - offset, 0, null);
    }

    //-----------------------------
    // scroll
    //-----------------------------
    private void scroll() {
        // 닌자의 이동 방향
        dir = (int) GameView.ninja.dir.x;

        // 정지이면 왼쪽으로 스크롤
        if (dir == 0) {
            dir = 1;
        }

        offset += dir * speed * Time.deltaTime;

        switch (dir) {
        case 1 : // 왼쪽으로 스크롤
            if (offset > w) {
                offset -= w;
                imgNum1 = MathF.repeat(imgNum1++, imgCnt);
            }

            imgNum2 = imgNum1 + 1;
            if (imgNum2 >= imgCnt) imgNum2 = 0;
            break;
        case -1 : // 오른쪽으로 스크롤
            if (offset < 0) {
                offset += w;
                imgNum1--;
                if (imgNum1 < 0) imgNum1 = imgCnt - 1;
            }

            imgNum2 = imgNum1 - 1;
            if (imgNum2 < 0) imgNum2 = imgCnt - 1;
        }
    }

    //-----------------------------
    // Make Bitmap
    //-----------------------------
    private void makeBitmap(Context context) {
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.sky);

        arSky[0] = Bitmap.createScaledBitmap(tmp, w, (int) (h * 0.4f), true);
        // arSky[1] = Bitmap.createScaledBitmap(tmp, w, (int) (h * 0.4f), true);
        arSky[1] = arSky[0];
    }

} // Sky
