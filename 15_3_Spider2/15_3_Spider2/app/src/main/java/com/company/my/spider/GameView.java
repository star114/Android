package com.company.my.spider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
    // Context, Thread
    private Context context;
    private GameThread mThread;

    // Random, 배경, 화면 크기
    private Random rnd = new Random();
    private Bitmap imgBack;
    private int w, h;

    // 점수, 포획 나비 수, 명중율
    private int score = 0;
    private int killCount = 0;
    private float rate;

    // 점수 표시용
    private Paint paint = new Paint();

    // 나비, 독액, 거미
    static public List<Butterfly> mFly;
    static public List<Poison> mPoison;
    private Spider spider;

    //-----------------------------
    // 생성자
    //-----------------------------
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        // 리소스 설정
        CommonResources.set(context);

        // 글자크기, 글자색, 글자속성(Bold체)
        paint.setTextSize(60);
        paint.setColor(Color.BLACK);
        paint.setFakeBoldText(true);

        // 나비, Poison
        mFly = Collections.synchronizedList( new ArrayList<Butterfly>() );
        mPoison = Collections.synchronizedList( new ArrayList<Poison>() );
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

        spider = new Spider(w, h);

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

        // 나비
        synchronized (mFly) {
            for (Butterfly tmp : mFly) {
                canvas.drawBitmap(tmp.img, tmp.x - tmp.w, tmp.y - tmp.h, null);
            }
        }

        // Poison
        synchronized (mPoison) {
            for (Poison tmp : mPoison) {
                canvas.drawBitmap(tmp.img, tmp.x - tmp.r, tmp.y - tmp.r, null);
            }
        }

        // 거미
        canvas.drawBitmap(spider.img, spider.x - spider.w, spider.y - spider.h, null);

        // 점수
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("득점 : " + score, 50, 80, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("포획 : " + killCount, w / 2, 80, paint);

        rate = killCount / (spider.fireCnt + 0.001f) * 100;
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.format("명중률 : %4.1f%%", rate), w - 50, 80, paint);
    }

    //-----------------------------
    // 나비 만들기
    //-----------------------------
    private void makeButterfly() {
        synchronized (mFly) {
            if (mFly.size() < 8) {
                mFly.add(new Butterfly(w, h));
            }
        }
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        // 나비
        synchronized (mFly) {
            for (Butterfly tmp : mFly) {
                tmp.update();
            }
        }

        // 거미
        spider.update();

        // Poison
        synchronized (mPoison) {
            for (Poison tmp : mPoison) {
                tmp.update();
            }
        }
    }

    //-----------------------------
    // 제거
    //-----------------------------
    private void removeDead() {
        // 득점 추가 및 나비 초기화
        for (int i = mFly.size() - 1; i >= 0; i--) {
            if (mFly.get(i).isDead) {
                killCount++;
                score += mFly.get(i).score;

                // 나비 초기화
                mFly.get(i).init();
            }
        }

        // Poison 제거
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
            spider.setAction( event.getX(), event.getY() );
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            spider.stop();
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

                    makeButterfly();
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
