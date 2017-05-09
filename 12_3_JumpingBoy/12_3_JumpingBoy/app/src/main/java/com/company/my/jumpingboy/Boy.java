package com.company.my.jumpingboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

public class Boy {
    // 화면 크기, 바닥 높이
    private int scrW, scrH;

    // 속도, 중력, 이동 방향
    private int speedWalk = 300;
    private int speedJump = 1000;
    private int gravity = 2000;
    private PointF dir = new PointF();

    // 애니메이션 속도, 경과 시간
    private float animSpan = 0.2f;    // 초당 5프레임
    private float animTime = 0;

    // 비트맵, 애니메이션 번호
    private Bitmap[] arrBoy = new Bitmap[5];
    private int animNum = 0;

    // 현재 위치
    public float x, y;

    // 바닥 높이, 착지상태인가?
    public int ground;
    public boolean isGround = true;

    // 소년 이미지, 크기
    public Bitmap boy;
    public int w, h;

    // 그림자, 크기
    public Bitmap shadow;
    public int sw, sh;
    public float shadowScale;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Boy(Context context, int width, int height) {
        // 화면 크기, 지면 높이
        scrW = width;
        scrH = height;
        ground = (int) (scrH * 0.9f);

        // 애니메이션 이미지와 그림자 만들기
        makeBitmap(context);

        // Class 초기화
        initBoy();
    }

    //-----------------------------
    // 이동
    //-----------------------------
    public void update() {
        // 애니메이션
        animationBoy();

        // 점프중이면 중력 적용
        if (!isGround) {
            dir.y += gravity * Time.deltaTime;
        }

        // 이동
        x += dir.x * Time.deltaTime;
        y += dir.y * Time.deltaTime;

        // 그림자 축소 비율
        // shadowScale = y / (ground - bh);
        shadowScale = (float) Math.pow(y / (ground - h), 1.2f);

        // 지면에 있는 상태인가?
        checkGround();

        // 화면을 벗어나면 초기화
        if (x > scrW + w * 2) {
            initBoy();
        }

    }

    //-----------------------------
    // 점프 <- Touch Event
    //-----------------------------
    public void jump() {
        if (isGround) {
            dir.y = -speedJump;
            isGround = false;
        }
    }

    //-----------------------------
    // 애니메이션
    //-----------------------------
    private void animationBoy() {
        // 점프중인가?
        if (!isGround) {
            boy = arrBoy[4];
            return;
        }

        // 다음 애니메이션 번호
        animTime += Time.deltaTime;
        if (animTime > animSpan) {
            animNum++;
            animTime = 0;

            // 마지막 Cell인가?
            if (animNum > 3) {
                animNum = 0;
            }
        }

        boy = arrBoy[animNum];
    }

    //-----------------------------
    // 착지인가?
    //-----------------------------
    private void checkGround() {
        if (y > ground - h) {
            y = ground - h;

            isGround = true;
        }
    }

    //-----------------------------
    // 비트맵 만들기
    //-----------------------------
    private void makeBitmap(Context context) {
        // 소년 이미지
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy);
        int tw = tmp.getWidth() / 5;
        int th = tmp.getHeight();

        // 이미지 분리
        for (int i = 0; i < 5; i++) {
            arrBoy[i] = Bitmap.createBitmap(tmp, tw * i, 0, tw, th);
        }

        w = tw / 2;
        h = th / 2;

        // 그림자
        shadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.shadow);
        sw = shadow.getWidth() / 2;
        sh = shadow.getHeight() / 2;
    }

    //-----------------------------
    // 초기화
    //-----------------------------
    private void initBoy() {
        // 초기 위치
        x = -w * 2;
        y = ground - h;

        // 애니메이션 번호
        isGround = true;
        animNum = 0;
        boy = arrBoy[animNum];

        // 이동 방향
        dir.x = speedWalk;
        dir.y = 0;
    }

} // Boy