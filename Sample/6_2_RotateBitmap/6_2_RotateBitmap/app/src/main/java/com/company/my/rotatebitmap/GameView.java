package com.company.my.rotatebitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {

    // 이미지 리소스와 비트맵 배열
    int[] imgRes = new int[5];
    Bitmap[] rose = new Bitmap[5];

    // 화면의 중심점
    int cx, cy;

    // 장미의 기준점 배열
    int[] rw = new int[5];
    int[] rh = new int[5];

    // 생성자
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 장미 이미지를 비트맵으로 만들기
        for (int i = 1; i <= 4; i++) {
            imgRes[i] = R.drawable.rose_1 + (i - 1);
            rose[i] = BitmapFactory.decodeResource(getResources(), imgRes[i]);
        }

        // 기준점 구하기(1)
        rw[1] =  rose[1].getWidth() / 2;
        rh[1] =  rose[1].getHeight();

        // 기준점 구하기(2)
        rw[2] =  0;
        rh[2] =  rose[2].getHeight() / 2;

        // 기준점 구하기(3)
        rw[3] =  rose[3].getWidth() / 2;
        rh[3] =  0;

        // 기준점 구하기(4)
        rw[4] =  rose[4].getWidth();
        rh[4] =  rose[4].getHeight() / 2;
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
        int n = 1;      // 이미지 번호
        int mrg = 30;   // 여백

        // 원본 이미지의 기준점 표시용
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        // 원본 출력
        canvas.drawBitmap(rose[n], mrg, mrg, null);

        // 원본에 기준점 표시
        canvas.drawCircle(rw[n] + mrg, rh[n] + mrg, 20, paint);

        // 출력 횟수와 회전 각도
        int cnt = 16;
        float ang = 360f / cnt;

        canvas.scale(0.9f, 0.9f, cx, cy);
        // 360˚ 회전
        for (int i = 1; i <= cnt; i++) {               // 360도 회전
        // for (int i = 1; i <= cnt / 2 + 1; i++) {    // 180도 회전
            canvas.drawBitmap(rose[n], cx - rw[n], cy - rh[n], null);
            canvas.rotate(ang, cx, cy);
        }
    }

} // GameView
