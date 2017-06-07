package com.company.my.parkalone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameView extends View {
    // Context, Thread, Random
    private Context context;
    private GameThread mThread;
    private Random rnd = new Random();

    // 배경과 화면 크기
    private Bitmap imgBack;
    private int w, h;

    // 공과 소년
    private Ball ball;
    static public Boy boy;

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

        // 배경 이미지
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true);

        ball = new Ball(context, w, h);
        boy = new Boy(context, w, h);

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

        // 소년 그림자
        canvas.save();
        canvas.scale(boy.shwScale, boy.shwScale, boy.x, boy.ground);
        canvas.drawBitmap(boy.shadow, boy.x - boy.sw, boy.ground - boy.sh, null);
        canvas.restore();

        // 소년
        canvas.save();
        canvas.scale(boy.dir.x, 1, boy.x, boy.y);
        canvas.drawBitmap(boy.img, boy.x - boy.w, boy.y - boy.h, null);
        canvas.restore();

        // 공 그림자
        canvas.save();
        canvas.scale(ball.shwScale, ball.shwScale, ball.x, ball.ground);
        canvas.drawBitmap(ball.shadow, ball.x - ball.sw, ball.ground - ball.sh, null);
        canvas.restore();

        // 공
        canvas.rotate(ball.ang, ball.x, ball.y);
        canvas.drawBitmap(ball.img, ball.x - ball.r, ball.y - ball.r, null);
        canvas.rotate(-ball.ang, ball.x, ball.y);
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        boy.update();
        ball.update();
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            boy.setAction( event.getX(), event.getY() );
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
