package com.company.my.alien;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Alien {
    // 화면 크기
    private int scrW, scrH;

    // 속도
    private final float MAX_SPEED = 1000;
    private float speed = 0;

    // 이동 방향, 목적지
    private PointF dir = new PointF(0, -1);
    private PointF target = new PointF();

    //  출발할 수 있는가?
    private boolean canStart;

    // 위치, 크기, 회전각
    public PointF pos = new PointF();
    public int w, h;
    public float ang;

    // 비트맵
    public Bitmap img;

    //--------------------------
    // 생성자
    //--------------------------
    public Alien(int width, int height) {
        scrW = width; // 화면 크기
        scrH = height;

        // Arien의 이미지
        img = CommonResources.imgAlien;
        w = CommonResources.aw;
        h = CommonResources.ah;

        // 초기 위치 - 화면 가운데
        pos.x = scrW / 2;
        pos.y = scrH / 2;
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        // 목적지로 출발
        if (canStart) {
            speed = MathF.lerp(speed, MAX_SPEED, 3 * Time.deltaTime);
        }

        // 목적지 근처에서 정지
        if (MathF.distance(pos, target) < 50) {
            canStart = false;
            speed = MathF.lerp(speed, 0, 15 * Time.deltaTime);
        }

        pos.x += dir.x * speed * Time.deltaTime;
        pos.y += dir.y * speed * Time.deltaTime;
    }

    //--------------------------
    // 액션 설정 <-- Touch Event
    //--------------------------
    public void setAction (float tx, float ty) {
        // 터치 위치가 우주선의 내부인가?
        if ( MathF.hitTest(pos.x, pos.y, w * 0.9f, tx, ty) ) {
            fire();
        } else {
            start(tx, ty);
        }
    }

    //--------------------------
    // 목적지 설정 <-- setAction
    //--------------------------
    private void start(float tx, float ty) {
        target.set(tx, ty);

        // 이동 방향
        dir.set( MathF.direction(pos, target) );
        ang = MathF.cwDegree(pos, target);

        canStart = true;
    }

    //--------------------------
    // Fire <-- setAction
    //--------------------------
    private void fire() {
        CommonResources.sndPlay();
         synchronized (GameView.mLaser) {
             GameView.mLaser.add(new Laser(scrW, scrH, pos, dir, ang));
         }
    }

} // Alien

