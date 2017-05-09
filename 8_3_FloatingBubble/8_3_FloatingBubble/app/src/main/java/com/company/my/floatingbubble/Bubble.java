package com.company.my.floatingbubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Random;

//-----------------------------
// 움직이는 비눗방울
//-----------------------------
public class Bubble {
    private int w, h;           // 화면의 크기
    private int sx, sy;         // 비눗방울의 이동 방향

    public Bitmap bubble;       // 비눗방울 비트맵
    public int x, y, bw;        // 비눗방울의 위치, 크기

    //-----------------------------
    // 생성자
    //-----------------------------
    public Bubble(Context context, int sw, int sh, int px, int py) {
        w = sw;     // 화면의 크기
        h = sh;
        x = px;     // 비눗방울의 초기 위치
        y = py;

        // 비눗방울의 크기를 랜덤하게 설정
        Random rnd = new Random();
        bw = rnd.nextInt(101) + 50;  // 50~150

        // 비눗방울 만들기
        bubble = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble);
        bubble = Bitmap.createScaledBitmap(bubble, bw * 2, bw * 2, true);

        // 비눗방울의 이동 속도
        sx = rnd.nextInt(5) + 1;    // 1~5;
        sy = rnd.nextInt(5) + 1;

        // 이동 방향을 +/-로 설정
        sx = rnd.nextInt(2) == 0 ? sx : -sx;
        sy = rnd.nextInt(2) == 0 ? sy : -sy;
    }

    //-----------------------------
    // 비눗방울 이동
    //-----------------------------
    public void update() {
        x += sx;
        y += sy;

        // 좌우의 끝인가?
        if (x < bw || x > w - bw) {
            sx = -sx;
            x += sx;
        }

        // 상하의 끝인가?
        if (y < bw || y > h - bw) {
            sy = -sy;
            y += sy;
        }
    }

} // Bubble
