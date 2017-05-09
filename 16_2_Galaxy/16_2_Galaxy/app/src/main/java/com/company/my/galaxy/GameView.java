package com.company.my.galaxy;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
    // Context, Thread
    private Context context;
    private GameThread mThread;

    // 화면 크기
    private int w, h;

    // 배경, X-Wing
    private Sky sky;
    private Xwing xwing;

    //-----------------------------
    // 생성자
    //-----------------------------
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    //-----------------------------
    // View의 크기 구하기
    //-----------------------------
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;  // 화면의 폭과 높이
        this.h = h;

        // 배경, X-Wing
        sky = new Sky(context, w, h);
        xwing = new Xwing(context, w, h);

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
        sky.draw(canvas);  // 배경
        canvas.drawBitmap(xwing.img, xwing.x - xwing.w, xwing.y - xwing.w, null);
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        sky.update();
        xwing.update();
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            xwing.setAction( event.getX() );
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

                    moveObject();
                    postInvalidate();   // 화면 그리기
                    sleep(10);
                } catch (Exception e) {
                    //
                }
            }
        }
    } // Thread

} // GameView
