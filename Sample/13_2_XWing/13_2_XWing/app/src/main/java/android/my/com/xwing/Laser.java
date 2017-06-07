package android.my.com.xwing;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Laser {
    // 화면 크기
    private int scrW, scrH;

    // 속도, 방향
    private  int speed = 1200;
    private PointF dir = new PointF();

    // 현재 위치, 크기
    public float x, y;
    public int w, h;

    // 비트맵, 소멸?
    public Bitmap img;
    public boolean isDead;

    //--------------------------
    // 생성자
    //--------------------------
    public Laser(int width, int height, float px, float py) {
        scrW = width;   // 화면 크기
        scrH = height;

        x = px;         // 초기 위치
        y = py;

        // 비트맵
        img = CommonResources.imgLaser;
        w = CommonResources.lw;
        h = CommonResources.lh;

        // 이동 방향
        dir.x = 0;
        dir.y = speed;
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        y -= dir.y * Time.deltaTime;

        // 화면을 벗어나면 소거
        if ( y < -h) {
            isDead = true;
        }
    }

} // Laser
