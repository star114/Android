package com.company.my.redrabbit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Rabbit {
    private int scrW, scrH;

    // 이동 속도, 걷기인가?
    private final int MAX_SPEED = 400;
    private int speed;
    private boolean canWalk;

    // 이미지 배열
    private Bitmap[] arImg = new Bitmap[8];
    private int imgCnt = 8;

    // 애니메이션 번호, 지연시간, 경과시간
    private int animNum = 0;
    private float animSpan = 0.1f;
    private float animTime = 0;

    // 현재 위치, 이동 방향, 지면
    public float x, y;
    public int dir = 1;   // (1, -1)
    public float ground;

    // 현재 이미지
    public Bitmap img;
    public int w, h;

    // 그림자
    public Bitmap shadow;
    public int sw, sh;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Rabbit(Context context, int w, int h) {
        scrW = w;
        scrH = h;

        makeBitmap(context);

        // 초기 위치
        ground = scrH * 0.9f;
        x = scrW / 3;
        y = ground - this.h;
    }

    //-----------------------------
    // 이동
    //-----------------------------
    public void update() {
        if (canWalk) {
            animation();
        }

        x += dir * speed * Time.deltaTime;

        // 화면 경계이면 원위치
        if (x < w || x > scrW - w) {
            x -= dir * speed * Time.deltaTime;
            speed = 0;
        }
    }

    //-----------------------------
    // 애니메이션
    //-----------------------------
    private void animation() {
        animTime += Time.deltaTime;
        if (animTime < animSpan) return;

        animTime = 0;
        animNum = MathF.repeat(animNum, imgCnt);

        // 마지막 애니메이션 재생 후 정지
        if (animNum == 0) {
            speed = 0;
            canWalk = false;
        }

        img = arImg[animNum];
    }

    //-----------------------------
    // 걷기 <-- Touch Event
    //-----------------------------
    public void setAction(float tx) {
        dir = (x < tx) ? 1 : -1;
        speed = MAX_SPEED;
        canWalk = true;
    }

    //-----------------------------
    // 비트맵 만들기
    //-----------------------------
    private void makeBitmap(Context context) {
        // 그림자
        shadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.shadow);
        sw = shadow.getWidth() / 2;
        sh = shadow.getHeight() / 2;

        // 토끼
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit);
        w = tmp.getWidth() / 4;
        h = tmp.getHeight();

        // 이미지 분리와 복사
        for (int i = 0; i < 4; i++) {
            arImg[i] = Bitmap.createBitmap(tmp, w * i, 0, w, h);
            arImg[i + 4] = arImg[i];
        }

        // 초기 이미지
        w /= 2;
        h /= 2;
        img = arImg[0];
    }

} // Rabbit
