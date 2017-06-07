package com.company.my.spider;

import android.graphics.Bitmap;

public class Spider {
    // 상태 코드
    private final int IDLE = 0;
    private final int MOVE = 1;
    private final int STOP = 2;

    // 현재 상태
    private int state = IDLE;

    // 화면 크기, 최대 속도, 현재 속도
    private int scrW, scrH;
    private final float MAX_SPEED = 1000;
    private float speed = 0;

    // 애니메이션 번호, 이미지 수
    private int animNum;
    private int animCnt = 5;

    // 애니메이션 지연 시간, 결과 시간
    private float animSpan = 0.4f;
    private float animTime;

    // 포이즌 발사 간격, 지연 시간
    private float fireSpan = 0.2f;
    private float fireTime = fireSpan;

    // 이동방향(-1, 0, 1), 발사
    private int dir;
    private boolean canFire;

    // 위치, 크기, 비트맵
    public float x, y;
    public int w, h;
    public Bitmap img;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Spider(int width, int height) {
        scrW = width;
        scrH = height;

        // 초기 이미지
        img = CommonResources.arSpider[0];
        w = CommonResources.sw;
        h = CommonResources.sh;

        // 초기 위치
        x = scrW / 2;
        y = scrH - h - 10;
    }

    //-----------------------------
    // 게임 루프
    //-----------------------------
    public void update() {
        animation();
        if (canFire) {
            firePosion();
        }

        switch (state) {
        case MOVE :    // 이동 - 가속
            speed = MathF.lerp(speed, MAX_SPEED, 5 * Time.deltaTime);
            break;
        case STOP :     // 정지 - 감속
            speed = MathF.lerp(speed, 0, 20 * Time.deltaTime);
        }

        x += dir * speed * Time.deltaTime;

        // 회면 가장자리이면 정지
        if (x < w || x > scrW - w) {
            // 원위치
            x -= dir * speed * Time.deltaTime;
            dir = 0;
        }
    }

    //-----------------------------
    // 독액 발사 - 동기화
    //-----------------------------
    private void firePosion() {
        fireTime += Time.deltaTime;

        if (fireTime > fireSpan) {
            fireTime = 0;

            synchronized (GameView.mPoison) {
                // 사운드, Poison
                CommonResources.sndPlay();
                GameView.mPoison.add( new Poison(x, y) );
            }
        }
    }

    //-----------------------------
    // 애니메이션
    //-----------------------------
    private void animation() {
        animTime += Time.deltaTime;

        if (animTime > animSpan) {
            animTime = 0;
            animNum = MathF.repeat(animNum, animCnt);
            img = CommonResources.arSpider[animNum];
        }
    }

    //--------------------------
    // Action 시작 <-- ACTION_DOWN
    //--------------------------
    public void startAction(float tx, float ty) {
        if ( MathF.hitTest(x, y, w, tx, ty) ) {
            canFire = true;
        } else {
            dir = (x < tx) ? 1 : -1;
            state = MOVE;
        }
    }

    //--------------------------
    // Action 끝 <-- ACTION_UP
    //--------------------------
    public void stopAction() {
        canFire = false;
        if (state == MOVE) {
            state = STOP;
        }
    }

} // Spider
