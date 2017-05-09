package com.company.my.floatingbubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private Context context;    // Context 저장용
    public GameThread mThread;

    // 배경과 화면 크기
    private Bitmap imgBack;
    private int w, h;

    // Random.
    private Random rnd = new Random();
    private Paint paint = new Paint();

    // 비눗방울, 파편
    private List<Bubble> mBubble = Collections.synchronizedList( new ArrayList<Bubble>() );
    static public List<SmallBubble> mSmall = Collections.synchronizedList( new ArrayList<SmallBubble>() );

    //-----------------------------
    // 생성자
    //-----------------------------
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Context 저장
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
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
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
    // 화면 그리기 - 동기화
    //-----------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(imgBack, 0, 0, null);

        // 비눗방울 그리기
        synchronized (mBubble) {
            for (Bubble tmp : mBubble) {
                canvas.drawBitmap(tmp.bubble, tmp.x - tmp.r, tmp.y - tmp.r, null);
            }
        }

        // 비눗방울 파편
        synchronized (mSmall) {
            for (SmallBubble tmp : mSmall) {
                paint.setAlpha(tmp.alpha);
                canvas.drawBitmap(tmp.bubble, tmp.x - tmp.r, tmp.y - tmp.r, paint);
            }
        }
    }

    //-----------------------------
    // 비눗방울 만들기 - 동기화
    //-----------------------------
    private void makeBubble() {
        synchronized (mBubble) {
            if (mBubble.size() < 20 && rnd.nextInt(1000) < 8) {
                mBubble.add(new Bubble(context, w, h));
            }
        }
    }

    //-----------------------------
    // 이동 - 동기화
    //-----------------------------
    private void moveBubble() {
        // 비눗방울
        synchronized (mBubble) {
            for (Bubble tmp : mBubble) {
                tmp.update();
            }
        }

        // 비눗방울 파편
        synchronized (mSmall) {
            for (SmallBubble tmp : mSmall) {
                tmp.update();
            }
        }
    }

    //-----------------------------
    // 수명이 끝난 오브젝트 제거 - 동기화
    //-----------------------------
    private void removeDead() {
        // 풍선
        synchronized (mBubble) {
            for (int i = mBubble.size() - 1; i >= 0; i--) {
                if (mBubble.get(i).isDead) {
                    mBubble.remove(i);
                }
            }
        }

        // 파편
        synchronized (mSmall) {
            for (int i = mSmall.size() - 1; i >= 0; i--) {
                if (mSmall.get(i).isDead) {
                    mSmall.remove(i);
                }
            }
        }
    }

    //-----------------------------
    // Hit Test <-- Touch Event
    //-----------------------------
    private synchronized void hitTest(float x, float y) {
        for (Bubble tmp : mBubble) {
            if ( tmp.hitTest(x, y) ) break;
        }
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hitTest(event.getX(), event.getY());
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

                    makeBubble();
                    moveBubble();
                    removeDead();
                    postInvalidate();   // 화면 그리기
                    sleep(10);
                } catch (Exception e) {
                    // Do nothing
                }
            }
        }
    } // Thread

} // GameView
