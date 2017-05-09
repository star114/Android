package com.company.my.analogclock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import android.os.Handler;
import android.os.Message;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class GameView extends View {
    // 화면 중심점
    int cx, cy;

    // 문자판과 시계 바늘을 표시할 비트맵
    Bitmap clock;
    Bitmap[] pin = new Bitmap[3];

    // 시, 분, 초, 밀리초
    int hour, min, sec, msec, amPm;

    // 시계 바늘의 회전각도
    float rHour, rMin, rSec;

    // 시계와 바늘의 크기
    int cw, pw, ph;

    // 생성자
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 비트맵 읽기
        clock = BitmapFactory.decodeResource(getResources(), R.drawable.clock);
        for (int i = 0; i < 3; i++) {
            pin[i] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_1 + i);
        }

        // 시계와 바늘의 크기
        cw = clock.getWidth() / 2;
        pw = pin[0].getWidth() / 2;
        ph = pin[0].getHeight() - 60;

        // Handler 호출
        mHandler.sendEmptyMessageDelayed(0, 100);
    }

    // 화면 중심점 구하기
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cx = w / 2;
        cy = h / 2;
    }

    // 화면 그리기
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.scale(0.9f, 0.9f, cx, cy);

//        canvas.scale(-1f, 1, cx, cy);
//        canvas.scale(1, -1, cx, cy);
//        canvas.rotate(90, cx, cy);
//        canvas.skew(-0.36f, 0);

        canvas.save();

        // 문자판
        canvas.drawBitmap(clock, cx - cw, cy - cw, null);

        // 시침
        canvas.rotate(rHour, cx, cy);
        canvas.drawBitmap(pin[2], cx - pw, cy - ph, null);

        // 분침
        canvas.rotate(rMin - rHour, cx, cy);
        canvas.drawBitmap(pin[1], cx - pw, cy - ph, null);

        // 초침
        canvas.rotate(rSec - rMin, cx, cy);
        canvas.drawBitmap(pin[0], cx - pw, cy - ph, null);
        canvas.restore();

        // 디지털 시간 표시
        Paint paint = new Paint();
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.CENTER);

        // 오후 0시인가?
        if (amPm == 1 && hour == 0) {
            hour += 12;
        }

        String str = String.format("%d : %d : %d.%d" , hour, min, sec, msec);
        canvas.drawText(str, cx, cy + cw + 100, paint);
    }

    // 현재 시각 읽기
    private void GetTime() {
        // 시간 읽기용 calendar
        GregorianCalendar calendar = new GregorianCalendar( TimeZone.getTimeZone("GMT+9") );

        hour = calendar.get(Calendar.HOUR);
        min = calendar.get(Calendar.MINUTE);
        sec = calendar.get(Calendar.SECOND);
        msec = calendar.get(Calendar.MILLISECOND) / 100;
        amPm = calendar.get(Calendar.AM_PM);

        // 시계 바늘 각도 계산
        rSec = sec * 6;
        rMin = min * 6 + rSec / 60;
        rHour = hour * 30 + rMin / 12;
    }

    // Handler
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            GetTime();
            invalidate();
            mHandler.sendEmptyMessageDelayed(0, 100);
        }
    };

} // GameView
