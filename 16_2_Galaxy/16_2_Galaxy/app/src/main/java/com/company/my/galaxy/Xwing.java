package com.company.my.galaxy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Xwing {
    // 화면 크기
    private int scrW, scrH;

    // 한계 속도, 현재 속도
    private final float MAX_SPEED = 1200;
    private float speed;

    // 이동 방향(-1, 0, 1), 목적지, 출발?
    private int dir;
    private float tx;
    private boolean start;

    // 현재 위치, 크기
    public float x, y;
    public int w, h;

    // 비트맵
    public Bitmap img;

    //--------------------------
    // 생성자
    //--------------------------
    public Xwing(Context context, int width, int height) {
        scrW = width;      // 화면 크기
        scrH = height;

        // 이미지 읽기
        img = BitmapFactory.decodeResource(context.getResources(), R.drawable.xwing);
        w = img.getWidth() / 2;
        h = img.getHeight() / 2;

        // 초기 위치 - 화면 아래 가운데
        x = scrW / 2;
        y = scrH - h - 40;
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        // 출발은 가속, 정지는 감속
        if (start) {
            speed = MathF.lerp(speed, MAX_SPEED, 5 * Time.deltaTime);
        } else {
            speed = MathF.lerp(speed, 0, 10 * Time.deltaTime);
        }

        // 목적지 50 픽셀 이내이면 정지
        if (MathF.distSqr(x, y, tx, y) < 2500) {
            start = false;
        }

        x += dir * speed * Time.deltaTime;

        // 화면 가장자리면 정지
        if (x < w || x > scrW - w) {
            x -= dir * speed * Time.deltaTime;
            speed = dir = 0;
            start = false;
        }
    }

    //--------------------------
    // Set Action <-- Touch Event
    //--------------------------
    public void setAction(float tx) {
        this.tx = tx;
        dir = (x < tx) ? 1 : -1;
        start = true;
    }

} // Xwing
