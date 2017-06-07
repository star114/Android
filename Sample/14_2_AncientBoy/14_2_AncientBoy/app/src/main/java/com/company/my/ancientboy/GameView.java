package com.company.my.ancientboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
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

    private Boy boy;

    // Gesture
    private MyGesture mGesture = new MyGesture();

    //-----------------------------
    // 생성자
    //-----------------------------
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        // 이미지 초기화
        CommonResources.set(context);

        // Gesture
        final GestureDetector mDetector = new GestureDetector(context, mGesture);
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mDetector.onTouchEvent(motionEvent);
            }
        });
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

        // 소년
        boy = new Boy(w, h);

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
        canvas.scale(boy.sdScale, boy.sdScale, boy.x, boy.ground);
        canvas.drawBitmap(boy.shadow, boy.x - boy.sw, boy.ground - boy.sh, null);
        canvas.restore();

        // 소년
        canvas.scale(boy.leftRight, 1, boy.x, boy.ground);
        canvas.drawBitmap(boy.img, boy.x - boy.w, boy.y - boy.h, null);
        canvas.scale(-boy.leftRight, 1, boy.x, boy.ground);
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        boy.update();
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

    //-----------------------------
    // Gesture
    //-----------------------------
    public class MyGesture extends GestureDetector.SimpleOnGestureListener {

        @Override   // Scroll - Drag
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // 스크롤 방향 = right : distX < 0, Left : distX > 0

            Log.v("거리 = ", "x = " + distanceX  + ", y = " + distanceY);
            boy.run(distanceX);
            return true;
        }

        @Override   // Double Tab
        public boolean onDoubleTap(MotionEvent e) {
            boy.jump();
            return true;
        }

        @Override  // Action Down
        public boolean onDown(MotionEvent e) {
            boy.stop( e.getX(), e.getY() );
            return true;
        }
    } // MyGesture

} // GameView
