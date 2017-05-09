package com.company.my.bounceball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//-----------------------------
// GameView
//-----------------------------
public class GameView extends View {
    // Context, Thread
    private Context context;
    private GameThread mThread;

    // 배경과 화면 크기
    private Bitmap imgBack;
    private int w, h;

    // 공 저장용
    private List<Ball> mBall;

    //-----------------------------
    // 생성자
    //-----------------------------
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Context 저장, 공 비트맵 이미지 처리
        this.context = context;
        CommonResources.set(context);

        // 공 저장용 ArrayList
        mBall = Collections.synchronizedList( new ArrayList<Ball>() );
    }

    //-----------------------------
    // View의 크기 구하기
    //-----------------------------
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 화면의 폭과 높이
        this.w = w;
        this.h = h;

        // 배경 이미지
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.field);
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true);

        // 스레드 기동
        if (mThread == null) {
            mThread = new GameThread();
            mThread.start();
        }
    }

    //-----------------------------
    // View의 종료
    //-----------------------------
    @Override
    protected void onDetachedFromWindow() {
        mThread.canRun = false;
        super.onDetachedFromWindow();
    }

    //-----------------------------
    // 화면 그리기
    //-----------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(imgBack, 0, 0, null);

        // 공 그리기
        synchronized (mBall) {
            for (Ball tmp : mBall) {
                // 공 회전
                canvas.rotate(tmp.ang, tmp.x, tmp.y);
                canvas.drawBitmap(tmp.ball, tmp.x - tmp.r, tmp.y - tmp.r, null);
                canvas.rotate(-tmp.ang, tmp.x, tmp.y);
            }
        }
    }


    //-----------------------------
    // 공 만들기 <-- Touch Event
    //-----------------------------
    private synchronized void makeBall(float x, float y) {
        mBall.add( new Ball(w, h, x, y) );
    }

    //-----------------------------
    // 공 이동
    //-----------------------------
    private synchronized void moveBall() {
        for (Ball tmp : mBall) {
            tmp.update();
        }
    }

    //-----------------------------
    // 수명이 끝난 공 제거
    //-----------------------------
    private synchronized void removeDead() {
        for (int i = mBall.size() - 1; i >= 0; i--) {
            if (mBall.get(i).isDead) {
                mBall.remove(i);
            }
        }
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            makeBall( event.getX(), event.getY() );
        }

        return true;
    }

    //-----------------------------
    // Thread
    //-----------------------------
    class GameThread extends Thread {
        public boolean canRun = true;

        @Override
        public void run() {
            while (canRun) {
                try {
                    Time.update();      // deltaTime 계산

                    moveBall();
                    removeDead();
                    postInvalidate();   // 화면 그리기
                    sleep(10);
                } catch (Exception e) {
                    //
                }
            }
        }
    } // Thread

} // GameView
