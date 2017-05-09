package com.company.my.floatingbubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

//-----------------------------
// GameView
//-----------------------------
public class GameView extends View {
    private Context context;    // Context 저장용

    // 배경과 화면 크기
    private Bitmap imgBack;
    private int w, h;

    // Random. 비눗방울 저장용
    private Random rnd = new Random();
    private ArrayList<Bubble> mBubble = new ArrayList<Bubble>();

    static public ArrayList<SmallBubble> mSmall = new ArrayList<SmallBubble>();
    private Paint paint = new Paint();

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

        // 핸들러 기동
        mHandler.sendEmptyMessageDelayed(0, 10);
    }

    //-----------------------------
    // 화면 그리기
    //-----------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(imgBack, 0, 0, null);

        // 비눗방울 그리기
        for (Bubble tmp : mBubble) {
            canvas.drawBitmap(tmp.bubble, tmp.x - tmp.r, tmp.y - tmp.r, null);
        }

        // 비눗방울 파편
        for (SmallBubble tmp : mSmall) {
            paint.setAlpha(tmp.alpha);
            canvas.drawBitmap(tmp.bubble, tmp.x - tmp.r, tmp.y - tmp.r, paint);
        }
    }

    //-----------------------------
    // 비눗방울 만들기 <-- Handler
    //-----------------------------
    private void makeBubble() {
        if (mBubble.size() < 20 && rnd.nextInt(1000) < 8) {
            mBubble.add( new Bubble(context, w, h) );
        }
    }

    //-----------------------------
    // 비눗방울 이동 <-- Handler
    //-----------------------------
    private void moveBubble() {
        for (Bubble tmp : mBubble) {
            tmp.update();
        }

        // 비눗방울 파편
        for (SmallBubble tmp : mSmall) {
            tmp.update();
        }
    }

    //-----------------------------
    // 수명이 끝난 파편 제거 <-- Handler
    //-----------------------------
    private void removeDead() {
        for (int i = mBubble.size() - 1; i >= 0; i--) {
            if (mBubble.get(i).isDead) {
                mBubble.remove(i);
            }
        }

        for (int i = mSmall.size() - 1; i >= 0; i--) {
            if (mSmall.get(i).isDead) {
                mSmall.remove(i);
            }
        }
    }

    //-----------------------------
    // Hit Test <-- Touch Event
    //-----------------------------
    private void hitTest(float x, float y) {
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
            hitTest( event.getX(), event.getY() );
        }

        return true;
    }

    //-----------------------------
    // Handler
    //-----------------------------
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Time.update();   // deltaTime 계산

            makeBubble();
            moveBubble();
            removeDead();
            invalidate();
            mHandler.sendEmptyMessageDelayed(0, 10);
        }
    };

} // GameView
