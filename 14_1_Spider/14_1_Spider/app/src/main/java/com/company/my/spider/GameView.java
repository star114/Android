package com.company.my.spider;

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

    // Spider, Poison
    private Spider spider;
    static public List<Poison> mPoison;

    //-----------------------------
    // 생성자
    //-----------------------------
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        // Context, 리소스 처리
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
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true);

        // 거미, Poison
        spider = new Spider(w, h);
        mPoison = Collections.synchronizedList( new ArrayList<Poison>() );

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

        // Poison
        synchronized (mPoison) {
            for (Poison tmp : mPoison) {
                canvas.drawBitmap(tmp.img, tmp.x - tmp.r, tmp.y - tmp.r, null);
            }
        }

        // 거미
        canvas.drawBitmap(spider.img, spider.x - spider.w, spider.y - spider.h, null);
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        spider.update();

        // Poison
        synchronized (mPoison) {
            for (Poison tmp : mPoison) {
                tmp.update();
            }
        }
    }

    //-----------------------------
    // 화면을 벗어난 Poison 삭제 - 동기화
    //-----------------------------
    private void removeDead() {
        synchronized (mPoison) {
            for (int i = mPoison.size() - 1; i >= 0; i--) {
                if (mPoison.get(i).isDead) {
                    mPoison.remove(i);
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
            spider.startAction( event.getX(), event.getY() );
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            spider.stopAction();
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
