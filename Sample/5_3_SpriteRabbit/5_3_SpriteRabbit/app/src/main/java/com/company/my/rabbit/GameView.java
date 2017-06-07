package com.company.my.rabbit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
    // 토끼 이미지와 리소스
    Bitmap[] rabbit = new Bitmap[2];
    int[] imgRes = { R.drawable.rabbit_1, R.drawable.rabbit_2 };

    int w, h;           // 화면의 크기
    int rw, rh;         // 토끼 이미지의 크기

    int x = 300;       // 토끼의 초기 좌표
    int y = 200;

    int sx = 3;         // 토끼의 이동 속도
    int sy = 2;

    int counter = 0;    // Loop Counter
    int imgNum = 0;     // 토끼 이미지 번호

    float x1, y1;       // 터치 시작 위치

    public boolean isRun = true;

    // 생성자
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        rabbit[0] = BitmapFactory.decodeResource( context.getResources(), imgRes[0] );
        rabbit[1] = BitmapFactory.decodeResource( context.getResources(), imgRes[1] );

        rw = rabbit[0].getWidth() / 2;
        rh = rabbit[0].getHeight() / 2;

        mHandler.sendEmptyMessageDelayed(0, 10);
    }

    // 화면의 크기 구하기
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;
        this.h = h;

        x = w / 2;     // 토끼의 좌표를 화면 중앙으로 설정
        y = h / 2;
    }
    // 그리기
    @Override
    protected void onDraw(Canvas canvas) {
        x += sx;                       // 토끼 이동
        if (x < rw || x > w - rw) {   // 화면의 좌우를 벗어났나?
            sx = -sx;                   // 이동 방향을 반대로 설정
            x += sx;                   // 토끼 원위치
        }

        y += sy;                       // 토끼 이동
        if (y < rh || y > h - rh) {   // 화면의 상하를 벗어났나?
            sy = -sy;                   // 이동 방향을 반대로 설정
            y += sy;                   // 토끼 원위치
        }

        counter++;                  // 카운터 증가
        if (counter % 50 == 0) {
            imgNum = 1 - imgNum;    // 0과 1을 교대로 반복한다.
        }

        canvas.drawBitmap( rabbit[imgNum], x - rw, y - rh, null );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
            // x = (int) event.getX();
            // y = (int) event.getY();
            x1 = event.getX();
            y1 = event.getY();
        }

        if ( event.getAction() == MotionEvent.ACTION_UP ) {
            float x2 = event.getX();
            float y2 = event.getY();

            sx = (int)(x2 - x1) / 100;
            sy = (int)(y2 - y1) / 100;
        }

        return true;
    }

    // Handler
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (isRun) {
                invalidate();
            }
            mHandler.sendEmptyMessageDelayed(0, 10);
        }
    };


} // GameView

