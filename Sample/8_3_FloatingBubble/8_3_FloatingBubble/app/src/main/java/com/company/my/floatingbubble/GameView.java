package com.company.my.floatingbubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

//-----------------------------
// GameView
//-----------------------------
public class GameView extends View {
    private Context context;    // Context 저장용

    // 배경과 화면 크기
    private Bitmap imgBack;
    private int w, h;

    // 비눗방울 저장용
    private ArrayList<Bubble> mBubble = new ArrayList<Bubble>();

    //-----------------------------
    // 생성자
    //-----------------------------
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Context 저장, 핸들러 기동
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
            canvas.drawBitmap(tmp.bubble, tmp.x - tmp.bw, tmp.y - tmp.bw, null);
        }
    }

    //-----------------------------
    // 비눗방울 이동
    //-----------------------------
    private void moveBubble() {
        for (Bubble tmp : mBubble) {
            tmp.update();
        }
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            mBubble.add( new Bubble(context, w, h, x, y) );
        }

        return true;
    }

    //-----------------------------
    // Handler
    //-----------------------------
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            moveBubble();
            invalidate();
            mHandler.sendEmptyMessageDelayed(0, 10);
        }
    };

} // GameView
