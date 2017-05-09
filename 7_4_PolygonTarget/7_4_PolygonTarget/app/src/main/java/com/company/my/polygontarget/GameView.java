package com.company.my.polygontarget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class GameView extends View {
    // 배경과 과녁의 비트맵
    Bitmap imgBack;
    Bitmap target;

    int w, h;           // 화면의 크기
    int cx, cy;         // 화면의 중심점
    int tw, th;         // 과녁의 크기

    int score = 0;      // 현재 점수
    int total = 0;      // 누적 점수

    float[] radius = new float[3];    // 원의 반지름
    Point[][] pt = new Point[3][7];   // 꼭짓점

    Paint paint = new Paint();

    // 총알 보존용 ArrayList
    ArrayList<BulletHole> mHole = new ArrayList<BulletHole>();

    // 생성자
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // View의 해상도 구하기
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;      // 화면의 폭과 높이
        this.h = h;

        cx = w / 2;      // 화면의 중앙
        cy = h / 2;

        getBitmap();     // 비트맴 반들기
        getArea();       // 터치 영역 만들기
    }

    // 비트맵 만들기
    private void getBitmap() {
        target = BitmapFactory.decodeResource(getResources(), R.drawable.target);
        tw = target.getWidth() / 2;
        th = target.getHeight() / 2;

        // 배경 이미지 확대
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true);
    }

    // 터치 판정 영역
    private void getArea() {
        // 화면 확대 비율
        float r = tw / 140f;
        int[] org = { 140, 90, 40 };

        for (int i = 0; i < 3; i++) {
            radius[i] = org[i] * r;

            // 꼭짓점의 좌표 계산
            for (int j = 0; j < 6; j++) {
                double rad = Math.toRadians(60 * j);
                double x = cx + Math.cos(rad) * radius[i];
                double y = cy - Math.sin(rad) * radius[i];

                pt[i][j] = new Point( (int)x, (int)y );
            }

            // 마지막 점은 시작점
            pt[i][6] = new Point( pt[i][0] );
        }
    }

    // 화면 그리기
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(imgBack, 0, 0, null);
        canvas.drawBitmap(target, cx - tw, cy - th, null);

        // 화살
        for (BulletHole tmp : mHole) {
            canvas.drawBitmap(tmp.hole, tmp.x - tmp.w, tmp.y - tmp.h, null);
        }

        // 점수
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.LEFT);

        String str1 = "득점 : " + score;
        String str2 = "총점 : " + total;
        canvas.drawText(str1, 100, 100, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(str2, w - 100, 100, paint);
    }

    // 점수 판정
    private int CheckScore(int x, int y) {
        // 각 섹터의 점수
        int[] n = { 10, 4, 10, 6, 4, 8, 10 };

        // 각도 계산
        double deg = -Math.toDegrees( Math.atan2(y - cy, x - cx) );
        if (deg < 0) deg += 360;

        // 몇 번째 섹터인가?
        int idx = (int) (deg / 60);

        score = 0;
        for (int i = 2; i >= 0; i-- ) {
            int count = 0;

            // (x,y)에서 그은 수평선이 각 변과 교차되는자 조사
            for (int j = 0; j < 6; j++) {
                if ( hitTest(x, y, pt[i][j], pt[i][j + 1]) ) {
                    count++;
                }
            } // for j

            // 교차된 선분의 합이 홀수이면 득점
            if (count % 2 == 1) {
                score = n[idx] * (i + 1);
                total += score;
                break;
            }
        } // for i

        return score;
    }

    // 선분의 교차 여부 판정
    private boolean hitTest (int x, int y, Point p1, Point p2) {
        boolean hit = false;

        // 점이 선분과 교차하는가? (화면 기준 CCW or CW)
        if ( (y > p1.y && y <= p2.y) || (y < p1.y && y >= p2.y) ) {
            // 기울기
            float m = (float) ((p2.y - p1.y)) / (p2.x - p1.x);

            // 교차점의 x좌표
            float px = p1.x + (y - p1.y) / m;

            // x가 교점의 왼쪽에 있는가?
            if (x < px) {
                hit = true;
            }
        }

        return hit;
    }

    // Touch Event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            if ( CheckScore(x, y) > 0 ) {
                mHole.add( new BulletHole(x, y) );
            }

            invalidate();
        }
        return true;
    }

    // 게임 초기화
    public void initGame() {
        mHole.clear();
        score = total = 0;
        invalidate();
    }

    // 총알 구명
    class BulletHole {
        public int x, y;      // 위치
        public int w, h;      // 크기
        public Bitmap hole;

        // 생성자
        public BulletHole(int x, int y) {
            this.x = x;
            this.y = y;
            hole = BitmapFactory.decodeResource(getResources(), R.drawable.dart);

            w = 0;
            h = hole.getHeight();
        }
    } // BulletHole

} // GameView
