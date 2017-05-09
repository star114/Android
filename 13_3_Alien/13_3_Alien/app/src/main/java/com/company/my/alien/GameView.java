package com.company.my.alien;

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

    // Alien, Laser
    private Alien alien;
    static public List<Laser> mLaser;

    //-----------------------------
    // 생성자
    //-----------------------------
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Context, 비트맵 이미지 처리
        this.context = context;
        CommonResources.set(context);
    }

    //-----------------------------
    // View의 크기 구하기
    //-----------------------------
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;  // 화면의 폭과 높이
        this.h = h;

        // 배경 이미지
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true);

        // Alien, Laser
        alien = new Alien(w, h);
        mLaser = Collections.synchronizedList( new ArrayList<Laser>() );

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
    // 화면 그리기 - 동기화
    //-----------------------------
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.drawBitmap(imgBack, 0, 0, null);

        // Alien
        canvas.rotate(alien.ang, alien.pos.x, alien.pos.y);
        canvas.drawBitmap(alien.img, alien.pos.x - alien.w, alien.pos.y - alien.h, null);
        canvas.rotate(-alien.ang, alien.pos.x, alien.pos.y);

        // Laser
        synchronized (mLaser) {
            for (Laser tmp : mLaser) {
                canvas.rotate(tmp.ang, tmp.pos.x, tmp.pos.y);
                canvas.drawBitmap(tmp.img, tmp.pos.x - tmp.w, tmp.pos.y - tmp.h, null);
                canvas.rotate(-tmp.ang, tmp.pos.x, tmp.pos.y);
            }
        }
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        alien.update();

        // 레이저
        synchronized (mLaser) {
            for (Laser tmp : mLaser) {
                tmp.update();
            }
        }
    }

    //-----------------------------
    // 화면을 벗어난 레이저 삭제 - 동기화
    //-----------------------------
    private void removeDead() {
        synchronized (mLaser) {
            for (int i = mLaser.size() - 1; i >= 0; i--) {
                if (mLaser.get(i).isDead) {
                    mLaser.remove(i);
                }
            }
        }
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            alien.setAction( event.getX(),  event.getY() );
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
