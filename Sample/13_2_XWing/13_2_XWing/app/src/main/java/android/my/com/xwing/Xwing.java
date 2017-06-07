package android.my.com.xwing;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Xwing {
    // 화면 크기
    private int scrW, scrH;

    // 목적지, 목적지가 있는가?
    private PointF target = new PointF();
    private boolean isTarget;

    // 위치, 크기
    public PointF pos = new PointF();
    public int w, h;

    // 비트맵
    public Bitmap img;

    //--------------------------
    // 생성자
    //--------------------------
    public Xwing(int width, int height) {
        scrW = width;      // 화면 크기
        scrH = height;

        img = CommonResources.imgXwing;
        w = CommonResources.xw;
        h = CommonResources.xh;

        // 초기 위치 - 화면 아래 가운데
        pos.x = scrW / 2;
        pos.y = scrH - h - 40;
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        if (isTarget) {
            pos.set( MathF.lerp(pos, target, 3f * Time.deltaTime) );

            if (MathF.distance(pos, target) < 1) {
                isTarget = false;
            }
        }
    }

    //--------------------------
    // set Action <-- Touch Event
    //--------------------------
    public void setAction(float tx, float ty) {
        // 터치 위치가 X-Wing의 내부인가?
        if ( MathF.hitTest(pos.x, pos.y, w * 0.9f, tx, ty) ) {
            fire();
        } else {
            setTarget(tx, ty);
        }
    }

    //--------------------------
    // set Target – 목적지 설정
    //--------------------------
    public void setTarget(float tx, float ty) {
        target.set(tx, pos.y);
        isTarget = true;
    }

    //--------------------------
    // Fire - 동기화
    //--------------------------
    public void fire() {
        isTarget = false;

        // 양쪽 날개 끝에 레이저 추가
        CommonResources.sndPlay();
        synchronized (GameView.mLaser) {
            GameView.mLaser.add(new Laser(scrW, scrH, pos.x - w, pos.y - h / 2));
            GameView.mLaser.add(new Laser(scrW, scrH, pos.x + w, pos.y - h / 2));
        }
    }

} // Xwing
