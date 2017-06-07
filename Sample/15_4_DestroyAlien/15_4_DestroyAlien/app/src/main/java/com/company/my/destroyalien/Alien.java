package com.company.my.destroyalien;

import android.graphics.Bitmap;

import java.util.Random;

public class Alien {
    // 화면 크기, Random
    private int scrW, scrH;
    private Random rnd = new Random();

    // 속도, 이동 방향(-1, 1), 피격 횟수
    private float speed;
    private int dir;

    // 발사 지연시간, 피격 횟수
    private float fireDelay;
    private int hitCnt;

    // 현재 위치, 크기
    public float x, y;
    public int w, h;

    // 비트맵
    public Bitmap img;

    //--------------------------
    // 생성자
    //--------------------------
    public Alien(int width, int height) {
        scrW = width;
        scrH = height;

        // 비트맵, 초기화
        img = CommonResources.imgAlien;
        w = CommonResources.aw;
        h = CommonResources.ah;

        init();
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        fire();     // 어뢰 발사
        x += speed * dir * Time.deltaTime;

        // 화면을 벗어나면 초기화
        if (x < -w * 2 || x > scrW + w * 2) {
            init();
        }
    }

    //--------------------------
    // 어뢰 발사
    //--------------------------
    private void fire() {
        fireDelay -= Time.deltaTime;

        if (fireDelay < 0) {
            CommonResources.addTorpedo(scrW, scrH, x, y);
            fireDelay = rnd.nextInt(2) + 1;     // 1~2초
        }
    }

    //--------------------------
    // 충돌 판정 <-- Laser
    //--------------------------
    public boolean checkCollision(float tx, float ty, int tw, int th) {
        // 원:사각형 충돌 판정
        boolean hit = MathF.checkCollision(x, y, h, tx, ty, tw, th);
        if (!hit) return false;   // 충돌 없음

        hitCnt++;   // 충돌 횟수 누적

        // 폭파
        if (hitCnt >= 4) {
            CommonResources.sndPlay("Big");
            CommonResources.addExp(x, y, "Big");
            init();    // 폭파되면 초기화
        } else {
            CommonResources.sndPlay("Small");
            CommonResources.addExp(x, y, "Small");
        }

        return true;
    }

    //--------------------------
    // 초기화
    //--------------------------
    private void init() {
        speed = rnd.nextInt(101) + 400;     // 400~500
        y = rnd.nextInt(201) + h;

        // 좌우 이동 방향
        if ( rnd.nextInt(2) == 1 ) {
            x = -w * 2;
            dir = 1;
        } else {
            x = scrW + w * 2;
            dir = -1;
        }

        // 발사 지연 시간, 피격 횟수
        fireDelay = rnd.nextInt(2) + 1;     // 1~2초
        hitCnt = 0;
    }

} // Alien

