package com.company.my.circletarget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
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
    int tw;             // 과녁의 크기

    int score = 0;      // 현재 점수
    int total = 0;      // 누적 점수

    float[] radius = new float[3];  // 원의 반지름
    Paint paint = new Paint();

    // 총알 보전용 ArrayList
    ArrayList<BulletHole> mHole = new ArrayList<BulletHole>();

    // 생성자
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // View의 해상도 구하기
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;           // 화면의 폭과 높이
        this.h = h;
        cx = this.w / 2;      // 화면의 중앙
        cy = this.h / 2;

        getBitmap();     // 비트맴 반들기
        getArea();       // 터치 영역 만들기
    }

    // 비트맵 만들기
    private void getBitmap() {
        target = BitmapFactory.decodeResource(getResources(), R.drawable.target);
        tw = target.getWidth() / 2;

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
        }
    }

    // 화면 그리기
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(imgBack, 0, 0, null);
        canvas.drawBitmap(target, cx - tw, cy - tw, null);

        // 총알 구멍
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

    // 점수 판정
    private int CheckScore(int x, int y) {
        // 각 원의 점수
        int[] n = { 6, 8, 10 };

        score = 0;
        for (int i = 2; i >= 0; i-- ) {
            if ( Math.pow(cx - x, 2) + Math.pow(cy - y, 2) < radius[i] * radius[i] ) {
                score = n[i];
                total += score;
                break;
            }
        }

        return score;
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
        public int w, h;        // 크기
        public Bitmap hole;

        // 생성자
        public BulletHole(int x, int y) {
            this.x = x;
            this.y = y;
            hole = BitmapFactory.decodeResource(getResources(), R.drawable.hole);

            w = hole.getWidth() / 2;
            h = hole.getHeight() / 2;
        }
    } // BulletHole

} // GameView
