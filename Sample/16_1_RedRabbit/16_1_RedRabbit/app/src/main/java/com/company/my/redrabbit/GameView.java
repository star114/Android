package com.company.my.redrabbit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
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

    // 배경 하늘
    private RectF sky = new RectF();
    private Paint paint = new Paint();

    // 구름, 토끼
    private Cloud cloud;
    private Rabbit rabbit;

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

        // 배경
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.field);
        imgBack = Bitmap.createScaledBitmap(imgBack, w, (int) (h * 0.7f), true);

        // 하늘
        LinearGradient grad = new LinearGradient(0, 0, 0, h * 0.7f, 0xFFa8ddff, 0x40a8ddff, Shader.TileMode.CLAMP);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(grad);
        sky.set(0, 0, w, h * 0.6f);

        // 구름, 토끼
        cloud = new Cloud(context, w, h);
        rabbit = new Rabbit(context, w, h);

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
        // 하늘
        canvas.drawRect(sky, paint);

        // 구름
        canvas.drawBitmap(cloud.img1, cloud.x1 - cloud.w1, cloud.y1 - cloud.h1, null);
        canvas.drawBitmap(cloud.img2, cloud.x2 - cloud.w2, cloud.y2 - cloud.h2, null);

        // 초원
        canvas.drawBitmap(imgBack, 0, h * 0.3f, null);
        // 그림자, 토끼
        canvas.drawBitmap(rabbit.shadow, rabbit.x - rabbit.sw, rabbit.ground - rabbit.sh, null);

        canvas.scale(rabbit.dir, 1, rabbit.x, rabbit.y);
        canvas.drawBitmap(rabbit.img, rabbit.x - rabbit.w, rabbit.y - rabbit.h, null);
        canvas.scale(-rabbit.dir, 1, rabbit.x, rabbit.y);
    }

    //-----------------------------
    // 이동 <-- Handler
    //-----------------------------
    private void moveObject() {
        cloud.update();
        rabbit.update();
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            rabbit.setAction( event.getX() );
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
