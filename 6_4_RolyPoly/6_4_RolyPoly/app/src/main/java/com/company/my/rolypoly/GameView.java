package com.company.my.rolypoly;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class GameView extends View {
    // 비트맵
    Bitmap imgBack;     // 배경
    Bitmap toy;         // 오뚝이
    Bitmap shadow;      // 그림자

    // 화면 폭과 높이
    int w, h;           // 화면의 크기
    int cx, cy;         // 오뚝이의 회전축

    // 오뚝이와 그림자 크기
    int tw, th;
    int sw, sh;

    // 회전 각도, 이동 방향, 좌우 한계
    int ang = 0;
    int dir = 0;
    int lLimit = -15;
    int rLimit = 15;

    // 생성자
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler.sendEmptyMessageDelayed(0, 10);
    }

    // View의 해상도 구하기
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;                     // 화면의 폭
        this.h = h;                     // 화면의 높이

        cx = w / 2;                     // 가로축의 중앙
        cy = (int) (h * 0.8f);          // 세로 축의 80%

        getBitmap();                    // 비트맵 만들기
    }

    // 화면 그리기
    @Override
    protected void onDraw(Canvas canvas) {
        // 배경과 그림자
        canvas.drawBitmap(imgBack, 0, 0, null);
        canvas.drawBitmap(shadow, cx - sw, cy - sh, null);

        RotateToy();                  // 회전각 구하기
        canvas.rotate(ang, cx, cy);   // canvas 회전
        canvas.drawBitmap(toy, cx - tw, cy - th, null);
        canvas.rotate(-ang, cx, cy);  // canvas 원위치
    }

    // 오뚝이 회전 각도 계산
    private void RotateToy() {
        // 회전 각도 누적
        ang += dir;

        // 회전각이 좌우의 한계에 도달했는가?
        if (ang <= lLimit || ang >= rLimit) {
            lLimit++;     // 왼쪽 한계
            rLimit--;     // 오른쪽 한계
            dir = -dir;   // 회전 방향 반전
            ang += dir;   // 회전각 원위치
        }
    }

    // 비트맵 만들기
    private void getBitmap() {
        toy = BitmapFactory.decodeResource(getResources(), R.drawable.toy);
        shadow = BitmapFactory.decodeResource(getResources(), R.drawable.shadow);
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.back);

        // 배경 이미지 확대
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true);

        // 오뚝이의 크기
        tw = toy.getWidth() / 2;
        th = toy.getHeight();

        // 그림자 크기
        sw = shadow.getWidth() / 2;
        sh = shadow.getHeight() / 2;
    }

    // Touch Event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            lLimit = -15;       // 좌우 한계 설정
            rLimit = 15;

            if (dir == 0) {     // 정지상태이면
                dir = -1;       // 횐쪽으로 회전
            }
        }
        return true;
    }

    // Handler
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            invalidate();
            mHandler.sendEmptyMessageDelayed(0, 10);
        }
    };

} // GameView
