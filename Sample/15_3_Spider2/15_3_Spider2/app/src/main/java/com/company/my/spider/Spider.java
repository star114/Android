package com.company.my.spider;

import android.graphics.Bitmap;

public class Spider {
    // 화면 크기
    private int scrW, scrH;

    // 속도
    private final float MAX_SPEED = 1000;
    private float speed = 0;

    // 애니메이션 번호, 이미지 수
    private int animNum;
    private int animCnt = 5;

    // 애니메이션 지연 시간, 결과 시간
    private float animSpan = 0.4f;
    private float animTime;

    // 이동방향(-1, 0, 1), 발사, 정지
    private int dir;
    private boolean canStop;

    // 위치, 크기, 비트맵
    public float x, y;
    public int w, h;
    public int fireCnt = 0;
    public Bitmap img;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Spider(int width, int height) {
        scrW = width;
        scrH = height;

        // 초기이미지
        img = CommonResources.arSpider[0];
        w = CommonResources.sw;
        h = CommonResources.sh;

        // 시작 위치
        x = scrW / 2;
        y = scrH - h - 10;
    }

    //-----------------------------
    // 게임 루프
    //-----------------------------
    public void update() {
        animation();

        // 정지 - 감속
        if (canStop) {
            speed = MathF.lerp(speed, 0, 20 * Time.deltaTime);
            x += dir * speed * Time.deltaTime;

            if (speed < 1f) {
                canStop = false;
                dir = 0;
            }
        } else {   // 이동 - 가속
            speed = MathF.lerp(speed, MAX_SPEED, 5 * Time.deltaTime);
            x += dir * speed * Time.deltaTime;
        }

        // 회면 가장자리를 벗어나면 즉시 정지
        if (x < w || x > scrW - w) {
            // 원위치
            x -= dir * speed * Time.deltaTime;
            canStop = false;
            dir = 0;
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

    //-----------------------------
    // 발사 - 동기화  <-- setAction
    //-----------------------------
    private void fire() {
        synchronized (GameView.mPoison) {
            // 사운드, 독액 추가, 발사회수 증가
            CommonResources.sndPlay("Poison");
            GameView.mPoison.add( new Poison(x, y) );
            fireCnt++;
        }
    }

    //--------------------------
    // 이동 <-- Touch Event
    //--------------------------
    public void setAction(float tx, float ty) {
        if ( MathF.hitTest(x, y, w, tx, ty) ) {
            fire();
        } else {
            dir =  (tx < x) ? -1 : 1;
            canStop = false;
        }
    }

    //--------------------------
    // 정지 <-- Touch Event
    //--------------------------
    public void stop() {
        canStop = true;
    }

} // Spider
