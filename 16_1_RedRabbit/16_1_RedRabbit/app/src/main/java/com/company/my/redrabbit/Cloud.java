package com.company.my.redrabbit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Cloud {
    private int scrW, scrH;

    // 이동 속도
    private int speed1 = 50;
    private int speed2 = 75;

    // 구름의 위치, 크기
    public float x1, y1, x2, y2;
    public int w1, h1, w2, h2;
    public Bitmap img1, img2;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Cloud(Context context, int w, int h) {
        scrW = w;
        scrH = h;

        img1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.clould1);
        img2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.clould2);

        w1 = img1.getWidth() / 2;
        h1 = img1.getHeight() / 2;

        w2 = img2.getWidth() / 2;
        h2 = img2.getHeight() / 2;

        // 초기 위치
        x1 = scrW * 0.3f;
        y1 = scrH * 0.2f;

        x2 = scrW * 0.8f;
        y2 = scrH * 0.3f;
    }

    //-----------------------------
    // 이동
    // -----------------------------
    public void update() {
        // 왼쪽으로 이동
        x1 -= speed1 * Time.deltaTime;
        x2 -= speed2 * Time.deltaTime;

        // 화면을 벗어나면 오른쪽에서 다시 등장
        if (x1 < -w1) {
            x1 = scrW + w1;
        }

        if (x2 < -w2) {
            x2 = scrW + w2;
        }
    }

} // Clould
