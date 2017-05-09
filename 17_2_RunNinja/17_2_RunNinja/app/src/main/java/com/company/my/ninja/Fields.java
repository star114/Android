package com.company.my.ninja;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Fields {
    // 화면 크기
    private int w, h;

    // 근경과 원경의 스크롤 속도
    private int speedNear = 220;
    private int speedFar = 50;

    // 근경, 원경 비트맵, 근경/원경 이미지 수
    private Bitmap[] near = new Bitmap[2];
    private Bitmap[] far = new Bitmap[2];
    private int nearCnt = 2;
    private int farCnt = 2;

    // 이동 방향, 근경, 원경 Offset
    private int dir;
    private float ofsNear;
    private float ofsFar;

    // 근경, 원경 이미지 번호
    private int farNum1, farNum2;
    private int nearNum1, nearNum2;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Fields(Context context, int width, int height) {
        w = width;
        h = height;

        makeBitmap(context);
    }

    //-----------------------------
    // update <-- Thread
    //-----------------------------
    public void update() {
        scrollFar();
        scrollNear();
    }

    //-----------------------------
    // draw <-- OnDraw
    //-----------------------------
    public void draw(Canvas canvas) {
        // 원경
        canvas.drawBitmap(far[farNum1], -ofsFar, h * 0.14f, null);
        canvas.drawBitmap(far[farNum2], w - ofsFar, h * 0.14f, null);

        // 근경
        canvas.drawBitmap(near[nearNum1], -ofsNear, h * 0.6f, null);
        canvas.drawBitmap(near[nearNum2], w - ofsNear, h * 0.6f, null);
    }

    //-----------------------------
    // scroll Far - 원경
    //-----------------------------
    private void scrollFar() {
        // 이동(스크롤) 방향
        dir = (int) GameView.ninja.dir.x;
        ofsFar += dir * speedFar * Time.deltaTime;

        switch (dir) {
        case 1 : // 왼쪽으로 스크롤
            if (ofsFar > w) {
                ofsFar -= w;
                farNum1 = MathF.repeat(farNum1++, farCnt);
            }

            farNum2 = farNum1 + 1;
            if (farNum2 >= farCnt) farNum2 = 0;
            break;
        case -1 : // 오른쪽으로 스크롤
            if (ofsFar < 0) {
                ofsFar += w;
                farNum1--;
                if (farNum1 < 0) farNum1 = farCnt - 1;
            }

            farNum2 = farNum1 - 1;
            if (farNum2 < 0) farNum2 = farCnt - 1;
        }
    }

    //-----------------------------
    // scroll Near - 근경
    //-----------------------------
    private void scrollNear() {
        ofsNear += dir * speedNear * Time.deltaTime;

        switch (dir) {
        case 1 : // 왼쪽으로 스크롤
            if (ofsNear > w) {
                ofsNear -= w;
                nearNum1 = MathF.repeat(nearNum1++, nearCnt);
            }

            nearNum2 = nearNum1 + 1;
            if (nearNum2 >= nearCnt) nearNum2 = 0;
            break;
        case -1 : // 오른쪽으로 스크롤
            if (ofsNear < 0) {
                ofsNear += w;
                nearNum1--;
                if (nearNum1 < 0) nearNum1 = nearCnt - 1;
            }

            nearNum2 = nearNum1 - 1;
            if (nearNum2 < 0) nearNum2 = nearCnt - 1;
        }
    }

    //-----------------------------
    // Make Bitmap
    //-----------------------------
    private void makeBitmap(Context context) {
        // 원경
        for (int i = 0; i < 2; i++) {
            Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.far0 + i);
            far[i] = Bitmap.createScaledBitmap(tmp, w, h / 2, true);
        }

        // 근경
        for (int i = 0; i < 2; i++) {
            Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.near0 + i);
            near[i] = Bitmap.createScaledBitmap(tmp, w, (int) (h * 0.4f), true);
        }
    }

} // Fields
