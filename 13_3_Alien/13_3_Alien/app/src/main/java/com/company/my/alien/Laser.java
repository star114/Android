package com.company.my.alien;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Laser {
    // 화면 크기
    private int scrW, scrH;

    // 속도, 방향
    private int speed = 1200;
    private PointF dir = new PointF();

    // 현재 위치, 크기
    public PointF pos = new PointF();
    public int w, h;
    public float ang;

    // 비트맵, 소멸?
    public Bitmap img;
    public boolean isDead;

    //--------------------------
    // 생성자
    //--------------------------
    public Laser(int width, int height, PointF pos, PointF dir, float ang) {
        scrW = width; // 화면 크기
        scrH = height;

        this.pos.set(pos);     // 초기 위치
        this.dir.set(dir);     // 발사 방향
        this.ang = ang;        // 회전 각도

        img = CommonResources.imgLaser;
        w = CommonResources.lw;
        h = CommonResources.lh;
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        pos.x += dir.x * speed * Time.deltaTime;
        pos.y += dir.y * speed * Time.deltaTime;

        // 화면을 벗어나면 소거
        if (pos.x < -w || pos.x > scrW + w || pos.y < -h || pos.y > scrH + h) {
            isDead = true;
        }
    }

} // Laser

