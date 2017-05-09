package com.company.my.rabbit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {
    // 토끼 이미지와 리소스
    Bitmap rabbit;
    int[] imgRes = { R.drawable.rabbit_1, R.drawable.rabbit_2 };

    int w, h;       // 화면의 크기
    int x, y;       // 토끼의 좌표
    int rw, rh;     // 토끼 이미지의 크기

    // 생성자
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        rabbit = BitmapFactory.decodeResource( context.getResources(), imgRes[0] );
        rw = rabbit.getWidth() / 2;
        rh = rabbit.getHeight() / 2;
    }

    // 화면의 크기 구하기
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;
        this.h = h;

        x = w / 2; // 토끼의 좌표를 화면 중앙으로 설정
        y = h / 2;
    }
    // 그리기
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(rabbit, x - rw, y - rh, null);
    }

} // GameView

