package com.company.my.bounceball;

import android.graphics.Bitmap;
import android.graphics.PointF;

//--------------------------
// Bounce Ball
//--------------------------
public class Ball {
    // 화면 크기
    private int scrW, scrH;

    // 바닥 높이
    private float ground;

    // 이동, 회전 속도(초속)
    private int speed = 300;
    private int rotAng = 120;

    // 중력, 반발 계수
    private float gravity = 1500f;
    private float bounce = 0.8f;

    // 이동 방향
    private PointF dir = new PointF();

    // 공의 위치, 반지름
    public float x, y;
    public int r;

    // 현재 각도, 비트맵, 소멸?
    public float ang;
    public Bitmap ball;
    public boolean isDead;

    //--------------------------
    // 생성자
    //--------------------------
    public Ball(int width, int height, float px, float py) {
        scrW = width;        // 화면 크기
        scrH = height;
        x = px;              // 터치 위치
        y = py;

        // 공의 이미지와 크기
        ball = CommonResources.ball;
        r = CommonResources.r;

        // 바닥의 높이
        ground = scrH * 0.8f;

        // 공의 이동 방향
        dir.x = speed;
        dir.y = 0;
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        // 현재의 회전 각도
        ang += rotAng * Time.deltaTime;

        // 중력
        dir.y += gravity * Time.deltaTime;

        // 이동
        x += dir.x * Time.deltaTime;
        y += dir.y * Time.deltaTime;

        // 바닥과 충돌인가?
        if (y > ground) {
            y = ground;

            dir.y = -dir.y * bounce;    // 반사
        }

        // 화면을 벗어났나?
        isDead = (x > scrW + r);
    }

} // Ball
