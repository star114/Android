package com.company.my.spider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.Random;

public class CommonResources {
    // Spider
    static public Bitmap[] arSpider = new Bitmap[5];
    static public int sw, sh;

    // Poison
    static public Bitmap imgPoison;
    static public int pr;

    // 나비 종류, 나비
    static public final int FRY_CNT = 6;
    static public Bitmap[][] arFly = new Bitmap[FRY_CNT][10];
    static public int bw, bh;

    // 사운드, 사운드 id
    static private SoundPool mSound;
    static private int sndPoison;
    static private int sndCapture;

    //--------------------------
    // Set Resource  <-- GameView
    //--------------------------
    static public void set(Context context) {
        makeSpider(context);
        makePoison(context);
        makeButterfly(context);
        makeSound(context);
    }

    //--------------------------
    // 거미 이미지
    //--------------------------
    private static void makeSpider(Context context) {
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.spider);
        sw = tmp.getWidth() / 5;
        sh = tmp.getHeight();

        // 이미지 분리
        for (int i = 0; i < 5; i++) {
            arSpider[i] = Bitmap.createBitmap(tmp, sw * i, 0, sw, sh);
        }

        sw /= 2;
        sh /= 2;
    }

    //--------------------------
    // Poison 이미지
    //--------------------------
    private static void makePoison(Context context) {
        imgPoison = BitmapFactory.decodeResource(context.getResources(), R.drawable.poison);
        pr = imgPoison.getWidth() / 2;
    }

    //--------------------------
    // 나비 이미지
    //--------------------------
    private static void makeButterfly(Context context) {
        //  원본 이미지 - 애니메이션 이미지 10장
        Bitmap org = BitmapFactory.decodeResource(context.getResources(), R.drawable.butterfly);
        int w = org.getWidth();
        int h = org.getHeight();

        // 랜덤한 색상의 나비 만들기
        Random rnd = new Random();
        for (int i = 0; i < FRY_CNT; i++) {
            // 컬러를 입힐 작업용 이미지 만들기
            Bitmap tmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Paint paint = new Paint();

            // 랜덤 컬러와 컬러 필터 만들기
            int color = rnd.nextInt(0x808080) + 0x808080;
            ColorFilter filter = new LightingColorFilter(color, 0x404040);
            paint.setColorFilter(filter);

            // 빈 이미지에 필터를 적용해서 출력
            Canvas canvas = new Canvas(tmp);
            canvas.drawBitmap(org, 0, 0, paint);

            // 이미지 분리
            int w2 = w / 10;
            for (int j = 0; j < 10; j++) {
                arFly[i][j] = Bitmap.createBitmap(tmp, w2 * j, 0, w2, h);
            }
        }

        bw = w / 20;
        bh = h / 2;
    }

    //--------------------------
    // makeSound
    //--------------------------
    private static void makeSound(Context context) {
        // 롤리팝 이전 버전인가?
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mSound = new SoundPool(5, AudioManager.STREAM_MUSIC, 1);
        } else {
            AudioAttributes attributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

            mSound = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(5).build();
        } // if

        // 사운드 리소스 읽기
        sndPoison = mSound.load(context, R.raw.poison, 1);
        sndCapture = mSound.load(context, R.raw.capture, 1);
    }

    //--------------------------
    // Play Sound  <-- Spider, Butterfly
    //--------------------------
    static public void sndPlay(String sKind) {
        switch (sKind) {
        case "Poison" :
            mSound.play(sndPoison, 1, 1, 1, 0, 1);
            break;
        case "Capture" :
            mSound.play(sndCapture, 1, 1, 1, 0, 1);
        }
    }

} // CommonResources
