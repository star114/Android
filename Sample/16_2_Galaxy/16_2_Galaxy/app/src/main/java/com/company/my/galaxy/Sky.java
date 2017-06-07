package com.company.my.galaxy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Sky {
    // 화면의 폭과 높이
    private int w, h;

    // 이미지 수, 이미지 배열, 출력할 이미지 번호
    private int imgCnt = 3;
    private Bitmap[] arSky = new Bitmap[imgCnt];
    private int imgNum1, imgNum2;

    // 스크롤 속도, 이미지 오프셋
    private float speed = 200f;
    private float ofs = 0;

    public Bitmap imgSky;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Sky(Context context, int width, int height) {
        w = width;
        h = height;

        createBitmap(context);
    }

    //-----------------------------
    // update <-- Thread
    //-----------------------------
    public void update() {
        ofs += speed * Time.deltaTime;
        if (ofs > h) {
            ofs -= h;
            // 아래쪽 이미지
            imgNum1 = MathF.repeat(imgNum1, imgCnt);
        }

        // 위쪽 이미지
        imgNum2 = imgNum1 + 1;
        if (imgNum2 >= imgCnt) imgNum2 = 0;

        // imgNum2 = MathF.repeat(imgNum1, imgCnt);
    }

    //-----------------------------
    // draw <-- onDraw
    //-----------------------------
    public void draw(Canvas canvas) {
        canvas.drawBitmap(arSky[imgNum1], 0, ofs, null);
        canvas.drawBitmap(arSky[imgNum2], 0, ofs - h, null);
    }

    //-----------------------------
    // 비트맵 만들기
    //-----------------------------
    private void createBitmap(Context context) {
        // 이미지를 배열에 저장
        for (int i = 0; i < imgCnt; i++) {
            Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.sky0 + i);
            arSky[i] = Bitmap.createScaledBitmap(tmp, w, h, true);
        }
    }

} // Sky
