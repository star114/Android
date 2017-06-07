package com.company.my.parkalone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import java.util.Random;

public class Boy {
    // 상태 코드
    private enum STATE { CRY, DOWN, IDLE, JUMP, WALK };
    private STATE state;

    // 화면 크기, Random
    private int scrW, scrH;
    private Random rnd = new Random();

    // 속도, 중력
    private float moveSpeed = 600;
    private float currSpeed;
    private float speedJump = 1300;
    private float gravity = 2000;

    // 애니메이션
    private int aniNum = 0;
    private float aniSpan = 0.15f;
    private float aniTime;

    // 대기 시간
    private  float waitTime = 0.5f;

    // 원본 비트맵, 목적지
    private Bitmap[][]  arImg = new Bitmap[5][4];
    private float tx, ty;

    // 현재 위치, 이동 방향
    public float x, y;
    public PointF dir = new PointF();

    // 비트맵, 크기
    public Bitmap img;
    public int w, h;

    // 그림자, 크기
    public Bitmap shadow;
    public int sw, sh;

    // 그림자 축소 비율, 지면 높이
    public float shwScale = 1;
    public float ground;

    //--------------------------
    // 생성자
    //--------------------------
    public Boy(Context context, int width, int height) {
        scrW = width;
        scrH = height;

        // 비트맵, 초기화
        makeBitmap(context);
        init();
    }

    //--------------------------
    // update
    //--------------------------
    public void update() {
        switch (state) {
            case IDLE :
                currSpeed = 0;
                break;
            case WALK :
                currSpeed = moveSpeed;
                break;
            case CRY:
                setCry();
                break;
            case DOWN :
                setDown();
                break;
        }

        moveBoy();
        checkGeound();
        checkTarget();
        animation();
    }

    //--------------------------
    // 이동
    //--------------------------
    private void moveBoy() {
        // 중력
        dir.y += gravity * Time.deltaTime;

        // 이동
        x += dir.x * currSpeed * Time.deltaTime;
        y += dir.y * Time.deltaTime;
    }

    //--------------------------
    // 지면과 충돌 판정
    //--------------------------
    private void checkGeound() {
        if (y > ground - h) {
            y = ground - h;
            dir.y = 0;

            if (state == STATE.JUMP) {
                state = STATE.IDLE;
            }
        }

        // 그림자 크기
        shwScale = y / (ground - h);
    }

    //--------------------------
    // 목적지 근처인가?
    //--------------------------
    private void checkTarget() {
        if (state == STATE.WALK && Math.abs(x - tx) < 2) {
            state = STATE.IDLE;
        }

        // 이동 범위를 화면으로 제한
        if (x < w) {
            x = w;
            state = STATE.IDLE;
        }
        if (x > scrW - w) {
            x = scrW - w;
            state = STATE.IDLE;
        }
    }

    //--------------------------
    // 주저앉기
    //--------------------------
    private void setDown() {
        if (state != STATE.JUMP) {
            currSpeed = 0;
        }

        waitTime -= Time.deltaTime;
        if (waitTime <= 0) {
            state = STATE.CRY;
            waitTime = 1f;
        }
    }

    //--------------------------
    // 울기
    //--------------------------
    private void setCry() {
        currSpeed = 0;
        waitTime -= Time.deltaTime;

        if (waitTime <= 0) {
            waitTime = 0.5f;
            state = STATE.IDLE;
        }
    }

    //--------------------------
    // 애니메이션
    //--------------------------
    private void animation() {
        if (state != STATE.WALK) {
            aniNum = 0;
        } else {
            aniTime += Time.deltaTime;

            if (aniTime > aniSpan) {
                aniTime = 0;
                aniNum = MathF.repeat(aniNum, 4);
            }
        }

        img = arImg[state.ordinal()][aniNum];
    }

    //--------------------------
    // 비트맵 만들기
    //--------------------------
    private void makeBitmap(Context context) {
        // 그림자
        shadow = BitmapFactory.decodeResource( context.getResources(), R.drawable.shadow);
        sw = shadow.getWidth();
        sh = shadow.getHeight();

        // 그림자 2배 확대
        shadow = Bitmap.createScaledBitmap(shadow, sw * 2, sh * 2, true);

        // 소년
        Bitmap tmp = BitmapFactory.decodeResource( context.getResources(), R.drawable.boy);
        w = tmp.getWidth() / 4;
        h = tmp.getHeight() / 5;

        // 울기
        arImg[0][0] = Bitmap.createBitmap(tmp, 0, 0, w, h);

        // 앉기
        arImg[1][0] = Bitmap.createBitmap(tmp, 0, h, w, h);

        // 정지
        arImg[2][0] = Bitmap.createBitmap(tmp, 0, h * 2,  w, h);

        // 점프
        arImg[3][0] = Bitmap.createBitmap(tmp, 0, h * 3, w, h);

         // 울기, 앉기, 정지, 점프
         for (int i = 0; i <= 3; i++) {
             arImg[i][0] = Bitmap.createBitmap(tmp, 0, h * i, w, h);
         }

        // 걷기
        for (int i = 0; i < 4; i++) {
            arImg[4][i] = Bitmap.createBitmap(tmp, w * i, h * 4,  w, h);
        }

        // 초기 이미지
        w /= 2;
        h /= 2;
    }

    //--------------------------
    // 초기화
    //--------------------------
    private void init() {
        // 이동 방향
        dir.x = 1;
        dir.y = 0;
        ground = scrH * 0.9f;

        // 목적지와 현재 위치
        tx = x = scrW / 2;
        ty = y = ground - h;

        state = STATE.IDLE;
        img = arImg[state.ordinal()][0];
    }

    //--------------------------
    // setAction <-- Touch Event
    //--------------------------
    public void setAction(float tx, float ty) {
        // 걷기와 정지 상태만 점프 가능
        if (state != STATE.WALK && state != STATE.IDLE) return;

        // 소년 터치?
        if ( MathF.hitTest(x, y, w, tx, ty) ) {
            dir.y = -speedJump;
            state = STATE.JUMP;
        } else {
            // 목적지 설정
            this.tx = tx;
            this.ty = y;

            // 이동 방향 설정
            dir.x = x < tx ? 1 : -1;
            state = STATE.WALK;
        }
    }

    //--------------------------
    // 충돌 판정 <-- Ball
    //--------------------------
    public boolean checkCollision(float tx, float ty, int r) {
        boolean hit = false;

        // 연속 충돌 금지
        if (state != STATE.DOWN && state != STATE.CRY) {
            // 원:사각형 충돌
            if (MathF.checkCollision(tx, ty, r, x, y, w * 0.9f, h * 0.9f)) {
                state = STATE.DOWN;
                hit = true;
            }
        }
        return hit;
    }

} // Boy
