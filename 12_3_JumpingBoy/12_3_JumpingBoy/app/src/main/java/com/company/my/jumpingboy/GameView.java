package com.company.my.jumpingboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    // 배경, 화면 크기
    private Bitmap imgBack;
    private int w, h;

    // 소년
    private Boy myBoy;

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
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.field);
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true);

        // 소년
        myBoy = new Boy(context, w, h);

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

        // 그림자
        canvas.save();
        canvas.scale(myBoy.shadowScale, myBoy.shadowScale, myBoy.x, myBoy.ground);

        canvas.drawBitmap(myBoy.shadow, myBoy.x - myBoy.sw, myBoy.ground - myBoy.sh, null);
        canvas.restore();

        // 소년
        canvas.drawBitmap(myBoy.boy, myBoy.x - myBoy.w, myBoy.y - myBoy.h, null);
    }

    //-----------------------------
    // 소년 이동
    //-----------------------------
    private void moveBoy() {
        myBoy.update();
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            myBoy.jump();
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

                    moveBoy();
                    postInvalidate();   // 화면 그리기
                    sleep(10);
                } catch (Exception e) {
                    //
                }
            }
        }
    } // Thread

} // GameView
