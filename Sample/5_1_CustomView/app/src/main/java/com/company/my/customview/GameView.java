package com.company.my.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {

    int w, h;      // 화면의 폭과 높이(픽셀)

    // 성성자(1)
    public GameView(Context context) {
        super(context);
    }

    // 성성자(2)
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;
        this.h = h;
    }

    // 화면 그리기
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();                 // 화면 출력용 Paint
        paint.setColor(Color.BLACK);               // Paint의 색상 설정

        paint.setTextSize(60);                     // 글자 크기
        paint.setTextAlign(Paint.Align.CENTER) ;   // 글자의 정렬 방식

        // 화면 출력용 문자열 만들기
        String str = String.format("화면 해상도 : %d x %d", w, h);

        // Canvas에 글자 출력
        canvas.drawText(str, w / 2, h / 2, paint);
    }

} // GameView
