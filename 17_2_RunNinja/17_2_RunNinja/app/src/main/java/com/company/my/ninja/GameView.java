package com.company.my.ninja;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
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

    // 화면 크기
    private int w, h;

    // 닌자, Fields
    static public Ninja ninja;
    private Sky sky;
    private Fields fields;

    // 버튼
    private Button btnLeft;
    private Button btnRight;
    private Button btnJump;

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

        // 닌자, Fields
        ninja = new Ninja(context, w, h);
        sky = new Sky(context, w, h);
        fields = new Fields(context, w, h);

        // 버튼 만들기
        makeButton();

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
        sky.draw(canvas);
        fields.draw(canvas);

        // Ninja
        canvas.save();
        if (ninja.isLeft) {
            canvas.scale(-1, 1, ninja.x, ninja.y);
        }
        canvas.drawBitmap(ninja.img, ninja.x - ninja.w, ninja.y - ninja.h, null);
        canvas.restore();

        // 버튼
        canvas.drawBitmap(btnLeft.img, btnLeft.x, btnLeft.y, null);
        canvas.drawBitmap(btnRight.img, btnRight.x, btnRight.y, null);
        canvas.drawBitmap(btnJump.img, btnJump.x, btnJump.y, null);
    }

    //-----------------------------
    // 버튼 만들기
    //-----------------------------
    private void makeButton() {
        // 버튼 이미지
        Bitmap imgLeft = BitmapFactory.decodeResource(getResources(), R.drawable.button_left);
        Bitmap imgRight = BitmapFactory.decodeResource(getResources(), R.drawable.button_right);
        Bitmap imgJump = BitmapFactory.decodeResource(getResources(), R.drawable.button_jump);

        // 버튼의 크기
        int bw = imgLeft.getWidth();
        int bh = imgLeft.getHeight();

        // 버튼의 위치
        int y = h - bh - 10;                      // 화면 아래
        Point lPos = new Point(10, y);            // 화면 왼쪽 아래
        Point rPos = new Point(bw + 20, y);       // lPos의 오른쪽
        Point jPos = new Point(w - bw - 10, y);   // 화면 오른쪽 아래

        // 버튼 만들기
        btnLeft = new Button(imgLeft, lPos);
        btnRight = new Button(imgRight, rPos);
        btnJump = new Button(imgJump, jPos);
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        ninja.update();
        sky.update();
        fields.update();
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isTouch = false;

        int action = MotionEventCompat.getActionMasked(event);
        // int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                isTouch = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                isTouch = false;
                break;
            default:
                return true;
        }

        // 터치 index, id
        int ptrIdx = MotionEventCompat.getActionIndex(event);
        int id = MotionEventCompat.getPointerId(event, ptrIdx);

        // 터치 좌표
        float x = MotionEventCompat.getX(event, ptrIdx);
        float y = MotionEventCompat.getY(event, ptrIdx);;

        // 각각의 버튼에 통지
        btnLeft.action(id, isTouch, x, y);
        btnRight.action(id, isTouch, x, y);
        btnJump.action(id, !isTouch, x, y);

        // 닌자의 액션 설정
        ninja.setAction( btnLeft.isTouch, btnRight.isTouch, btnJump.isTouch );
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
                    // nothing
                }
            }
        }
    } // Thread

} // GameView
