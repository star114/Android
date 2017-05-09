package com.company.my.ancientboy;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Boy {
    // 상태 코드
    private final int IDLE = 0;
    private final int RUN = 1;
    private final int JUMP = 2;

    // 화면 크기
    private int scrW, scrH;

    // 속도, 점프, 중력, 이동 방향
    private float speed = 600;
    private float speedJump = 800;
    private float gracvity = 1200;
    private PointF dir = new PointF();

    // 현재 상태, 애니메이션 번호, 이미지 수
    private int state = IDLE;
    private int animNum;
    private int animCnt = 5;

    // 애니메이션 지연 시간, 결과 시간
    private float animSpan = 0.2f;
    private float animTime;

    // 위치, 크기, 좌우 방향
    public float x, y;
    public int w, h;
    public int leftRight = 1;

    // 지면, 현재 이미지
    public float ground;
    public Bitmap img;

    // 그림자 크기, 비율, 이미지
    public int sw, sh;
    public float sdScale = 1;
    public Bitmap shadow;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Boy(int width, int height) {
        scrW = width;
        scrH = height;

        //  소년 이미지
        img = CommonResources.arBoy[IDLE][0];
        w =  CommonResources.bw;
        h =  CommonResources.bh;

        // 그림자
        shadow = CommonResources.shadow;
        sw = CommonResources.sw;
        sh = CommonResources.sh;

        // 초기 위치
        ground = scrH * 0.85f;
        x = scrW / 2;
        y = ground - h;
    }

    //-----------------------------
    // 게임 루프
    //-----------------------------
    public void update() {
        animation();

        switch (state) {
            case JUMP:
                dir.y += gracvity * Time.deltaTime;
                sdScale = y / (ground - h);
                checkGround();
                // break없음 – case RUN 수행할 것
            case RUN:
                x += dir.x * Time.deltaTime;
                y += dir.y * Time.deltaTime;
        }

        // 화면을 벗어나면 반대 방향에서 등장
        if (x > scrW + w) x = -w;
        if (x < -w) x = scrW + w;
    }

    //-----------------------------
    // 애니메이션
    //-----------------------------
    private void animation() {
        animTime += Time.deltaTime;
        if (animTime > animSpan) {
            animTime = 0;

            animNum = MathF.repeat(animNum, animCnt);
            img = CommonResources.arBoy[state][animNum];
        }
    }

    //--------------------------
    // 애니메이션 설정 <-- 상태 변경시
    //--------------------------
    private void startAnimation() {
        animNum = 0;
        img = CommonResources.arBoy[state][animNum];
    }

    //--------------------------
    // 착지인가? <-- 점프중일 때
    //--------------------------
    private void checkGround() {
        if (y + h > ground) {
            y = ground - h;
            dir.y = 0;

            state = RUN;
            startAnimation();
        }
    }

    //--------------------------
    // Hit Test <-- GameView
    //--------------------------
    private boolean hitTest(float tx, float ty) {
        return MathF.hitTest(x, y, h, tx, ty);
    }

    //region Gesture Event Call
    //--------------------------
    // 정지 <-- Gesture.onDown
    //--------------------------
    public void stop(float tx, float ty) {
        if ( state == RUN && hitTest(tx, ty) ) {
            dir.x = 0;
            state = IDLE;
            startAnimation();
        }
    }

    //--------------------------
    // 달리기 <-- Gesture.onScroll
    //--------------------------
    public void run(float dist) {
        leftRight = (dist > 0) ? -1 : 1;
        dir.x = leftRight * speed;

        if (state != RUN) {
            state = RUN;
            startAnimation();
        }
    }

    //--------------------------
    // Jump <-- Gesture.onDoubleTab
    //--------------------------
    public void jump() {
        if (state == RUN) {
            dir.y = -speedJump;
            state = JUMP;
            startAnimation();
        }
    }
    //endregion

} // boy
