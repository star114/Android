package com.company.my.destroyalien;

import android.graphics.Bitmap;

public class Xwing {
    // X-WING의 상태
    private enum STATE { IDLE, START, STOP };
    private STATE state = STATE.IDLE;

    // 화면 크기
    private int scrW, scrH;

    // 애니메이션 지연 시간, 경과시간, 이미지 번호
    private final float ANIM_DELAY = 0.5f;
    private float animSpan;
    private int imgNum;

    // 최대 속도, 현재 속도
    private final float MAX_SPEED = 1200;
    private float speed;

    // 목적지, 이동 방향(-1, 0, 1)
    private float tx;
    private int dir;

    // 현재 위치, 크기
    public float x, y;
    public int w, h;

    // 비트맵
    public Bitmap img;

    //--------------------------
    // 생성자
    //--------------------------
    public Xwing(int width, int height) {
        scrW = width;      // 화면 크기
        scrH = height;

        // 이미지, 크기
        img = CommonResources.arXwing[0];
        w = CommonResources.xw;
        h = CommonResources.xh;

        // 초기 위치 - 화면 아래 가운데
        x = scrW / 2;
        y = scrH - h - 40;
    }

    //--------------------------
    // 애니메이션 <-- update
    //--------------------------
    private void animation() {
        animSpan += Time.deltaTime;
        if (animSpan > ANIM_DELAY) {
            animSpan = 0;

            imgNum = 1 - imgNum;    // 0, 1, 0, 1, ...
            img = CommonResources.arXwing[imgNum];
        }
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        animation();

        switch (state) {
        case START :    // 가속
            speed = MathF.lerp(speed, MAX_SPEED, 5 * Time.deltaTime);
            break;
        case STOP :    // 감속
            speed = MathF.lerp(speed, 0, 10 * Time.deltaTime);
        }

        x += speed * dir * Time.deltaTime;

        // 목적지의 50픽셀 이내이면 정지 모드로 전환
        if (MathF.distSqr(x, y, tx, y) < 2500) {
            state = STATE.STOP;
        }

        // 화면 가장자리면 정지
        if (x < w || x > scrW - w) {
            // 원위치
            x -= speed * dir * Time.deltaTime;
            speed = dir = 0;
        }
    }

    //--------------------------
    // Set Action <-- Touch Event
    //--------------------------
    public void setAction(float tx, float ty) {
        if ( MathF.hitTest(x, y, h, tx, ty) ) {
            fire();
        } else {
            startMove(tx);
        }
    }

    //--------------------------
    // 레이저 발사 <-- setAction
    //--------------------------
    private void fire() {
        CommonResources.sndPlay("Laser");

        // 레이저 추가
        CommonResources.addLaser(scrW, scrH, x - w, y);
        CommonResources.addLaser(scrW, scrH, x + w, y);
    }

    //--------------------------
    // 목적지로 출발 <-- setAction
    //--------------------------
    private void startMove(float tx) {
        this.tx = tx;
        dir = (x < tx) ? 1 : -1;
        state = STATE.START;
    }

    //--------------------------
    // 충돌판정(원:원) <-- Torpedo
    //--------------------------
    public boolean checkCollision(float tx, float ty, int tr) {
        boolean hit = MathF.checkCollision(x, y, h * 0.7f, tx, ty, tr);

        if (hit) {  // 충돌시 작은 사운드, 작은 불꽃 표시;
            CommonResources.sndPlay("Small");
            CommonResources.addExp(tx, ty + tr, "Small");
        }

        return hit;
    }

} // Xwing
