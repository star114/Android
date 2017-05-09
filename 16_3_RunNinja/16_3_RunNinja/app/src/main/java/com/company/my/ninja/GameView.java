package com.company.my.ninja;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

//-----------------------------
// GameView
//-----------------------------
public class GameView extends View {
    // Context, Thread
    private Context context;
    private GameThread mThread;

    // 화면 크기
    private int w, h;

    // 닌자, Fields
    static public Ninja ninja;
    private Fields fields;

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

        // 닌자, Fields
        ninja = new Ninja(context, w, h);
        fields = new Fields(context, w, h);

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
        fields.draw(canvas);
        // Ninja
        canvas.scale(ninja.dir.x, 1, ninja.x, ninja.y);
        canvas.drawBitmap(ninja.img, ninja.x - ninja.w, ninja.y - ninja.h, null);
        canvas.scale(-ninja.dir.x, 1, ninja.x, ninja.y);
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        ninja.update();
        fields.update();
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ninja.setAction( event.getX(), event.getY() );
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
                    // nothing
                }
            }
        }
    } // Thread

} // GameView
