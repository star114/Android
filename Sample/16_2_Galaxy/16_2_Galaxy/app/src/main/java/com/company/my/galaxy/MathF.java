package com.company.my.galaxy;

import android.graphics.PointF;

public class MathF {
    // 작업용 Point
    static private PointF pos = new PointF();
    static private PointF dir = new PointF();

    static private float dx, dy, m;

    //-----------------------------
    // distance - 두 점의 거리 (1)
    //-----------------------------
    static public float distance(PointF p, PointF t) {
        return (float) Math.sqrt( (p.x - t.x) * (p.x - t.x) + (p.y - t.y) * (p.y - t.y) );
    }

    //-----------------------------
    // distance - 두 점의 거리 (2)
    //-----------------------------
    static public float distance(float x, float y, float px, float py) {
        return (float) Math.sqrt( (x - px) * (x - px) + (y - py) * (y - py) );
    }

    //-----------------------------
    // Hit Test(1) - 터치가 원의 내부인가?
    //-----------------------------
    static public boolean hitTest(PointF p, float r, PointF t) {
        return (p.x - t.x) * (p.x - t.x) + (p.y - t.y) * (p.y - t.y) < r * r;
    }

    //-----------------------------
    // Hit Test(2) - 터치가 원의 내부인가?
    //-----------------------------
    static public boolean hitTest(float x, float y, float r, float tx, float ty ) {
        return (x - tx) * (x - tx) + (y - ty) * (y - ty) < r * r;
    }

    //-----------------------------
    // lerp - 선형 보간 (1)
    //-----------------------------
    static public PointF lerp(PointF p1, PointF p2, float rate) {
        // 목적지 근처인가?
        if (distance(p1, p2) < 1f) return p2;

        // 수평, 수직 거리
        dx = p2.x - p1.x;
        dy = p2.y - p1.y;

        // 수직 방향인가?
        if (dx == 0) {
            pos.x = p1.x;
            pos.y = p1.y + dy * rate;
        } else {
            m = dy / dx;                   // 기울기
            pos.x = p1.x + dx * rate;      // 수평거리*비율
            pos.y = p1.y + dx * m * rate;  // 수평거리*기울기*비율
        }

        return pos;
    }

    //-----------------------------
    // lerp - 선형 보간 (2)
    //-----------------------------
    static public float lerp(float start, float end, float rate) {
        return start + (end - start) * rate;
    }

    //-----------------------------
    // 충돌판정(1) 사각형:사각형
    //-----------------------------
    static public boolean checkCollision(float x, float y, float w, float h, float tx, float ty, float tw, float th) {
        return (w + tw) <= Math.abs(x - tx) && (h + th) <= Math.abs(y - ty);
    }

    //-----------------------------
    // 충돌판정(2) 원:원
    //-----------------------------
    static public boolean checkCollision(float x, float y, float r, float tx, float ty, float tr) {
        return (x - tx) * (x - tx) + (y - ty) * (y - ty) <= (r + tr) * (r + tr);
        // return distance(x, y, tx, ty) < (r + tr);}
    }

    //-----------------------------
    // 충돌판정(3) 원:사각형
    //-----------------------------
    static public boolean checkCollision(float x, float y, float r,  float tx, float ty, float tw, float th) {
        return Math.abs(x - tx) <= (tw + r) && Math.abs(y - ty) <= (th + r);
    }

    //-----------------------------
    // Repeat - 구간 반복
    //-----------------------------
    static public int repeat(int n, int end) {
        if (++n >= end) n = 0;
        return n;
    }

    //-----------------------------
    // cwDegree - Canvas 회전각
    //-----------------------------
    static public float cwDegree(PointF p1, PointF p2) {
        double rad = -Math.atan2(p2.y - p1.y, p2.x - p1.x);
        return 90 - (float) Math.toDegrees(rad);
    }

    //-----------------------------
    // Clamp - min ~ max로 제한
    //-----------------------------
    static public float clamp(float org, float min, float max) {
        return Math.max( min, Math.min(org, max) );
    }

    //-----------------------------
    // ditection - 이동 방향
    //-----------------------------
    static public PointF direction(PointF p, PointF t) {
        m = (float) -Math.atan2(t.y - p.y, t.x - p.x);

        pos.x = (float) Math.cos(m);
        pos.y = -(float) Math.sin(m);

        return pos;
    }

    //-----------------------------
    // distance - 두 점의 거리 (3)
    //-----------------------------
    static public float distSqr(float x, float y, float px, float py) {
        return (x - px) * (x - px) + (y - py) * (y - py);
    }

} // MathF

