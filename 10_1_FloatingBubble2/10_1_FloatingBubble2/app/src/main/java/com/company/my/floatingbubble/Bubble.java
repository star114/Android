package com.company.my.floatingbubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

//-----------------------------
// 움직이는 비눗방울
//-----------------------------
public class Bubble {
    // 화면의 크기
    private int scrW, scrH;

    // 속도와 이동 방향, Random
    private int speed;
    private PointF dir = new PointF();
    private Random rnd = new Random();

    // 비눗방울의 위치, 크기
    public float x, y;
    public int r;

    // 비트맵
    public Bitmap bubble;
    public boolean isDead;

    // 추가
    private Context context;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Bubble(Context context, int width, int height) {
        // 화면의 크기
        this.context = context;
        scrW = width;
        scrH = height;

        // 비눗방울의 크기를 랜덤하게 설정
        r = rnd.nextInt(71) + 50;  // 50~120

        // 비눗방울 만들기
        bubble = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble);
        bubble = Bitmap.createScaledBitmap(bubble, r * 2, r * 2, true);

        // 비눗방울 초기 설정
        initBubble();
    }

    //-----------------------------
    // 비눗방울 초기화
    //-----------------------------
    private void initBubble() {
        // 이동 속도
        speed = rnd.nextInt(51) + 150;         // 초속 150~200 픽셀

        // 이동 방향 : 0~360도
        double rad = Math.toRadians( rnd.nextInt(360) );

        dir.x = (float) Math.cos(rad) * speed;
        dir.y = (float) -Math.sin(rad) * speed;

        // 초기 위치 : 화면 전체
        x = rnd.nextInt(scrW - r * 4) + r * 2;
        y = rnd.nextInt(scrH - r * 4) + r * 2;
    }

    //-----------------------------
    // 비눗방울 이동
    //-----------------------------
    public void update() {
        x += dir.x * Time.deltaTime;     // 이동
        y += dir.y * Time.deltaTime;

        // 화면의 경계에서 반사
        if (x < r || x > scrW - r) {
            dir.x = -dir.x;
            x += dir.x * Time.deltaTime;     // 원위치
        }

        if (y < r || y > scrH - r) {
            dir.y = -dir.y;
            y += dir.y * Time.deltaTime;
        }
    }

    //-----------------------------
    // Hit Test
    //-----------------------------
    public boolean hitTest(float px, float py) {
        isDead = false;
        float dist = (x - px) * (x - px) + (y - py) * (y - py);

        if (dist < r * r) {
            int cnt = rnd.nextInt(6) + 25;     // 25~30
            int idx = GameView.mSmall.size();

            // GameView의 onDraw와 동기 유지
            synchronized(GameView.mSmall) {
                for (int i = 1; i <= cnt; i++) {
                    GameView.mSmall.add(idx, new SmallBubble(context, scrW, scrH, x, y));
                    idx++;
                }
            }
            isDead = true;
        }

        return isDead;
    }

} // Bubble

