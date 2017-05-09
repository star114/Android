package com.company.my.rocket;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

public class Rocket {
    // 화면 크기, 현재 시각
    private int scrW, scrH;

    // 이동 속도, 중력, 이동 방향
    private int speed = 1000;
    private float gravity = 400f;
    private PointF dir = new PointF();

    // 위치, 로켓 크기
    public float x, y;
    public int w, h;

    // 현재 각도, 로켓
    public float ang;
    public Bitmap rocket;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Rocket(Context context, int width, int height) {
        scrW = width;      // 화면 크기
        scrH = height;

        // 로켓 이미지
        rocket = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket);
        w = rocket.getWidth() / 2;
        h = rocket.getHeight() / 2;

        // 로켓 초기화
        reset();
    }

    //--------------------------
    // 로켓 초기화
    //--------------------------
    private void reset() {
        // 초기 위치 - 화면 왼쪽 아래
        x = w;
        y = scrH - h;

        // 회전각
        ang = 0;
    }

    //--------------------------
    // 로켓 발사
    //--------------------------
    public void launch(float px, float py) {
        // 발사각도
        double rad = -Math.atan2(py - y - h, px - x);

        // 발사 방향(속도)
        dir.x = (float) Math.cos(rad) * speed;
        dir.y = -(float) Math.sin(rad) * speed;
    }

    //--------------------------
    // Move
    //--------------------------
    public boolean update() {
        boolean canRun = true;

        // 중력
        dir.y += gravity * Time.deltaTime;

        // 이동
        x += dir.x * Time.deltaTime;
        y += dir.y * Time.deltaTime;

        // 로켓 회전
        double rad = -Math.atan2(dir.y, dir.x);
        ang = 90 - (float)Math.toDegrees(rad);

        // 화면을 벗어나면 로켓 초기화
        if (x > scrW + h * 2 || y > scrH + h * 2) {
            reset();
            canRun = false;
        }

        return canRun;
    }

} // Rocket
