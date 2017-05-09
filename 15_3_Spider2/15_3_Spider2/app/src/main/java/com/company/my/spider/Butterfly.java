package com.company.my.spider;

import android.graphics.Bitmap;
import java.util.Random;

//--------------------------
// Butterfly
//--------------------------
public class Butterfly {
    // 화면 크기, 나비 종류
    private int scrW, scrH;
    private int kind;

    // 애니메이션 속도, 경과 시간, 애니메이션 번호
    private float animSpan;
    private float animTime = 0;
    private int animNum = 0;

    // 현재 위치, 이동 방향, 속도
    public float x, y;
    private int dir;
    private int speed;

    // 이미지, 크기
    public Bitmap img;
    public int w, h;

    // 충돌 영역 크기, 점수. 사망?
    public int score;
    public boolean isDead;

    //--------------------------
    // 생성자
    //--------------------------
    public Butterfly(int width, int height) {
        scrW = width;
        scrH = height;

        init();
    }

    //--------------------------
    // 나비 초기화
    //--------------------------
    public void init() {
        // 나비 종류
        Random rnd = new Random();
        kind = rnd.nextInt(CommonResources.FRY_CNT);              // 0~5

        // 초기 위치, 점수
        speed = rnd.nextInt(401) + 200;     // 200 ~ 600
        y = rnd.nextInt(301) + 150;         // 150 ~ 450
        score = (rnd.nextInt(5) + 1) * 10;  // 10 ~ 50

        // 좌우 이동 방향
        if (rnd.nextInt(2) == 0) {          // 0, 1
            dir = -1;
            x = scrW + w * 4;
        } else {
            dir = 1;
            x = -w * 4;
        }

        // 애니메이션 시간
        animSpan = (rnd.nextInt(6) + 7) /100f;    // 0.07 ~ 0.12
        isDead = false;

        // 초기 이미지, 크기
        img = CommonResources.arFly[kind][0];
        w = CommonResources.bw;
        h = CommonResources.bh;
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        // 애니메이션, 이동
        animationFly();
        x += dir * speed * Time.deltaTime;

        // 화면을 멀리 벗어나면 나비 초기화
        if (x < -w * 4 || x > scrW + w * 4) {
            init();
        }
    }

    //-----------------------------
    // 애니메이션
    //-----------------------------
    private void animationFly() {
        // 다음 애니메이션 번호
        animTime += Time.deltaTime;
        if (animTime > animSpan) {
            animTime = 0;
            animNum = MathF.repeat(animNum, 10);
        }

        img = CommonResources.arFly[kind][animNum];
    }

    //--------------------------
    // 충돌 판정 <-- Poison
    //--------------------------
    public boolean checkCollision(float px, float py, int r) {
        // 원형:원형의 충돌
        boolean hit = MathF.checkCollision(x, y, w, px, py, r);
        if (hit) {
            // 사운드 재생 및 소멸 설정
            CommonResources.sndPlay("Capture");
            isDead = true;
        }

        return isDead;
    }

} // Butterfly
