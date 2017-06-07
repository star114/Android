package com.company.my.floatingbubble;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import java.util.Random;

//-----------------------------
// 작은 비눗방울
//-----------------------------
public class SmallBubble {
    // 화면 크기
    private int scrW, scrH;

    // 이동 방향, 속도, 수명
    private PointF dir = new PointF();
    private int speed;
    private float life;

    // 현재 위치, 반지름
    public float x, y;
    public int r;

    // 투명도, 소멸, 이미지
    public int alpha = 255;
    public boolean isDead;
    public Bitmap bubble;

    //-----------------------------
    // 생성자
    //-----------------------------
    public SmallBubble(Context context, int sw, int sh, float px, float py) {
        scrW = sw;     // 화면 크기
        scrH = sh;
        x = px;         // 초기 위치
        y = py;

        Random rnd = new Random();

        // 속도, 수명
        speed = rnd.nextInt(201) + 300;    // 300~500
        // speed = 300;
        life = (rnd.nextInt(6) + 10) / 10f;   // 1~1.5초

        // 이동 방향
        double rad = Math.toRadians( rnd.nextInt(360) );

        dir.x = (float) Math.cos(rad) * speed;
        dir.y = (float) -Math.sin(rad) * speed;

        // 반지름, 이미지 번호
        r = rnd.nextInt(11) + 10;     // 10~20
        int n = rnd.nextInt(6);       // 이미지 번호

        bubble = BitmapFactory.decodeResource(context.getResources(), R.drawable.b0 + n);
        bubble = Bitmap.createScaledBitmap(bubble, r * 2, r * 2, true);
    }

    //-----------------------------
    // 이동
    //-----------------------------
    public void update() {
        x += dir.x * Time.deltaTime;     // 이동
        y += dir.y * Time.deltaTime;

        life -= Time.deltaTime;
        if (life < 0) {
            alpha -= 5;
            if (alpha < 0) alpha = 0;
        }

        // 화면을 벗어났는가?
        if (alpha == 0 || x < -r || x > scrW + r || y < -r || y > scrH + r) {
            isDead = true;
        }
    }

} // SmallBubble
