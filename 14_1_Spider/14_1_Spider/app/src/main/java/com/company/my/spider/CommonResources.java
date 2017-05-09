package com.company.my.spider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class CommonResources {
    // Spider
    static public Bitmap[] arSpider = new Bitmap[5];
    static public int sw, sh;

    // Poison
    static public Bitmap imgPoison;
    static public int pw, ph;

    // 사운드, 사운드 id
    static private SoundPool mSound;
    static int sndPoison;

    //--------------------------
    // Set Resource  <-- GameView
    //--------------------------
    static public void set(Context context) {
        setSpider(context);
        setPoison(context);
        setSound(context);
    }

    //--------------------------
    // Spider 이미지
    //--------------------------
    static private void setSpider(Context context) {
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
    static private void setPoison(Context context) {
        imgPoison = BitmapFactory.decodeResource(context.getResources(), R.drawable.poison);
        pw = imgPoison.getWidth() / 2;
        ph = imgPoison.getHeight() / 2;
    }

    //--------------------------
    // Sound
    //--------------------------
    static private void setSound(Context context) {
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
        }

        sndPoison = mSound.load(context, R.raw.poison, 1);
    }

    //--------------------------
    // Play Sound  <-- Spider
    //--------------------------
    static public void sndPlay() {
        mSound.play(sndPoison, 1, 1, 1, 0, 1);
    }

} // CommonResources
