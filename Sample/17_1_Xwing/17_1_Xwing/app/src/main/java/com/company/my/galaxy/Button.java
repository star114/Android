package com.company.my.galaxy;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.view.MotionEvent;

public class Button {
    // 버튼 이미지, 크기
    public Bitmap img;
    public int w, h;

    private boolean isTouch;
    private RectF rect;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Button(Bitmap bitmap, int x, int y) {
        w = bitmap.getWidth();
        h = bitmap.getHeight();

        img = Bitmap.createBitmap(bitmap, 0, 0, w, h);
        rect = new RectF(x, y, x + w, y + h);
    }

    //-----------------------------
    // Touch <-- Touch Event
    //-----------------------------
    public void onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if ( rect.contains(event.getX(), event.getY()) ) {
                isTouch = true;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            isTouch = false;
        }
    }

    //-----------------------------
    // Get Hit
    //-----------------------------
    public boolean hit() {
        boolean isHit = isTouch;
        isTouch = false;
        return isHit;
    }

    //-----------------------------
    // Get Press
    //-----------------------------
    public boolean press() {
        return isTouch;
    }

} // Button
