package com.company.my.butterfly;

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
import java.util.Random;

//-----------------------------
// GameView
//-----------------------------
public class GameView extends View {
    // Context, Thread
    private Context context;
    private GameThread mThread;

    // 배경, 화면 크기
    private Bitmap imgBack;
    private int w, h;

    // 꽃다발, 나비
    Flower flower;
    ArrayList<Butterfly> mFly = new ArrayList<Butterfly>();

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

        // 화면의 폭과 높이
        this.w = w;
        this.h = h;

        // 배경 이미지
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.field);
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true);

        // 꽃다발
        flower = new Flower(context, w, h);

        // 나비 만들기
        mFly.clear();
        int cnt = new Random().nextInt(6) + 15;   // 15~20

        for (int i = 1; i <= cnt; i++) {
            mFly.add( new Butterfly(context, w, h) );
        }

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
        canvas.drawBitmap(flower.img, flower.x - flower.w, flower.y - flower.h, null);

        // 나비
        for (Butterfly tmp : mFly) {
            canvas.rotate(tmp.ang, tmp.x, tmp.y);
            canvas.drawBitmap(tmp.fly, tmp.x - tmp.w, tmp.y - tmp.h, null);
            canvas.rotate(-tmp.ang, tmp.x, tmp.y);
        }
    }

    //-----------------------------
    // 나비 이동
    //-----------------------------
    private void moveButterfly() {
        for (Butterfly tmp : mFly) {
            tmp.update();
        }
    }

    //-----------------------------
    // 목적지 설정 <-- Touch Event
    //-----------------------------
    private void setTarget(float x, float y) {
        // 꽃다발 이동
        if ( flower.move(x, y) ) {
            // 나비의 목적지 설정
            for (Butterfly tmp : mFly) {
                tmp.setTarget(x, y);
            }
        }
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            setTarget( event.getX(), event.getY());
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

                    moveButterfly();
                    postInvalidate();   // 화면 그리기
                    sleep(10);
                } catch (Exception e) {
                    //
                }
            }
        }
    } // Thread

} // GameView
