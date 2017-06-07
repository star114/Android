package com.company.my.destroyalien;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonResources {
    // X-Wing
    static public Bitmap[] arXwing = new Bitmap[2];
    static public int xw, xh;

    // Alien
    static public Bitmap imgAlien;
    static public int aw, ah;

    // Laser
    static public Bitmap imgLaser;
    static public int lw, lh;

    // Torpedo
    static public Bitmap imgTorpedo;
    static public int tw, th;

    // Explosion
    static public Bitmap[] arExp = new Bitmap[25];
    static public int ew, eh;

    // 사운드, 사운드 id
    static private SoundPool mSound;
    static private int sndLaser;
    static private int sndBig;
    static private int sndSmall;

    // Game Object
    static public List<Laser> mLaser = Collections.synchronizedList( new ArrayList<Laser>() );
    static public List<Alien> mAlien = Collections.synchronizedList( new ArrayList<Alien>() );
    static public List<Torpedo> mTorpedo = Collections.synchronizedList( new ArrayList<Torpedo>() );
    static public List<Explosion> mExp = Collections.synchronizedList( new ArrayList<Explosion>() );

    //--------------------------
    // Set Resource  <-- GameView
    //--------------------------
    static public void set(Context context) {
        makeXwing(context);
        makeAlien(context);
        makeLaser(context);
        makeTorpedo(context);
        makeExplosion(context);
        makeSound(context);
    }

    //--------------------------
    // X-Wing
    //--------------------------
    private static void makeXwing(Context context) {
        // 원본 이미지
        arXwing[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.xwing);
        xw = arXwing[0].getWidth() / 2;
        xh = arXwing[0].getHeight() / 2;

        // 빨간색 필터 설정
        ColorFilter filter = new LightingColorFilter(0xFF0000, 0x404040);
        Paint paint = new Paint();
        paint.setColorFilter(filter);

        // 비어 있는 비트맵 만들기
        arXwing[1] = Bitmap.createBitmap(xw * 2, xh * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(arXwing[1]);

        // 비어 있는 비트맵에 원본 이미지를 point와 합성해서 출력
        canvas.drawBitmap(arXwing[0], 0, 0, paint);
    }

    //--------------------------
    // Laser
    //--------------------------
    private static void makeLaser(Context context) {
        imgLaser = BitmapFactory.decodeResource(context.getResources(), R.drawable.laser);
        lw = imgLaser.getWidth() / 2;
        lh = imgLaser.getHeight() / 2;
    }

    //--------------------------
    // Alien
    //--------------------------
    private static void makeAlien(Context context) {
        imgAlien = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien);
        aw = imgAlien.getWidth() / 2;
        ah = imgAlien.getHeight() / 2;
    }

    //--------------------------
    // Torpedo
    //--------------------------
    private static void makeTorpedo(Context context) {
        imgTorpedo = BitmapFactory.decodeResource(context.getResources(), R.drawable.torpedo);
        tw = imgTorpedo.getWidth() / 2;
        th = imgTorpedo.getHeight() / 2;
    }

    //--------------------------
    // Explosion
    //--------------------------
    private static void makeExplosion(Context context) {
        //  원본 이미지 - 5*5
        Bitmap org = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
        int w = org.getWidth() / 5;
        int h = org.getHeight() / 5;

        // 이미지 분리
        int n = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                arExp[n] = Bitmap.createBitmap(org, w * j, h * i, w, h);

                // 이미지 2배 확대
                arExp[n] = Bitmap.createScaledBitmap(arExp[n], w * 2, h * 2, true);
                n++;
            }
        }

        ew = arExp[0].getWidth() / 2;
        eh = arExp[0].getHeight() / 2;
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
        sndLaser = mSound.load(context, R.raw.laser, 1);
        sndBig = mSound.load(context, R.raw.big_explosion, 1);
        sndSmall = mSound.load(context, R.raw.small_explosion, 1);
    }

    //--------------------------
    // Play Sound  <-- X-Wing, Alien
    //--------------------------
    static public void sndPlay(String sKind) {
        switch (sKind) {
        case "Laser" :
            mSound.play(sndLaser, 1, 1, 1, 0, 1);
            break;
        case "Big" :
            mSound.play(sndBig, 1, 1, 1, 0, 1);
            break;
        case "Small" :
            mSound.play(sndSmall, 1, 1, 1, 0, 1);
        }
    }

    //--------------------------
    // Add Laser <-- X-Wing
    //--------------------------
    static public void addLaser(int scrW, int scrH, float x, float y) {
        synchronized (mLaser) {
            mLaser.add(new Laser(scrW, scrH, x, y));
        }
    }

    //--------------------------
    // Add Alien  <-- GameView
    //--------------------------
    static public void addAlien(int scrW, int scrH) {
        synchronized (mAlien) {
            mAlien.add( new Alien(scrW, scrH) );
        }
    }

    //--------------------------
    // Add Torpedo <-- Alien
    //--------------------------
    static public void addTorpedo(int scrW, int scrH, float x, float y) {
        synchronized (mTorpedo) {
            mTorpedo.add( new Torpedo(scrW, scrH, x, y ) );
        }
    }

    //--------------------------
    // Add Explosion <-- X-Wing, Alien
    //--------------------------
    static public void addExp(float x, float y, String sKind) {
        synchronized (mExp) {
            mExp.add( new Explosion(x, y, sKind) );
        }
    }

    //--------------------------
    // Update GameObject  <-- GameView
    //--------------------------
    static public void updateObjects() {
        // Laser
        synchronized (mLaser) {
            for (Laser tmp : mLaser) {
                tmp.update();
            }
        }

        // Alien
        synchronized (mAlien) {
            for (Alien tmp : mAlien) {
                tmp.update();
            }
        }

        // Torpedo
        synchronized (mTorpedo) {
            for (Torpedo tmp : mTorpedo) {
                tmp.update();
            }
        }

        // Explosion
        synchronized (mExp) {
            for (Explosion tmp : mExp) {
                tmp.update();
            }
        }
    }

    //--------------------------
    // Remove Dead <-- GameView
    //--------------------------
    static public void removeDead() {
        // Laser
        synchronized (mLaser) {
            for (int i = mLaser.size() - 1; i >= 0; i--) {
                if (mLaser.get(i).isDead) {
                    mLaser.remove(i);
                }
            }
        }

        // Torpedo
        synchronized (mTorpedo) {
            for (int i = mTorpedo.size() - 1; i >= 0; i--) {
                if (mTorpedo.get(i).isDead) {
                    mTorpedo.remove(i);
                }
            }
        }

        // Explosion
        synchronized (mExp) {
            for (int i = mExp.size() - 1; i >= 0; i--) {
                if (mExp.get(i).isDead) {
                    mExp.remove(i);
                }
            }
        }
    }

} // CommonResources
