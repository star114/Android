package com.company.my.ninja;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;

public class Button {
    // 이미지, 위치
    public Bitmap img;
    public int x, y;

    // 터치 id, 터치 판정용 Rect
    private int ptrId = -1;
    private RectF rect;

    // 버튼을 눌렀나?
    public boolean isTouch;
    public float scale = 1;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Button(Bitmap bitmap, Point pos) {
        // 버튼의 크기, 비트맵
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        img = bitmap;

        // 터치 영역
        rect = new RectF(pos.x, pos.y, pos.x + w, pos.y + h);

        // 위치
        x = pos.x;
        y = pos.y;
    }

    //-----------------------------
    // action <-- Touch Event
    //-----------------------------
    public void action(int id, boolean isDown, float x, float y) {
        if ( isDown && rect.contains(x, y) ) {
            isTouch = true;
            ptrId = id;
        }

        if ( !isDown && id == ptrId) {
            isTouch = false;
        }
    }

} // Button
