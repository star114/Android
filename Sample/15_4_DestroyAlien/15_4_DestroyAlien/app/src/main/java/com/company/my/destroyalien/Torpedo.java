package com.company.my.destroyalien;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Torpedo {
    // 화면 크기
    private int scrW, scrH;

    // 속도, 방향, 목표물 좌표
    private final int SPEED = 800;
    private PointF dir = new PointF();
    private PointF target = new PointF();

    // 현재 위치, 크기, 회전각
    public float x, y;
    public int w, h;
    public float ang;

    // 비트맵, 소멸?
    public Bitmap img;
    public boolean isDead;

    //--------------------------
    // 생성자
    //--------------------------
    public Torpedo(int width, int height, float px, float py) {
        scrW = width;       // 화면 크기
        scrH = height;
        x = px;             // 초기 위치
        y = py;

        img = CommonResources.imgTorpedo;
        w = CommonResources.tw;
        h = CommonResources.th;

        // 목표물의 좌표와 이동 방향 계산
        getTarget();
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        checkCollision();  // 충돌 판정

        x += dir.x * SPEED * Time.deltaTime;
        y += dir.y * SPEED * Time.deltaTime;

        // 화면을 벗어나면 소거
        if (x < -w || x > scrW + w || y < -h || y > scrH + h) {
            isDead = true;
        }
    }

    //--------------------------
    // X-Wing과 충돌 탐지
    //--------------------------
    private void checkCollision() {
        if ( GameView.xwing.checkCollision(x, y, h) ) {
            isDead = true;
        }
    }

    //--------------------------
    // X-Wing의 위치로 이동 방향 설정
    //--------------------------
    private void getTarget() {
        target.x = GameView.xwing.x;
        target.y = GameView.xwing.y;

        // 비행 방향 및 회전 각도
        PointF pos = new PointF(x, y);
        dir.set( MathF.direction(pos, target) );
        ang = MathF.cwDegree(pos, target);
    }

} // Torpedo
