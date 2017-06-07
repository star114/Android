package com.company.my.ninja;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

public class Ninja {
    // 애니메이션 이미지
    private final int JUMP = 0;
    private final int RUN = 1;

    // 점프 속도, 중력, 점프 카운터, 점프중?
    private int speedJump = 1000;
    private int gravity = 2000;
    private int jumpCnt = 0;
    private boolean isJump;

    // 이미지 배열
    private int imgCnt = 8;
    private Bitmap[][] arImg = new Bitmap[2][imgCnt];

    // 애니메이션 번호, 지연시간, 경과시간
    private int animNum = 0;
    private float animSpan = 1f / 12;
    private float animTime = 0;

    // 현재 위치, 이동 방향
    public float x, y;
    public PointF dir = new PointF(0, 0);

    // 지면, 왼쪽 방향인가?
    public float ground;
    public boolean isLeft;

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
        if (!isJump) return;

        // 점프의 중력 처리
        dir.y += gravity * Time.deltaTime;
        y += dir.y * Time.deltaTime;

        // 착지
        if (y > ground - h) {
            y = ground - h;
            jumpCnt = 0;
            isJump = false;
            animNum = 0;
        }
    }

    //-----------------------------
    // 애니메이션
    //-----------------------------
    private void animation() {
        animTime += Time.deltaTime;
        if (animTime <= animSpan) return;

        // 정지 상태는 고정된 이미지 표시
        if (dir.x == 0 && !isJump) {
            img = arImg[1][1];
            return;
        }

        // 다음 이미지 번호
        animTime = 0;
        animNum = MathF.repeat(animNum, imgCnt);

        // Jump중인가?
        if (isJump) {
            // 착지때까지 마지막 동작 유지
            if (animNum == 0) {
                animNum = imgCnt - 1;
            }
            img = arImg[JUMP][animNum];
        } else {
            img = arImg[RUN][animNum];
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
        img = arImg[1][1];
    }

    //-----------------------------
    // 이동 방향, Jump <-- TouchEvent
    //-----------------------------
    public void setAction(boolean btnLeft, boolean btnRight, boolean btnJump) {
        // 왼쪽으로 이동
        if (btnLeft) {
            dir.x = -1;
            isLeft= true;
        }

        // 오른쪽으로 이동
        if (btnRight) {
            dir.x = 1;
            isLeft = false;
        }

        // 정지
        if (!btnLeft && !btnRight) {
            dir.x = 0;
        }

        // 점프 - 2단 점프 허용
        if (btnJump && jumpCnt < 2) {
            dir.y = -speedJump;
            animNum = 0;
            jumpCnt++;

            isJump = true;
        }
    }

} // Ninja
