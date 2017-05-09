package com.company.my.floatingbubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

//-----------------------------
// 움직이는 비눗방울
//-----------------------------
public class Bubble {
    // 화면의 크기
    private int scrW, scrH;

    // 속도와 이동 방향, Random
    private int speed;
    private PointF dir = new PointF();
    private Random rnd = new Random();

    // 현재 시각과 DeltaTime
    private long currentTime;
    private float deltaTime;

    // 비눗방울의 위치, 크기, 비트맵
    public float x, y;
    public int r;
    public Bitmap bubble;

    // 삭제 표시용
    public boolean isDead;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Bubble(Context context, int width, int height) {
        // 화면의 크기
        scrW = width;
        scrH = height;

        // 비눗방울의 크기를 랜덤하게 설정
        r = rnd.nextInt(71) + 50;  // 50~120

        // 비눗방울 만들기
        bubble = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble);
        bubble = Bitmap.createScaledBitmap(bubble, r * 2, r * 2, true);

        // 비눗방울 초기 설정
        initBubble();
    }

    //-----------------------------
    // 비눗방울 초기화
    //-----------------------------
    private void initBubble() {
        // 이동 속도
        speed = rnd.nextInt(51) + 150;         // 초속 150~200 픽셀

        // 이동 방향 : 0~360도
        double rad = Math.toRadians( rnd.nextInt(360) );

        dir.x = (float) Math.cos(rad) * speed;
        dir.y = (float) -Math.sin(rad) * speed;

        // 초기 위치 : 화면 전체
        x = rnd.nextInt(scrW - r * 4) + r * 2;
        y = rnd.nextInt(scrH - r * 4) + r * 2;

        currentTime = System.nanoTime();
    }

    //-----------------------------
    // 비눗방울 이동
    //-----------------------------
    public void update() {
        // 직전 프레임으로 부터의 경과 시간
        float deltaTime = (System.nanoTime() - currentTime) / 1000000000f;
        currentTime = System.nanoTime();

        x += dir.x * deltaTime;     // 이동
        y += dir.y * deltaTime;

        // 화면의 경계에서 반사
        if (x < r || x > scrW - r) {
            dir.x = -dir.x;
            x += dir.x * deltaTime;     // 원위치
        }

        if (y < r || y > scrH - r) {
            dir.y = -dir.y;
            y += dir.y * deltaTime;
        }
    }

    //-----------------------------
    // Hit Test
    //-----------------------------
//    public boolean hitTest(float px, float py) {
//        boolean hit = false;
//        float dist = (x - px) * (x - px) + (y - py) * (y - py);
//
//        return (dist < r * r);
//    }

    public boolean hitTest(float px, float py) {
        isDead = (x - px) * (x - px) + (y - py) * (y - py) < r * r;
        return isDead;
    }

} // Bubble

