package com.company.my.spider;

import android.graphics.Bitmap;

public class Poison {
    // 속도, 현재 위치, 크기
    private  int speed = 1200;
    public float x, y;
    public int r;

    // 비트맵, 소멸?
    public Bitmap img;
    public boolean isDead;

    //--------------------------
    // 생성자
    //--------------------------
    public Poison(float tx, float ty) {
        x = tx;
        y = ty;

        // 비트맵 이미지
        img = CommonResources.imgPoison;
        r = CommonResources.pw;
    }

    //--------------------------
    // Move & Dead
    //--------------------------
    public void update() {
        y -= speed * Time.deltaTime;
        isDead = (y < -r);
    }

} // Poison
