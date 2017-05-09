package com.company.my.canvasexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {

    int w, h;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 화면의 크기 구하기
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;      // 화면의 폭
        this.h = h;      // 화면의 높이
    }

    @Override
    protected void onDraw(Canvas canvas) {
//            // 1 translate
//            Paint paint = new Paint();
//            paint.setColor(0xff2040ff);
//
//            canvas.drawRect(200, 200, 450, 550, paint);
//
//            canvas.translate(300, 0);
//            canvas.drawRect(200, 200, 450, 550, paint);
//
//            Rect rect = new Rect(200, 200, 450, 550);
//            canvas.translate(300, 0);
//            canvas.drawRect(rect, paint);
//
//            canvas.translate(300, 0);
//            canvas.drawRect(rect, paint);

//            // 2 rotate
//            Paint paint = new Paint();
//            paint.setColor(Color.BLUE);
//
//            for (int i = 1; i < 6; i++) {
//                canvas.drawRect(1000, 200, 1500, 300, paint);
//                canvas.rotate(40, 900, 360);
//            }

//            // 3 Skew
//            Paint paint = new Paint();
//            paint.setColor(Color.rgb(0, 80, 255));
//            RectF rect = new RectF(0, 0, 350, 400);
//
//            canvas.translate(100, 200);
//            canvas.drawRect(rect, paint);
//
//            paint.setColor(Color.argb(255, 0, 180, 220));
//            canvas.translate(500, 0);
//
//            canvas.skew(1.29f, 0);      // tan 50˚
//            canvas.drawRect(rect, paint);

//            // 4 save & restore
//            Paint paint = new Paint();
//            paint.setColor(0x800000ff);             // 반투명한 파란색
//            Rect rect = new Rect(0, 0, 400, 400);
//
//            canvas.translate(500, 150);             // 초기 위치
//            canvas.drawRect(rect, paint);           // 사각형 그리기
//            canvas.save();
//
//            paint.setColor(0x80ff0000);             // 반투명한 빨간색
//            canvas.skew(1, 0);                      // x축으로 45˚ 밀침
//            canvas.drawRect(rect, paint);           // 평행사변형 그리기
//
//            canvas.restore();                       // Canvas 복원 후 다시 저장
//            canvas.save();
//
//            paint.setColor(0x8000ff00);             // 반투명한 초록색
//            canvas.skew(0, 1);                      // y축으로 45˚ 밀침
//            canvas.drawRect(rect, paint);           // 평행사변형 그리기
//
//            canvas.restore();
//            canvas.translate(400, 400);
//
//            paint.setColor(Color.BLUE);
//            canvas.drawRect(rect, paint);

        // 5 scale
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        // Diamond 도형
        Path path = new Path();
        path.moveTo(20, 0);
        path.lineTo(320, -150);
        path.lineTo(620, 0);
        path.lineTo(320, 150);
        path.lineTo(20, 0);

        canvas.translate(w / 2, h / 2);
        canvas.rotate(45);

        // 오른쪽
        canvas.drawPath(path, paint);

        canvas.scale(-1, 1);
        canvas.drawPath(path, paint);

        canvas.rotate(90);
        canvas.drawPath(path, paint);

        // 위쪽
        canvas.scale(-1, 1);
        canvas.drawPath(path, paint);
    }

}
