package com.company.my.parkalone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import java.util.Random;

public class Ball {
    // 화면 크기
    private int scrW, scrH;

    // 속도, 중력
    private float speed;
    private final float GRAVITY = 800;

    // 이동 방향
    private PointF dir = new PointF();

    // 현재 위치, 반지름, 회전각
    public float x, y;
    public int r;
    public float ang;

    // 비트맵, 그림자
    public Bitmap img;
    public Bitmap shadow;

    // 그림자 크기, 축소 비율, 지면 높이
    public int sw, sh;
    public float shwScale;
    public float ground;

    //--------------------------
    // 생성자
    //--------------------------
    public Ball(Context context, int width, int height) {
        scrW = width;
        scrH = height;

        // 지면의 높이
        ground = scrH * 0.9f;

        // 비트맵, 초기화
        makeBitmap(context);
        init();
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        // 중력, 회전
        dir.y += GRAVITY * Time.deltaTime;
        ang += dir.x * speed * Time.deltaTime;

        // 이동
        x += dir.x * speed * Time.deltaTime;
        y += dir.y * Time.deltaTime;

        // 지면, 소년과 충돌 검사
        checkGeound();
        checkCollision();

        // 화면을 벗어나면 초기화
        if (x < -r * 4 || x > scrW + r * 4) {
            init();
        }
    }

    //--------------------------
    // 지면과 충돌 판정
    //--------------------------
    private void checkGeound() {
        if (y > ground - r) {
            y = ground - r;
            dir.y = -dir.y;   // 100% 반사
        }

        // 그림자 크기
        shwScale = y / (ground - r);
    }

    //--------------------------
    // 충돌 판정
    //--------------------------
    private void checkCollision() {
        // 소년과 충돌시 반사 - 보류
        if (GameView.boy.checkCollision(x, y, r)) {
            dir.x = -dir.x;
        }
    }

    //--------------------------
    // 비트맵 만들기
    //--------------------------
    private void makeBitmap(Context context) {
        img = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
        r = img.getWidth() / 2;

        shadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.shadow);
        sw = shadow.getWidth() / 2;
        sh = shadow.getHeight() / 2;
    }

    //--------------------------
    // 초기화
    //--------------------------
    private void init() {
        Random rnd = new Random();

        // 좌우 이동 방향
        if ( rnd.nextInt(2) == 1 ) {
            dir.x = 1;
            x = -r * 4;
        } else {
            dir.x = -1;
            x = scrW + r * 4;
        }

        // 초기 위치, 속도
        y = rnd.nextInt(101) + 600;         // 600~700
        speed = rnd.nextInt(101) + 400;     // 400~500
        dir.y = 0;
    }

} // Ball
