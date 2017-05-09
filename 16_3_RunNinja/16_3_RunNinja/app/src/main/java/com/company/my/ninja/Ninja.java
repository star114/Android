package com.company.my.ninja;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

public class Ninja {
    private enum STATE { JUMP, RUN };
    private STATE state = STATE.RUN;

    // 점프 속도, 중력
    private int speedJump = 1200;
    private int gravity = 2000;

    // 이미지 배열
    private int imgCnt = 8;
    private Bitmap[][] arImg = new Bitmap[2][imgCnt];

    // 애니메이션 번호, 지연시간, 경과시간
    private int animNum = 0;
    private float animSpan = 1f / 12;
    private float animTime = 0;

    // 현재 위치, 이동 방향, 지면
    public float x, y;
    public PointF dir = new PointF(1, 0);
    public float ground;

    // 현재 이미지
    public Bitmap img;
    public int w, h;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Ninja(Context context, int width, int height) {
        makeBitmap(context);

        // 초기 위치
        ground = height * 0.9f;
        x = width / 2;
        y = ground - h;
    }

    //-----------------------------
    // 이동
    //-----------------------------
    public void update() {
        animation();
        if (state == STATE.RUN) return;

        // 점프의 중력 처리
        dir.y += gravity * Time.deltaTime;
        y += dir.y * Time.deltaTime;

        // 착지
        if (y > ground - h) {
            y = ground - h;
            state = STATE.RUN;
            animNum = 0;
        }
    }

    //-----------------------------
    // 애니메이션
    //-----------------------------
    private void animation() {
        animTime += Time.deltaTime;

        if (animTime > animSpan) {
            animTime = 0;
            animNum = MathF.repeat(animNum, imgCnt);

            // Jump는 착지때까지 마지막 동작 유지
            if (state == state.JUMP && animNum == 0) {
                animNum = imgCnt - 1;
            }
        }

        img = arImg[state.ordinal()][animNum];
    }

    //-----------------------------
    // 이동 방향, Jump  <-- Touch Event
    //-----------------------------
    public void setAction(float tx, float ty) {
        if (state == STATE.JUMP) return;

        if ( MathF.hitTest(x, y, h, tx, ty) ) {
            // 점프
            dir.y = -speedJump;
            state = STATE.JUMP;
            animNum = 0;
        } else {
            // 이동 방향 바꾸기
            dir.x = (x < tx) ? 1 : -1;
        }
    }

    //-----------------------------
    // 비트맵 만들기
    //-----------------------------
    private void makeBitmap(Context context) {
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ninja);
        w = tmp.getWidth() / imgCnt;
        h = tmp.getHeight() / 2;

        // 이미지 분리
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < imgCnt; j++)
            arImg[i][j] = Bitmap.createBitmap(tmp, w * j, i * h, w, h);
        }

        // 초기 이미지
        w /= 2;
        h /= 2;
        img = arImg[state.ordinal()][0];
    }

} // Ninja
