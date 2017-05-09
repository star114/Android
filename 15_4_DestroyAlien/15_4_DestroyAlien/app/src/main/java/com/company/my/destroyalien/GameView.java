package com.company.my.destroyalien;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

//-----------------------------
// GameView
//-----------------------------
public class GameView extends View {
    // Context, Thread
    private Context context;
    private GameThread mThread;

    // Random, CommonResources
    private Random rnd = new Random();
    private CommonResources mRes;

    // 배경과 화면 크기
    private Bitmap imgBack;
    private int w, h;

    // X-Wing
    static public Xwing xwing;

    //-----------------------------
    // 생성자
    //-----------------------------
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        // Resource 읽기
        mRes.set(context);
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

        // xwing 만들기
        xwing = new Xwing(w, h);

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

        // Laser
        synchronized (mRes.mLaser) {
            for (Laser tmp : mRes.mLaser) {
                canvas.drawBitmap(tmp.img, tmp.x - tmp.w, tmp.y - tmp.h, null);
            }
        }

        // Torpedo
        synchronized (mRes.mTorpedo) {
            for (Torpedo tmp : mRes.mTorpedo) {
                canvas.rotate(tmp.ang, tmp.x, tmp.y);
                canvas.drawBitmap(tmp.img, tmp.x - tmp.w, tmp.y - tmp.h, null);
                canvas.rotate(-tmp.ang, tmp.x, tmp.y);
            }
        }

        // Alien
        synchronized (mRes.mAlien) {
            for (Alien tmp : mRes.mAlien) {
                canvas.drawBitmap(tmp.img, tmp.x - tmp.w, tmp.y - tmp.h, null);
            }
        }

        // X-Wing
        canvas.drawBitmap(xwing.img, xwing.x - xwing.w, xwing.y - xwing.h, null);

        // 폭파 불꽃
        synchronized (mRes.mExp) {
            for (Explosion tmp : mRes.mExp) {
                canvas.drawBitmap(tmp.img, tmp.x - tmp.w, tmp.y - tmp.h, null);
            }
        }
    }

    //-----------------------------
    // Alien 만들기
    //-----------------------------
    private void makeAlien() {
        if (mRes.mAlien.size() < 6 && rnd.nextInt(1000) < 1) {
            mRes.addAlien(w, h);
        }
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        xwing.update();
        mRes.updateObjects();
    }

    //-----------------------------
    // 제거
    //-----------------------------
    private void removeDead() {
        mRes.removeDead();
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            xwing.setAction( event.getX(), event.getY() );
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

                    makeAlien();
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
